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

```
[pankuzu.jsp]
<vid:pankuzu />
```

これだけです。デフォルトでは、例えばユーザが画面A、画面B、画面Cと遷移した場合、以下のように表示されます。  

{% highlight text %}
  画面A > 画面B > 画面C
{% endhighlight %}

パンくずの表示に画面IDを利用するため、以下のように画面IDを定義し、  
pankuzu属性にtrueを指定する必要があります。  

```
[pankuzu.jsp]
<vid:is id="VID#0001" pankuzu="true" />
```

これは、以下のコードと等価です。  

```
ViewId vid = new ViewId("VID#0001");
vid.setPankuzu(true);
vid.fill(request);
ViewId.is(vid, request);
```

この場合はパンクズに画面IDが表示されてしまうため、  

```
[pankuzu.jsp]
<vid:is id="VID#0001" pankuzu="true" label="画面A" />
```
 
とすることで、「画面A」がパンクズとして表示されます。 これと等価なコードは、以下の通りです。  

```
ViewId vid = new ViewId("VID#0001");
vid.setPankuzu(true);
vid.setLabel("画面A");
vid.fill(request);
ViewId.is(vid, request);
```

パンくずのURLはデフォルトで設定されますが、 任意の指定も可能です。  

変更する場合、以下のようにurl属性に任意のURLを指定します。  
このときurl属性にはコンテキストパスより指定してください。  

```
[pankuzu.jsp]
<vid:is id="VID#0001" pankuzu="true" label="画面A" />
```

さらに、パンくずにリクエストパラメータを追加することもできます。  

```
[pankuzu.jsp]
<vid:is id="VID#0001" pankuzu="true" label="画面A" url="/foo/bar/baz.do">
 <vid:param name="name" value="value" />
</vid:is>
```

※このタグライブラリの詳細は [PankuzuTag][PankuzuTag] のjavadocを参照してください。


[PankuzuTag]:{{ site.baseurl }}docs/projects/jfw-web-core/{{ site.jfw-web-core.version }}/api/jp/co/ctc_g/jse/vid/PankuzuTag.html
