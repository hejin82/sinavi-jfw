---
layout: page
category : manual
tags : [パンくずナビ]
title: "デフォルト表示を変更する方法はある？"
---

SINAVI J-Frameworkのパンくずにおいてデフォルト表示を変更する方法は次の2種類があります。

 - パンくず表示全体をカスタマイズする方法
 - 個別のパンくず表示のみをカスタマイズする方法

### パンくず表示全体をカスタマイズする方法

SINAVI J-Frameworkではパンくず表示時に以下のキーで設定されている値をテンプレートとして利用しています。  
これはJSP時のBODY要素指定と同じ意味になります。また、テンプレート内の${url}や${query}、${label}は、それぞれ画面IDオブジェクトの対応するプロパティ値で置換されます。  

<script src="https://gist.github.com/t-oi/8394d8c7db58e8b35811.js"></script>

このデフォルト設定されているテンプレートを変更することによって、パンくずの表示をカスタマイズすることができます。  
カスタマイズする場合はファイル名がFrameworkResources.propertiesのプロパティファイルに  
キー(jp.co.ctc_g.jfw.vid.PankuzuTag.pankuzu_template)で値をオーバーライドすれば変更できます。  
例えば、span要素からli要素に変更し、表示したいものであれば、  

<script src="https://gist.github.com/t-oi/5b8eff10a8f82b9ad8cd.js"></script>

というように設定することで実現できます。  

※このタグライブラリの詳細は [PankuzuTag][PankuzuTag] のJavadocを参照してください。  

### 個別のパンくず表示のみをカスタマイズする方法

上記はパンくず表示全体に影響するカスタマイズ方法ですが、ある特定の画面のみパンくず表示を変更したい場合は  
パンくずのタグライブラリのJSP-BODY要素を編集して下さい。例えば、アンカーを非表示にしたいのであれば、  

<script src="https://gist.github.com/t-oi/4cff467854d33af97761.js"></script>

というように設定することで実現できます。  

[PankuzuTag]:{{ site.baseurl }}docs/projects/jfw-web-core/{{ site.jfw-web-core.version }}/api/jp/co/ctc_g/jse/vid/PankuzuTag.html