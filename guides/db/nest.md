---
layout: page
category : manual
tags : [データベースアクセス]
title: "Summary-Detail のようなデータ構造に SQL Query の結果セットをマッピングするにはどうすればいい？"
---

Summary-Detail のようなデータ構造(has-manyのデータ構造)に SQL Query の結果セットをマッピングするには２つの方法があります。  
  * MyBatis の MapperXML の結果マッピング機能を利用する方法
  * ネストしたデータの検索を別の SQL Query を発行し取得する方法

以降にそれぞれの方法について記載します。  

なお、前提となるデータベースのテーブル構成、及び、SQL Query の結果セットをマッピングするドメインオブジェクトの構成は下図の通りです。  


![データベースのテーブル構造]({{ BASE_PATH }}/images/db/nest_00.png "データベースのテーブル構造")

   図１ データベースのテーブル構造

![ドメインオブジェクトの構造]({{ BASE_PATH }}/images/db/nest_01.png "ドメインオブジェクトの構造")

   図２ ドメインオブジェクトの構造

### MyBatis の MapperXML の結果マッピング機能を利用する方法

MyBatis の Mapper XML 結果マッピング機能を利用し、ネストした結果セットのマッピング定義を行います。  

データベースに発行する SQL Query は次の通りです。  

<script src="https://gist.github.com/tetsuya-oikawa/145afee5c559a43da81d.js"></script>

SQL Query の実行結果として下表のような結果が得られます。  
この実行結果をSummaryクラスとSummaryクラスに定義されているDetailクラスのList型のプロパティにマッピングします。  

  <table class="table table-bordered table-hover">
    <thead>
      <tr>
        <th>SUMMARY_ID</th>
        <th>SUMMARY_INFO</th>
        <th>DETAIL_ID</th>
        <th>DETAIL_INFO</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>1</td>
        <td>SUMMARY_INFO 1</td>
        <td>1</td>
        <td>DETAIL_INFO 1</td> 
      </tr>
      <tr>
        <td>1</td>
        <td>SUMMARY_INFO 1</td>
        <td>2</td>
        <td>DETAIL_INFO 2</td> 
      </tr>
      <tr>
        <td>2</td>
        <td>SUMMARY_INFO 2</td>
        <td>3</td>
        <td>DETAIL_INFO 3</td> 
      </tr>
      <tr>
        <td>2</td>
        <td>SUMMARY_INFO 2</td>
        <td>4</td>
        <td>DETAIL_INFO 4</td> 
      </tr>
      <tr>
        <td>2</td>
        <td>SUMMARY_INFO 2</td>
        <td>5</td>
        <td>DETAIL_INFO 5</td> 
      </tr>
      <tr>
        <td>3</td>
        <td>SUMMARY_INFO 3</td>
        <td>6</td>
        <td>DETAIL_INFO 6</td> 
      </tr>
      <tr>
        <td>4</td>
        <td>SUMMARY_INFO 4</td>
        <td>7</td>
        <td>DETAIL_INFO 7</td> 
      </tr>
      <tr>
        <td>4</td>
        <td>SUMMARY_INFO 4</td>
        <td>8</td>
        <td>DETAIL_INFO 8</td> 
      </tr>
      <tr>
        <td>5</td>
        <td>SUMMARY_INFO 5</td>
        <td>9</td>
        <td>DETAIL_INFO 9</td> 
      </tr>
    </tbody>
  </table>

マッピングの定義は Mapper XML の **resultMap 要素**  に行います。  
**collection 要素** を用いてネストしたマッピング定義を行うのが重要なポイントです。  
collection 要素では **ofType 属性** を使用してListに含まれるJavaオブジェクトの型を定義する必要があります。  

<script src="https://gist.github.com/tetsuya-oikawa/c7c16936d79882907fbd.js"></script>

このようにネストしたマッピング定義を行うことにより、Summary-Detailのようなデータ構造(いわゆる has-manyのデータ構造)に SQL Query の結果セッをマッピングするこのができます。  
また、select 要素の定義例を以下に示します。  
select 要素の定義は単表に対する select ステートメント を定義するケースと大差ありません。  

<script src="https://gist.github.com/tetsuya-oikawa/eba07a1a8e479802112e.js"></script>

### ネストしたデータの検索を別の SQL Query を発行し取得する方法


Mapper XML にネストしたデータの検索を行う SQL Query 別途定義しドメインオブジェクトへのマッピングを定義します。  

select 要素の定義は、SUMMARY表に対する SQL Query の定義とDETAIL表に対する SQL Query の定義を別々に行ます。  

<script src="https://gist.github.com/tetsuya-oikawa/ad93a511a3617e0a4a0a.js"></script>

<script src="https://gist.github.com/tetsuya-oikawa/8d43041a507deecca559.js"></script>

次に resultMap 要素によるマッピングの定義ですが、ここでも **collection 要素** を利用します。  
ポイントは以下の２点です。  
  * select 属性を利用してネストした SQL Query の select ステートメントを指定する。  
  * column 属性でネストされた select ステートメントに引数として渡される列名、あるいは列の別名を指定する。  複数のパラメータを渡す場合は、column="{prop1=col1,prop2=col2} のように指定することが可能。  

<script src="https://gist.github.com/tetsuya-oikawa/7375bf001b8ba11e1f27.js"></script>

なお、この方法の利用には注意が必要です。  ここで説明する SQL Query をネストさせる方法は、SQL Query の定義は簡単ですが性能面でのオーバーヘッドが高く利用できるケースは限定されます。  例えばデータベースの検索結果を一覧表示するような予め必要であることが分かっているデータをわざわざネストした SQL Query で検索することは性能面の理由により推奨していません。  
Lazy Loading 機能と組合せ必要になった段階でのみネストした SQL Query が実行されるようなケースにおいて利用することを推奨します。  

