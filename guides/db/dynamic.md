---
layout: page
category : manual
tags : [データベースアクセス]
title: "特定の条件に従って動的にSQL Queryを構築したいけど、どうすればいい？"
---

特定の条件に従って動的にSQLを構築しするには、MyBatis の動的SQLを利用します。  
MyBatis における動的なSQL Queryの構成は、制御構造を実現するための下記の要素を利用します。  

  * if
  * choose (when, otherwise)

各要素の利用方法を具体的に説明します。

### if 要素 の利用方法

if 要素の利用は非常に単純で、test 属性に評価式を実装するだけで、評価式の真偽に従って if 要素で囲まれる式の評価の要否が決定されます。  

例えば、パラメータが null でない場合のみ条件式を付加するケースを考えてみましょう。  
下記の例ではcondition パラメータが null でない場合のみ、SELECT文に AND condition like ? といった条件式が付加されます。  

```
SELECT * FROM SAMPLE
WHERE flag = '1'
<if test="condition != null">
    AND condition like #{condition}
</if>
```

別の条件のケースについても見てみましょう。パラメータが特定の文字列かどうかで動的にSQL Queryを構築するケースです。  
下記の例ではcondition パラメータが 'VALID' というも文字列の場合のみ、SELECT文に AND condition like ? といった条件式が付加されます。  

```
SELECT * FROM SAMPLE
WHERE flag = '1'
<if test="condition == 'VALID'">
    AND condition like #{likeKey}
</if>
```

if 要素の利用法は以上になります。

### choose 要素の利用方法

if 要素は単純でパワフルです。if 要素を上手く利用することで大半の動的 SQL Queryを構築することが可能です。  
ただ、if 要素のみを利用した動的 SQL Queryの構築は MapperXML の可読性を著しく低下させるケースがあります。  
ここで説明する choose 要素をうまく利用することにより MapperXML の可読性を低下させることなく動的 SQL Queryの構築を行うことができます。  
choose 要素は、多くの選択肢の中から一つを選んで適用したいというケースで利用する要素です。  

具体的な例を見てみましょう。
下記の例は、name パラメータが null でなければ AND name like ? を付加し、 name パラメータが null かつ address パラメータが null でなければ AND adress like ? を付加、どちらにも該当しなければ AND tel like '03%' を条件に付加します。  

```
SELECT *
FROM SAMPLE
WHERE flag = '1'
<choose>
    <when test="name != null">
        AND name like #{name}
    </when>
    <when test="address != null">
        AND adress like #{address}
    </when>
    <otherwise>
        AND tel like '03%'
    </otherwise>
</choose>
```

choose 要素の利用法は以上になります。
