---
layout: page
category : manual
tags : [入力値検証]
title: "どうすれば入力値検証が実行できる？"
---

SINAVI J-Frameworkの入力値検証は、Spring MVC がサポートしている Bean Validation をベースに構成されています。  

入力値検証には、次の2種類があります。  

 - 単項目チェック
 - 相関チェック

それぞれの方法について以降に記載します。  

### 単項目チェックの実行

まず、入力値検証を有効にするためには外部に公開している Controller メソッドの @ModelAttribute アノテーションが付与されているドメインの引数定義に対して @Validated というアノテーションを付与します。  
また、入力値検証に失敗した場合の遷移先は@PostBack.Actionというアノテーションで設定します。  
例えば、createというパスが実行されたタイミングでFooクラスに対して入力値検証を有効にし、入力値検証に失敗したときにはreadyToCreateのパスに遷移する場合、

<script src="https://gist.github.com/t-oi/0bb183b88d9a548abf30.js"></script>

というように設定することで実現できます。  

次に、どの入力項目に対してどのバリデータを実行するかどうかの指定は、該当のドメインのプロパティ定義に対して入力チェックアノテーションを付与することによって行います。  

例えば、フラグ値を保持するプロパティに対して数値チェックと長さチェックを実行する場合、

<script src="https://gist.github.com/t-oi/6b495aff2005d20884a3.js"></script>

というように設定することで実現できます。  

最後に入力値検証に失敗した場合にエラーメッセージの表示には、SINAVI J-Frameworkのメッセージタグライブラリを利用します。  
利用方法は、

<script src="https://gist.github.com/t-oi/dd402fe2c48fa3f3fb73.js"></script>

または

<script src="https://gist.github.com/t-oi/c682fc6c5d62f1ba0547.js"></script>

これだけです。エラーメッセージを表示したい部分に

<script src="https://gist.github.com/t-oi/dd402fe2c48fa3f3fb73.js"></script>

を指定することで出力されます。  
また、特定の入力項目のみ表示したい場合、property属性に指定すると指定されたもののみ出力することができます。  


### 相関チェックの実行

入力値検証を有効にする方法は、単項目チェックと同様です。違いは入力チェックアノテーションを付与する場所です。  
相関チェックの場合は、クラス定義に対して入力チェックアノテーションを付与します。  

例えば、From-Toのような範囲検索を行う際は入力された値の前後関係をチェックする必要があります。  
日付値を保持する2つのプロパティに対して前後関係をチェックを実行する場合、

<script src="https://gist.github.com/t-oi/521475cefd7372bc4933.js"></script>

というように設定することで実現できます。  

なお、相関チェックは from属性 に指定されたプロパティに対するチェックとしてフレームワークに認識されます。  
@BeforeEqualsTo アノテーションのチェックでエラーが発生した場合は、fromSalesDate プロパティに対する入力値検証エラーとして認識されます。  
この場合特定の入力項目に対するエラーメッセージの表示を定義している場合、&lt;jse:messeages /&gt; の property属性に fromSalesDate が指定されている方のみ表示されます。  
