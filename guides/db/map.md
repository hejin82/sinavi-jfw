---
layout: page
category : manual
tags : [データベースアクセス]
title: "検索結果をMapで取得したいけど、どうすればいい？"
---

検索結果をMapとして取得する方法は単純です。  
Mapper XML に定義する SELECT ステートメントの定義である select 要素の resultType 属性に hashmap を指定するのみです。  

```
<select id="selectUsers" parameterType="int" resultType="hashmap">
    SELECT
        ID,
        NAME,
        ...
    FROM SAMPLE
    WHERE ID = #{ID}
</select>
```

SQL Query の SELECT文で指定されたカラム名、または、エイリアスが Map のキーに、各カラムに対応する検索結果がバリューマッピングされます。  

SQL Query を実行した結果は当然 Map として返却されますが、型パラメータをどう指定するか示すため
Dao実装クラスの定義を見てみます。  

```
@Override
public Map<String, Object> findById(Integer id) {
  return getSqlSession().selectOne("ステートメントの指定", id);
}
```

上記の通りキーには java.lang.String を指定します。  SQL Query の SELECT文で指定されたカラム名、あるいは、エイリアスは java.lang.String型としてMapのキーにマップされます。  MyBatis は内部に保有するデータベースの型とJavaオブジェクトの型のマッピングに従って適宜型を決定するためバリューには java.lang.Object を指定します。  

では、SELECT 文を実行した結果、複数の行が返却された場合はどうなるか以下に示します。  

```
@Override
public List<Map<String, Object>> list() {
  return getSqlSession().selectList("ステートメントの指定");
}
```

複数の行が返却された場合は Map 型の List が返却されます。  


