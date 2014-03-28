---
layout: page
category : manual
tags : [パンくずナビ]
title: "どうすればパンくずを表示できる？"
---

SINAVI J-Frameworkでは画面IDによるパンくずを表示するためのタグライブラリを提供しています。  

パンくずとは、ユーザが遷移してきた画面の履歴をリスト化して表示するユーザインタフェースパターンの種類です。  
これにより、ユーザは任意の画面に戻ることができるようになります。  
パンくず自体はよく利用されるパターンですが、これをプログラムにハードコードしてしまうと、  
画面遷移順序が変更された場合の修正コストの増加が懸念されます。  
そこで、SINAVI J-Frameworkでは画面IDを利用して動的にパンくずを表示することができます。  

表示は簡単です。  

<script src="https://gist.github.com/tetsuya-oikawa/4cd81b6f6b80451ada49.js"></script>

これだけです。デフォルトでは、例えばユーザが画面A、画面B、画面Cと遷移した場合、以下のように表示されます。  

{% highlight text %}
  画面A > 画面B > 画面C
{% endhighlight %}

パンくずの表示に画面IDを利用するため、以下のように画面IDを定義し、  
pankuzu属性にtrueを指定する必要があります。  

<script src="https://gist.github.com/tetsuya-oikawa/39f11e7ede14d6c75845.js"></script>

これは、以下のコードと等価です。  

<script src="https://gist.github.com/tetsuya-oikawa/eb3701487fd0ed077451.js"></script>

この場合はパンクズに画面IDが表示されてしまうため、  

<script src="https://gist.github.com/tetsuya-oikawa/c3e9f9870b2db76883e3.js"></script>
 
とすることで、「画面A」がパンクズとして表示されます。 これと等価なコードは、以下の通りです。  

<script src="https://gist.github.com/tetsuya-oikawa/35f4f8df3b4a6b2fcc10.js"></script>

パンくずのURLはデフォルトで設定されますが、 任意の指定も可能です。  

変更する場合、以下のようにurl属性に任意のURLを指定します。  
このときurl属性にはコンテキストパスより指定してください。  

<script src="https://gist.github.com/tetsuya-oikawa/c3e9f9870b2db76883e3.js"></script>

さらに、パンくずにリクエストパラメータを追加することもできます。  

<script src="https://gist.github.com/tetsuya-oikawa/f7b58439f45163f10c4c.js"></script>

※このタグライブラリの詳細は [PankuzuTag][PankuzuTag] のjavadocを参照してください。


[PankuzuTag]:{{ site.baseurl }}docs/projects/jfw-web-core/{{ site.jfw-web-core.version }}/api/jp/co/ctc_g/jse/vid/PankuzuTag.html
