---
layout: page
title: Guides
header: SINAVI J-Framework API Documentation
group: navigation
---
{% include JB/setup %}

<div class="page-header">
  <h2>リファレンス</h2>
</div>
<p>本ドキュメントは SINAVI J-Frameworkを利用してやりたいことから、実装方法を調べられるリファレンスです。</p>
<div class="row">
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>入力値検証
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/validation/type.html">どんな入力値検証がある？</a></li>
      <li><a href="{{ site.baseurl}}guides/validation/howto.html">どうすれば入力値検証が実行できる？</a></li>
      <li><a href="{{ site.baseurl}}guides/validation/order.html">入力値検証には実行される順番はある？</a></li>
      <li><a href="{{ site.baseurl}}guides/validation/security.html">セレクトボックスやチェックボックスなどに入力値検証は必要？</a></li>
    </ul>
  </div>
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>データベースアクセス
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/db/nest.html">Summary-Detailのようなデータ構造にSQL Queryの結果セットをマッピングするにはどうすればいい？</a></li>
      <li><a href="{{ site.baseurl}}guides/db/in.html">IN句の指定はどのようにすればいい？</a></li>
      <li><a href="{{ site.baseurl}}guides/db/map.html">Mapで取得したいけど、どうすればいい？</a></li>
      <li><a href="{{ site.baseurl}}guides/db/dynamic.html">特定の条件に従って動的にSQL Queryを構築したいけど、どうすればいい？</a></li>
      <li><a href="{{ site.baseurl}}guides/db/sequential.html">逐次処理するにはどうしたらいい？</a></li>
    </ul>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>ログ出力
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/logging/log.html">ユーザID、セッションIDなどをログに出力したい場合は？</a></li>
    </ul>
  </div>
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>例外処理
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/exception/exception.html">例外はどのように使い分けたらいい？</a></li>
    </ul>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>リソース管理
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/resource/add.html">リソースファイルを追加する場合、どのように設定したらいい？</a></li>
      <li><a href="{{ site.baseurl}}guides/resource/get.html">リソースファイルの値を取得する場合、どのように取得したらいい？</a></li>
      <li><a href="{{ site.baseurl}}guides/resource/reload.html">アプリケーションをロードし直すことなく、リソースファイルの値を更新できる？</a></li>
    </ul>
  </div>
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>自動プロパティ設定
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/auto_bean/bulk.html">複数のデータを更新する場合に自動でプロパティを設定するにはどうしたらいい？</a></li>
      <li><a href="{{ site.baseurl}}guides/auto_bean/userId.html">どこにユーザIDを設定すれば、自動でプロパティに設定できる？</a></li>
    </ul>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>ページング
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/paging/howto.html">どうすればページングが表示できる？</a></li>
    </ul>
  </div>
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>パンくずナビ
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/bread_crumb/howto.html">どうすればパンくずを表示できる？</a></li>
      <li><a href="{{ site.baseurl}}guides/bread_crumb/view.html">デフォルト表示を変更する方法はある？</a></li>
    </ul>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <h4>
      <i class="fa">&nbsp;</i>画面ID管理/画面フロー制御
    </h4>
    <ul>
      <li><a href="{{ site.baseurl}}guides/view_control/view_id.html">どうすれば画面IDを管理できる？</a></li>
      <li><a href="{{ site.baseurl}}guides/view_control/view_constraint.html">どうすれば許可された画面からの遷移だけに制限できる？</a></li>
    </ul>
  </div>
</div>
