---
layout: page
category : manual
tags : [リソース管理]
title: "アプリケーションをロードし直すことなく、リソースファイルの値を更新できる？"
---

はい。アプリケーションをロードし直すことなく、更新されたリソースファイルの値を取得することができます。  
このようにリソースファイルのリロードを実施する場合は、Springの設定ファイルに次のように更新の読込間隔を指定してください。  

※詳細はSpringの [ReloadableResourceBundleMessageSource][ReloadableResourceBundleMessageSource] を参照してください。

<script src="https://gist.github.com/tetsuya-oikawa/d4aa8b279ac7ecd9b82c.js"></script>

[ReloadableResourceBundleMessageSource]: http://docs.spring.io/spring/docs/4.0.1.RELEASE/javadoc-api/org/springframework/context/support/ReloadableResourceBundleMessageSource.html