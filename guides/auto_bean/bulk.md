---
layout: page
category : manual
tags : [Beanプロパティ自動設定]
title: "複数のデータを更新する場合に自動でプロパティを設定するにはどうしたらいい？"
---

SINAVI J-Frameworkの自動プロパティ設定機能を利用すると作成日・作成者や更新日・更新者といったカラムに対応するJavaBeansのプロパティに自動で適切な値を設定することができます。  
この自動プロパティ設定機能を有効にするにはSpringの設定ファイル(Root-Context.xml)にトランザクション開始時刻を設定するインターセプタの設定と  
リクエストに関連付けらているjava.security.Principalをスレッドローカルに登録するフィルタの設定を行う必要があります。  

<script src="https://gist.github.com/tetsuya-oikawa/887df6abda4ba0a983a6.js"></script>

これで自動プロパティ設定機能を利用することができます。  
次にJavaBeansに値を設定するためには対象のJavaBeansのプロパティのアクセサメソッド(setterメソッド)に  
JavaBeansの対象プロパティのsetterメソッドに自動プロパティ設定機能のアノテーションを付与することで  
値が自動で設定されます。

<script src="https://gist.github.com/tetsuya-oikawa/a3b97caccb385ed86441.js"></script>
自動プロパティ設定機能の使い方は簡単なのですが、ここで注意事項があります。  

自動プロパティ設定機能はパフォーマンスに配慮しているため、複数のデータに対しては設定しません。  
例えば、社員情報のリストや配列でデータを登録・更新する場合には  


<script src="https://gist.github.com/tetsuya-oikawa/1e31afb23e56a054f69e.js"></script>

のように定義することがありますが、このときは自動プロパティ設定機能は何も設定しません。  
必ず単一のJavaBeansである必要があります。  
それではServiceのインターフェースを単一にし、Controllerから複数回呼び出せばよいかというと  
今度はトランザクション境界の問題があり、単一にすることはできません。  
ではどうするかというとDaoを呼び出されるタイミングで実行するようにインターセプタを設定することで解決することができます。  

<script src="https://gist.github.com/tetsuya-oikawa/9ce9d1e264b29708cd20.js"></script>
