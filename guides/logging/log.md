---
layout: page
category : manual
tags : [ロギング]
title: "ユーザ、セッションIDなどをログに出力したい場合は？"
---

ログを出力するとき、常にユーザIDやセッションIDといった情報を付加する方法について記載します。  

[SLF4j][SLF4j] の APIであるMDC(Mapped Diagnostic Context)を利用しユーザIDやセッションIDなどをストアすることにより、
ログ出力時にユーザID、セッションIDが付加されたメッセージを生成させることができます。  

まず、SLF4jのMDCにユーザIDやセッションIDなどを登録するServletFilterを用意します。  
例えば、ユーザIDがPrincipalに格納されている場合、

```
[AuthLoggingFilter.java]
public class AuthLoggingFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {};
    
    @Override
    public void destory() {};
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        Principal auth = req.getUserPrincipal();
        String userId = "";
        if (auth != null) {
            userId = auth.getName();
        }
        if (!"".equals(name)) {
            MDC.put("userId", userId);
        } else {
            MDC.put("userId", "未設定");
        }
        HttpSession session = r.getSession(false);
        if (session != null) {
            MDC.put("sessionId", session.getId());
        }
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("loginId");
            MDC.remove("sessionId");
        }
    }
}
```

のようにSLF4jのMDCに登録します。  

次にログフォーマットに従い、LOGBackの設定ファイル(logback.xml)のレイアウトでMDCの値を参照することにより、
MDCに登録した値をログに出力することができます。  
例えば、
{% highlight text %}
日付 [ログレベル] [userId=xxxxxx] [sessionId=xxxxxxx] ログメッセージ
{% endhighlight %}
のようにログを出力する場合、

```
[logback.xml]
<appender name="console" class="ch.qos.logback.core.ConsoleAppender">
  <encoder>
    <Pattern>%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] [userId=%X{userId}] [sessionId=%X{sessionId}] %m %n" />
  </encoder>
</appender>
```

のようにレイアウトを設定することで、出力できます。  

MDCの値を参照する際は、
{% highlight xml %}
[* %X{key} *]
{% endhighlight %}
で参照します。  

これで、ログを出力したときにユーザIDやセッションIDがログに出力されます。  

```
[HogeServiceImpl.java]
@Service
public class HogeServiceImpl implements HogeService {
    private static final Log L = LogFactory.getLog();
    public void findById(String id) {
        L.debug("検索条件は" + id + "です。");
    }
}

```

※出力例
{% highlight text %}
12:00:00.000 [DEBUG] [userId=z123456] [sessionId=xxxxxxxxxxx] 検索条件は1111です。  
{% endhighlight %}


[SLF4j]:http://www.slf4j.org/