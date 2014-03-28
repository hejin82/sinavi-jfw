---
layout: page
category : manual
tags : [データベースアクセス]
title: "検索結果をMapで取得したいけど、どうすればいい？"
---

検索結果をMapとして取得する方法は単純です。  
Mapper XML に定義する SELECT ステートメントの定義である select 要素の resultType 属性に hashmap を指定するのみです。  

<script src="https://gist.github.com/tetsuya-oikawa/8150aacbdd1703ab7507.js"></script>

SQL Query の SELECT文で指定されたカラム名、または、エイリアスが Map のキーに、各カラムに対応する検索結果がバリューマッピングされます。  

SQL Query を実行した結果は当然 Map として返却されますが、型パラメータをどう指定するか示すため
Dao実装クラスの定義を見てみます。  

<script src="https://gist.github.com/tetsuya-oikawa/f2d5fbf5e4b942a18739.js"></script>

上記の通りキーには java.lang.String を指定します。  SQL Query の SELECT文で指定されたカラム名、あるいは、エイリアスは java.lang.String型としてMapのキーにマップされます。  MyBatis は内部に保有するデータベースの型とJavaオブジェクトの型のマッピングに従って適宜型を決定するためバリューには java.lang.Object を指定します。  

では、SELECT 文を実行した結果、複数の行が返却された場合はどうなるか以下に示します。  

<script src="https://gist.github.com/tetsuya-oikawa/2cb46c740ba0589bb89b.js"></script>

複数の行が返却された場合は Map 型の List が返却されます。  


