---
layout: page
category : manual
tags : [データベースアクセス]
title: "逐次処理するにはどうしたらいい？"
---

検索結果を逐次処理するには MyBatis の ResultHandler 機構を利用します。  

ResultHandler は 検索結果を逐次処理するためのインタフェースを既定したもので、
handleResult(ResultContext) メソッドを実装することにより検索結果を逐次処理することが可能になります。  
また、引数として与えられる ResultContext は検索結果に関するコンテキスを表現するオブジェクトで検索件数や検索結果にアクセスする手段を提供します。  

では、具体的な実装手順を説明します。  

実装手順は下記の２ステップです。  

  * ResultHandler インタフェース の実装クラスの作成
  * Daoの実装

まず、ResultHandler インタフェース を実装します。  
ResultHandler インタフェースに定義されているメソッドは handleResult(ResultContext) メソッドのみなので実装は至って単純です。  
この handleResult(ResultContext) メソッド に逐次処理の実装を行うだけです。  
下記のように ResultContext の getResultObject() メソッドを通して検索結果を逐次取得し目的の処理を実装します。  

<script src="https://gist.github.com/tetsuya-oikawa/bde8e069d7bdb8bbe69c.js"></script>

次に、Daoの実装です。  
基本的なCRUD処理と同様に逐次処理についても SqlSession インタフェースを通して処理を呼出します。  
SqlSession には ResultHandler を引数に渡す select メソッドが定義されており、この select メソッドに ResultHandler のインタスタンスを渡しSQL ステートメントを実行します。  
MyBatis がSQL ステートメントを実行後、検索結果を順次取得する度に ResultHandler#handleResult(ResultContext) メソッドを起動しすることによって順次処理が実現されます。  

<script src="https://gist.github.com/tetsuya-oikawa/a464981adcf4fdcf4767.js"></script>

SqlSession インタフェースには下記の３種類の select メソッドが定義されます。  
目的の処理に合わせて適宜選択し実装を行って下さい。  

  * select(String statement, Object parameter, ResultHandler handler) : void
  * select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) : void
  * select(String , ResultHandler handler) : void

SqlSession のAPIの詳細については、 [MyBatisのAPIマニュアル][SqlSession] を参照して下さい。  

[SqlSession]: http://mybatis.github.io/mybatis-3/apidocs/reference/org/apache/ibatis/session/SqlSession.html
