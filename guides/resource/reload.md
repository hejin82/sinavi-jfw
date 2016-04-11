---
layout: page
category : manual
tags : [リソース管理]
title: "アプリケーションをロードし直すことなく、リソースファイルの値を更新できる？"
---

はい。アプリケーションをロードし直すことなく、更新されたリソースファイルの値を取得することができます。  
このようにリソースファイルのリロードを実施する場合は、Springの設定ファイルに次のように更新の読込間隔を指定してください。  

※詳細はSpringの [ReloadableResourceBundleMessageSource][ReloadableResourceBundleMessageSource] を参照してください。

```
[Context.xml]
<bean id="messageSourceLocator" class="jp.co.ctc_g.jfw.core.resource.MessageSourceLocator" />
<!-- class属性をデフォルト値よりReloadableResourceBundleMessageSourceに変更してください。 -->
<bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
  <property name="basenames">
    <list>
      <value>file:/home/user/SystemConfig</value>
    </list>
  </property>
  <property name="cacheSeconds" value="60" />
  <property name="defaultEncoding" value="UTF-8" />
</bean>
```

[ReloadableResourceBundleMessageSource]: http://docs.spring.io/spring/docs/4.0.1.RELEASE/javadoc-api/org/springframework/context/support/ReloadableResourceBundleMessageSource.html