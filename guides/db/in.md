---
layout: page
category : manual
tags : [データベースアクセス]
title: "IN句の指定はどのようにすればいい？"
---

IN句を利用したSQL Queryを構成するには、MyBatisの動的SQLの機能を利用します。

以下ではコレクションの要素をイテレーション処理し IN 句を使った条件を構築する方法を説明します。

SELECT ステートメントの指定は次の通りです。

<script src="https://gist.github.com/tetsuya-oikawa/685754c0987da094e365.js"></script>

**foreach 要素** を利用するのが重要なポイントです。  
foreach 要素は非常に強力で、イテレーション処理の対象となるコレクションを指定する **collection 属性** と、ループ内で要素を格納する変数 item、ループ回数を格納する 変数 index を宣言することができます。  
これによって collection 属性で指定された値によってparameterType 属性で指定したJavaオブジェクトのプロパティからコレクションを取得し変数 item に順次値が設定されます。また、開始・終了の文字列とイテレーションの合間に出力する区切り文字を指定することもできます。  
なお、指定した区切り文字ですが、余分に出力されることはありません。

MyBatisのSqlSession の検索系メソッド（selectList）の引数に直接ListやArrayを渡すことも可能です。  
この場合、MyBatis は渡された引数を Map に格納しキーとして名前を設定します。List のインスタンスは "list" というキーで Map に格納され、Arrayの場合は "array" というキーで格納されます。  

