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

```
[ExampleController.java]
@Controller
public class ExampleController {
    @RequestMapping("/vid/example")
    @ViewIdConstraint(allow = "VID#0001")
    public String example() {
        return "/vid/example";
    }
}
```

のように定義します。許可する画面IDをallow属性で指定します。
なお、許可されていない画面IDからのアクセスがあった場合、InvalidViewTransitioniExceptionが発生します。

また、複数の画面IDへの許可が必要な場合には、

```
@ViewIdConstraint(allow = "VID#0001|VID#0002")
```

のように|記号で区切ってください。

こららの詳細は [ViewIdConstraint][ViewIdConstraint] アノテーションのJavadocを参照してください。

[ViewIdConstraint]: {{ site.baseurl}}docs/projects/jfw-web-core/{{ site.jfw-web-core.version }}/api/jp/co/ctc_g/jse/vid/ViewIdConstraint.html