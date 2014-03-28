---
layout: page
category : manual
tags : [画面ID管理/画面フロー制御]
title: "どうすれば許可された画面からの遷移だけに制限できる？"
---

ViewIdConstraintアノテーションを利用するとこで、ある画面からのみ実行できる処理や、ある画面からのみ遷移できる画面などを実現することができます。
まずこのアノテーションを有効に利用するには、以下の条件を満たす必要があります。

 - ViewId.is(..)や &lt;vid:is id=".." /&gt; を利用している
 - Controllerアノテーションを利用している。

例えば、直前の画面がVID#0001である場合のみ、実行を許可する場合、

<script src="https://gist.github.com/tetsuya-oikawa/b6c747f3f9be0eb7bb49.js"></script>

のように定義します。許可する画面IDをallow属性で指定します。
なお、許可されていない画面IDからのアクセスがあった場合、InvalidViewTransitioniExceptionが発生します。

また、複数の画面IDへの許可が必要な場合には、

<script src="https://gist.github.com/tetsuya-oikawa/b3405b297db54742a5b4.js"></script>

のように|記号で区切ってください。

こららの詳細は [ViewIdConstraint][ViewIdConstraint] アノテーションのJavadocを参照してください。

[ViewIdConstraint]: {{ site.baseurl}}docs/projects/jfw-web-core/1.0.0.0.M1/api/jp/co/ctc_g/jse/vid/ViewIdConstraint.html