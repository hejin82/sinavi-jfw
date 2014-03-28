---
layout: page
category : manual
tags : [Beanプロパティ自動設定]
title: "どこにユーザIDを設定すれば、自動でプロパティに設定できる？"
---

SINAVI J-Frameworkの自動プロパティ設定機能を利用し、作成者や更新者の情報に値を自動で設定することができますが、  
デフォルトの状態のままではSpringの設定ファイルに定義されているデフォルト値が必ず設定されます。  

デフォルト値をログインユーザのログインIDなどに変更したい場合には  
java.security.Principalインタフェースを実装し、  
getNameメソッドの戻り値にログインIDなどを返すようにすれば設定された値が自動設定されます。  


※SpringSecurityなどの認証・認可ライブラリと併用することをお勧めします。  
SpringSecurityでは認証情報はUserDetailsインターフェースを実装したものを用意し、  
SecurityContextHolder.getContext().getAuthentication()よりPrincipalを取得することができます。  
