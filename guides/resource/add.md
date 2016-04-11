---
layout: page
category : manual
tags : [リソース管理]
title: "リソースファイルを追加する場合、どのように設定したらいい？"
---

新しくリソースファイルを追加する場合、
Springの設定ファイルに次のような定義がされている部分に  
クラスパスからの相対パスでリソースファイルのファイル名を指定してください。  

<p>Spring設定ファイル</p>
```
[Context.xml]
<bean id="messageSourceLocator" class="jp.co.ctc_g.jfw.core.resource.MessageSourceLocator" >
<bean id="messageSource"
  class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
  <property name="basenames">
    <list>
      <value>classpath:FrameworkResources</value>
      <value>classpath:ApplicationResources</value>
      <value>classpath:ErrorResources</value>
      <value>classpath:ValidationResources</value>
      <value>classpath:NewResources</value> <!-- 追加するリソースファイルのファイル名を指定してください。 -->
    </list>
  </property>
  <property name="defaultEncoding" value="UTF-8" >
</bean>
```
