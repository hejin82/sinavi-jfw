---
layout: page
category : manual
tags : [ページング]
title: "どうすればページングが表示できる？"
---

SINAVI J-Frameworkではページングを表示するためのタグライブラリを提供しています。  

ページングとは、検索結果など大量の一覧項目をページ分割して表示する場合、ページを送るためのリンクを表示するユーザインタフェースパターンの種類です。  
これにより、ユーザはスクロールせずに、クリックによる画面遷移によって簡単に情報を取得できるようになります。  
SINAVI J-Frameworkでは独自のPartialListというリストを利用して動的にページングを表示することができます。  

表示は簡単です。  

```
[paging.jsp]
<jse:navi partial="${emps}" action="/list">
</jse:navi>
```

これだけです。  以下のように表示されます。  


{% highlight text %}
  << < 1 2 3 4 5 ... > >>
{% endhighlight %}

partial属性にPartialListを指定することで、そのListの総数や1ページのインデックスなどを表示することができます。  

さらにSINAVI J-FrameworkではMyBatisを一部拡張し、このPartialListを簡単に生成できるようにしています。  

まず、検索条件を保持するドメインクラスはPaginatableSupportを継承して実装する必要があります。  

```
[DomainCriteria.java]
public class DomainCriteria extends PaginatableSupport {
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
```

次に、DaoインタフェースにサフィックスがWithPaginatingとなるメソッドを定義し、引数には検索条件を保持するドメインを指定します。  

```
[DomainDao.java]
public interface DomainDao {
    List<Domain> listWithPaginating(DomainCriteria criteria);
}
```

```
[DomainDaoImpl.java]
public class DomainDaoImpl extends SqlSessionDaoSupport implements DomainDao {
    public List<Domain> listWithPaginating(DomainCriteria criteria) {
        return getSqlSession().selectList("listWithPaginating", criteria);
    }
}
```

検索条件に応じて検索するSQLに加えて、検索結果の件数を取得するSQLも定義する必要があります。  
これはWithPaginatingのサフィックスに::countとなるIDで検索結果の件数を取得するSQLを定義します。  

```
[DomainDaoImpl.xml]
<select i="listWithPaginating::count" parameterType="DomainCriteria" resultType="int"  >
  SELECT count(*)
  FROM EMP
  WHERE NAME LIKE '%' || #{name} || '%'
  ORDER BY ID
</select>

<select id="listWithPaginating" parameterType="DomainCriteria" resultType="Domain" >
  SELECT *
  FROM EMP
  WHERE NAME LIKE '%' || #{name} || '%'
</select>
```

これで検索を実行する前に検索結果の件数を取得し、PartialListに設定されます。  

Controllerクラスで画面から送信されてきたページ番号をもとにページの開始位置や終了位置を計算し、検索を実行します。  
計算するときにはPaginates#set(Paginatable, int)やPaginates#set(Paginatable, int, int)などを利用してください。  

```
[DomainController.java]
@Controller
@RequestMapping("/domain")
public class DomainController {
    @Autowired
    protected DomainService service;

    @ModelAttribute("domainCriteria")
    public DomainCriteria newInstance4Criteria() {
        return new DomainCriteria();
    }

    @RequestMapping("list")
    @PostBack.Action({
        @PostBack.Action(
    // 逵∫払
    })
    public String list(@Validated @ModelAttribute DomainCriteria criteria, Model model) {
        int pageNumber = Integer.valueOf(criteria.getPageNumber()).intValue();
        Paginates.set(criteria, criteria.getPageNumber());
        List<Domain> emps = service.listWithPaginating(criteria);
        model.addAttribute("emps", emps);
        // 逵∫払
        return "jsp/list.jsp";
    }
}
```

最後にJSPにページングを表示します。  

```
[domain.jsp]
<c:if test="${not empty emps}">
 <jse:navi partial="${emps}" action="/domain/list">
 </jse:navi>
 // 一覧表示は省略
</c:if>
```
