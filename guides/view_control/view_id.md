---
layout: page
category : manual
tags : [画面ID管理/画面フロー制御]
title: "どうすれば画面IDを管理できる？"
---

SINAVI J-Frameworkでは通常の利用においては、画面IDはJSPで定義されると想定しています。  

現在処理通のJSPが生成する画面にIDを付加する場合、以下のようにします。  

<script src="https://gist.github.com/tetsuya-oikawa/41f85b1d82e9aadf9e55.js"></script>

ただし、モーダルダイアログを利用するなどして、サーバに通知することなく画面が遷移する場合、
JSPにて付加する方法ではユーザに表示されている画面IDとサーバが管理している画面IDが一致しなくなる可能性があります。  
そのような場合は、明示的に画面IDの追加や削除をする必要があります。  

<script src="https://gist.github.com/tetsuya-oikawa/853033dc1ca0a5c49d92.js"></script>
 
このようにすると、現在表示されている画面のIDを設定できます。  


なお、この画面IDはセッション内のスタックで管理されています。ViewId#is(ViewId, HttpServletRequest)がコールされるたびに、  
指定された画面IDオブジェクトがスタックの上位に配置されます。  
ただし、指定された画面IDオブジェクトが既にスタックの中に存在していた場合、  
当該画面IDまでにスタックに積まれた画面IDオブジェクトを破棄します。  

このときデフォルトでは既存画面IDを新しい画面IDでは上書きしませんので、ご注意ください。  
もし、上書きが必要であれば、クラスコンフィグオーバライドを利用してください。  