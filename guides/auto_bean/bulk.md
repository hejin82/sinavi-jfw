---
layout: page
category : manual
tags : [Beanプロパティ自動設定]
title: "複数のデータを更新する場合に自動でプロパティを設定するにはどうしたらいい？"
---

SINAVI J-Frameworkの自動プロパティ設定機能を利用すると作成日・作成者や更新日・更新者といったカラムに対応するJavaBeansのプロパティに自動で適切な値を設定することができます。  
この自動プロパティ設定機能を有効にするにはSpringの設定ファイル(Root-Context.xml)にトランザクション開始時刻を設定するインターセプタの設定と  
リクエストに関連付けらているjava.security.Principalをスレッドローカルに登録するフィルタの設定を行う必要があります。  

```
[AOPContext.xml]
<aop:config>
  <aop:pointcut id="servicePointcut" expression="execution(* ..*Service.*(..))" >
  <aop:advisor pointcut-ref="servicePointcut" advice-ref="transactionAdvice" >
  <aop:advisor pointcut-ref="servicePointcut" advice-ref="transactionTimeAdvice" >
</aop:config>

<tx:advice id="transactionAdvice" transaction-manager="transactionManager>
  ...
</tx:advice>

<bean id="transactionTimeAdvice" class="jp.co.ctc_g.jfw.profill.util.TransactionTimeInterceptor" >
```

これで自動プロパティ設定機能を利用することができます。  
次にJavaBeansに値を設定するためには対象のJavaBeansのプロパティのアクセサメソッド(setterメソッド)に  
JavaBeansの対象プロパティのsetterメソッドに自動プロパティ設定機能のアノテーションを付与することで  
値が自動で設定されます。

```
[Emp.java]
public class Emp implements {
    private String empno;
    private String empName;
    private Date hireDate;
    private Date createStamp;
    private String createUserId;
    private Date updateStamp;
    private String updateUserId;
    // 一部アクセサメソッド省略

    @UpdateStamp
    public void setCreateStamp(Date createStamp) {
        this.createStamp = createStamp;
    }
    public Date getCreateStamp() { return createStamp; }

    @UpdateUser
    public String setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
```

自動プロパティ設定機能の使い方は簡単なのですが、ここで注意事項があります。  

自動プロパティ設定機能はパフォーマンスに配慮しているため、複数のデータに対しては設定しません。  
例えば、社員情報のリストや配列でデータを登録・更新する場合には  


```
[EmpService.java]
public interface EmpService {
    void create(List<Emp> emps);
}
```

のように定義することがありますが、このときは自動プロパティ設定機能は何も設定しません。  
必ず単一のJavaBeansである必要があります。  
それではServiceのインターフェースを単一にし、Controllerから複数回呼び出せばよいかというと  
今度はトランザクション境界の問題があり、単一にすることはできません。  
ではどうするかというとDaoを呼び出されるタイミングで実行するようにインターセプタを設定することで解決することができます。  

```
[AOPContext.xml]
<aop:config>
  <aop:pointcut id="servicePointcut" expression="execution(* ..*Service.*(..))" >
  <aop:pointcut id="daoPointcut" expression="execution(* ..*Dao.*(..))" >
  <aop:advisor pointcut-ref="servicePointcut" advice-ref="transactionAdvice" >
  <aop:advisor pointcut-ref="servicePointcut" advice-ref="transactionTimeAdvice" >
  <aop:advisor pointcut-ref="daoPointcut" advice-ref="transactionTimeAdvice" >
</aop:config>
```

