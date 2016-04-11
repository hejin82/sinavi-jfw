---
layout: page
category : manual
tags : [リソース管理]
title: "プロパティファイルの値を取得する場合、どのように取得したらいい？"
---

SINAVI J-Frameworkのリソース管理機能(jp.co.ctc_g.jfw.core.resource.Rs#find)を利用すれば、簡単に取得することができます。  
プロパティファイルより取得するサンプルコードは以下の通りです。  

<p>プロパティファイル</p>
```
[MessageResource.properties]
hoge.foo.bar=値を取得できるか？
```

<p>プロパティ値取得サンプルコード</p>
```
String key = "hoge.foo.bar";
String value = Rs.find(key);
System.out.println(value);
```
