---
layout: index
title: SINAVI J-Framework
tagline: 
---
{% include JB/setup %}

<div class="content" style="padding:10px 0px">
  <div class="row">
    <div class="col-xs-12">
      <p>
       SINAVI J-Frameworkは、Java EE に対応したWebアプリケーションフレームワークで、
       高い汎用性が要求されるスクラッチ開発向けのCTC標準のフレームワークです。
       これまでCTCの開発現場で発生した問題に対する解決策やシステム基盤に必要な共通機能がフィードバックされているので、
       技術リスクを低減し、高い信頼性を実現しています。
      </p>
    </div>
  </div>
</div>

<div class="content" style="padding:10px 0px">
  <div class="row">
    <div class="col-md-6">
      <h3>
        <i class="fa fa-rocket">&nbsp;</i>お知らせ
      </h3>
      <ul>
        {% for post in site.posts %}
          <li><span>{{ post.date | date_to_string }}</span>&nbsp;<span><a href="{{ site.baseurl}}{{ post.url }}">{{ post.title }}</a></span></li>
        {% endfor %}
      </ul>
    </div>
    <div class="col-md-6">
      <h3>
        <i class="fa fa-coffee">&nbsp;</i>構成
      </h3>
      <p>
      SINAVI J-Frameworkは構成技術リスクの低減や品質の確保を目的とし、
      業界でデファクト・スタンダードとして広く利用されている以下のオープンソースをベースに構成しています。
      </p>
        <ul>
          <li>Spring Framework(DI×AOP)</li>
          <li>Spring MVC</li>
          <li>Jersey</li>
          <li>MyBatis</li>
          <li>Spring AMQP RabbitMQ</li>
          <li>Spring Data MongoDB</li>
        </ul>
      <p>再利用性の高い基盤機能やユーティリティを独自拡張することで、生産性と品質の更なる向上を目指しています。</p>
    </div>
  </div>
</div>
<hr>
<div class="content" style="padding:10px 0px">
  <div class="row">
    <div class="col-lg-4 col-sm-6">
      <h3>
        <i class="fa fa-github">&nbsp;</i>Github
      </h3>
      <p>ソースコードは<a href="{{ site.github_project_pages }}">GitHub</a>で管理しています。</p>
    </div>
    <div class="col-lg-4 col-sm-6">
      <h3>
        <i class="fa fa-building-o">&nbsp;</i>Bamboo
      </h3>
      <p>ビルド、単体テスト実行には<a href="{{ site.bamboo_pages }}">Bamboo</a>を利用しています。※現在、利用申請中です。</p>
    </div>
    <div class="col-lg-4 col-sm-6">
      <h3>
        <i class="fa fa-bug">&nbsp;</i>JIRA
      </h3>
      <p>課題管理には<a href="{{ site.jira_pages }}">JIRA</a>を利用しています。※現在、利用申請中です。</p>
    </div>
  </div>
  <div class="row">
    <div class="col-lg-4 col-sm-6">
      <h3>
        <i class="fa fa-comment-o">&nbsp;</i>Community
      </h3>
      <p>Communityには<a href="{{ site.confluence_pages }}">Confluence</a>を利用しています。※現在、利用申請中です。</p>
    </div>
    <div class="col-lg-4 col-sm-6">
      <h3>
        <i class="fa fa-envelope-o">&nbsp;</i>Contacts
      </h3>
      <p>SINAVI J-Frameworkに関するお問い合わせは、{{ site.author.email }} までお願いします。</p>
    </div>
    <div class="col-lg-4 col-sm-6">
      <h3>
        <i class="fa fa-code">&nbsp;</i>OpenSource
      </h3>
      <p>SINAVI J-Framework is released under version 2.0 of the <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache License</a></p>
    </div>
  </div>
</div>
