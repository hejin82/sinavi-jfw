---
layout: page
category : manual
tags : [例外処理]
title: "例外はどのように使い分けたらいい？"
---

SINAVI J-Frameworkでは次の3つの基底例外クラスを設けています。

 * アプリケーション例外
  * 回復可能なアプリケーション例外 ( [ApplicationRecoverableException][ApplicationRecoverableException] )
  * 回復不可能なアプリケーション例外 ( [ApplicationUnrecoverableException][ApplicationUnrecoverableException] )
 * システム例外 ( [SystemException][SystemException] )

### 回復可能なアプリケーション例外

回復可能なアプリケーション例外を利用するときの指針は以下の通りです。

 * アプリケーションが自動的に例外状態から復帰できるとき
 * ユーザがもう1度同じ手順を実行すればうまくいくかもしれないとき
 * 運用管理者の手を煩わせることはないとき
 * パトランプがまわらないとき

例えば、排他エラーや検索条件によって検索結果が見つからない場合などのように再実行すれば、
うまく実行できる可能性があるときは回復可能なアプリケーション例外を利用します。

### 回復不可能なアプリケーション例外

回復不可能なアプリケーション例外を利用するときの指針は以下の通りです。

 * アプリケーションが自動的に例外状態から復帰できないとき
 * ユーザがもう1度同じ手順を実行したとしてもうまくいくことがないとき
 * 運用管理者が手動で例外状態から復帰させなければいけないとき
 * パトランプがまわるとき

例えば、処理に必要なマスタデータが参照できなくなっている場合などのように再実行しても、
うまくいくことはないときは回復不可能なアプリケーション例外を利用します。

### システム例外

システム例外を利用するときの指針は以下の通りです。

 * システムが自動的に例外状態から復帰できないとき
 * 運用管理者が手動で例外状態から復帰させなければいけないとき
 * パトランプがまわるとき

例えば、データベースサーバへ接続ができなくなっている場合などのように再実行しても、
システムが回復しないときはシステム例外を利用します。

[ApplicationRecoverableException]: {{ site.baseurl }}docs/projects/jfw-exception-core/{{ site.jfw-exception-core.version }}/api/jp/co/ctc_g/jfw/core/exception/ApplicationRecoverableException.html
[ApplicationUnrecoverableException]: {{ site.baseurl }}docs/projects/jfw-exception-core/{{ site.jfw-exception-core.version }}/api/jp/co/ctc_g/jfw/core/exception/ApplicationUnrecoverableException.html
[SystemException]: {{ site.baseurl }}docs/projects/jfw-exception-core/{{ site.jfw-exception-core.version }}/api/jp/co/ctc_g/jfw/core/exception/SystemException.html