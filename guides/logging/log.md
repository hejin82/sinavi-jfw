---
layout: page
category : manual
tags : [ロギング]
title: "ユーザ、セッションIDなどをログに出力したい場合は？"
---

ログを出力するとき、常にユーザIDやセッションIDといった情報を付加する方法について記載します。  

[SLF4j][SLF4j] の APIであるMDC(Mapped Diagnostic Context)を利用しユーザIDやセッションIDなどをストアすることにより、
ログ出力時にユーザID、セッションIDが付加されたメッセージを生成させることができます。  

まず、SLF4jのMDCにユーザIDやセッションIDなどを登録するServletFilterを用意します。  
例えば、ユーザIDがPrincipalに格納されている場合、

<script src="https://gist.github.com/tetsuya-oikawa/07c1a45eb3917284804d.js"></script>

のようにSLF4jのMDCに登録します。  

次にログフォーマットに従い、LOGBackの設定ファイル(logback.xml)のレイアウトでMDCの値を参照することにより、
MDCに登録した値をログに出力することができます。  
例えば、
{% highlight text %}
日付 [ログレベル] [userId=xxxxxx] [sessionId=xxxxxxx] ログメッセージ
{% endhighlight %}
のようにログを出力する場合、

<script src="https://gist.github.com/tetsuya-oikawa/42d3f0eff65f42dcfffa.js"></script>

のようにレイアウトを設定することで、出力できます。  

MDCの値を参照する際は、
{% highlight xml %}
[* %X{key} *]
{% endhighlight %}
で参照します。  

これで、ログを出力したときにユーザIDやセッションIDがログに出力されます。  

<script src="https://gist.github.com/tetsuya-oikawa/816c94080ab640c13619.js"></script>

※出力例
{% highlight text %}
12:00:00.000 [DEBUG] [userId=z123456] [sessionId=xxxxxxxxxxx] 検索条件は1111です。  
{% endhighlight %}


[SLF4j]:http://www.slf4j.org/