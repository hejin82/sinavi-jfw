---
layout: page
category : manual
tags : [ページング]
title: "どうすればページングが表示できる？"
---

SINAVI J-Frameworkではページングを表示するためのタグライブラリを提供しています。  

ページングとは、検索結果など大量の一覧項目をページ分割して表示する場合、ページを送るためのリンクを表示するユーザインタフェースパターンの種類です。  
これにより、ユーザはスクロールせずに、クリックによる画面遷移によって簡単に情報を取得できるようになります。  
SINAVI J-Frameworkでは独自のPartialListというリストを利用して動的にページングを表示することができます。  

表示は簡単です。  

<script src="https://gist.github.com/tetsuya-oikawa/f8d97335021c67cf6e15.js"></script>

これだけです。  以下のように表示されます。  


{% highlight text %}
  << < 1 2 3 4 5 ... > >>
{% endhighlight %}

partial属性にPartialListを指定することで、そのListの総数や1ページのインデックスなどを表示することができます。  

さらにSINAVI J-FrameworkではMyBatisを一部拡張し、このPartialListを簡単に生成できるようにしています。  

まず、検索条件を保持するドメインクラスはPaginatableSupportを継承して実装する必要があります。  

<script src="https://gist.github.com/tetsuya-oikawa/1e8dc8967ff32a5e5a93.js"></script>

次に、DaoインタフェースにサフィックスがWithPaginatingとなるメソッドを定義し、引数には検索条件を保持するドメインを指定します。  

<script src="https://gist.github.com/tetsuya-oikawa/0ab6af79a4fc33def3ae.js"></script>

<script src="https://gist.github.com/tetsuya-oikawa/068c55c31b692d195aaf.js"></script>

検索条件に応じて検索するSQLに加えて、検索結果の件数を取得するSQLも定義する必要があります。  
これはWithPaginatingのサフィックスに::countとなるIDで検索結果の件数を取得するSQLを定義します。  

<script src="https://gist.github.com/tetsuya-oikawa/c58bf9de0101deb1940b.js"></script>

これで検索を実行する前に検索結果の件数を取得し、PartialListに設定されます。  

Controllerクラスで画面から送信されてきたページ番号をもとにページの開始位置や終了位置を計算し、検索を実行します。  
計算するときにはPaginates#set(Paginatable, int)やPaginates#set(Paginatable, int, int)などを利用してください。  

<script src="https://gist.github.com/tetsuya-oikawa/1fec74147640bc3d7ee7.js"></script>

最後にJSPにページングを表示します。  

<script src="https://gist.github.com/tetsuya-oikawa/0455cf86565ea7e521f1.js"></script>