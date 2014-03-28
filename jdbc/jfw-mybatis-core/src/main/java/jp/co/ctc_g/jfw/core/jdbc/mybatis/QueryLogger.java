/*
 * Copyright (c) 2013 ITOCHU Techno-Solutions Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.ctc_g.jfw.core.jdbc.mybatis;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * このクラスはMyBatisにより発行されるSQLのロギング処理を実行します。
 * SQLロギング処理の責務は、MyBatisのSqlMapファイルに定義されたSQLを極力実行可能な形式で出力する、
 * 特にプリペアードクエリのようなプレースホルダを含むSQLについてはプレースホルダをパラメータ値で置換することとなります。
 * </p>
 * <h3>利用方法</h3>
 * <p>
 * MyBatis のSQL実行プロセスへの介入は、MyBatis のプラグイン機構を用いて実現されます。
 * プラグイン機構を利用してSQLロギング処理を有効にするには、SpringのBean定義ファイルに次のように設定します。
 * </p>
 * <pre>
 * ・・・
 * &lt;bean id=&quot;sqlSessionFactory&quot; class=&quot;org.mybatis.spring.SqlSessionFactoryBean&quot;&gt;
 *     &lt;property name=&quot;dataSource&quot; ref=&quot;dataSource&quot; /&gt;
 *     &lt;property name=&quot;typeAliasesPackage&quot; value=&quot;org.mybatis.jpetstore.domain&quot; /&gt;
 *     &lt;property name=&quot;plugins&quot; &gt;
 *         &lt;list&gt;
 *             &lt;ref bean=&quot;queryLoggingInterceptor&quot; /&gt;
 *         &lt;/list&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * ...
 * &lt;bean id=&quot;"queryLoggingInterceptor"&quot;
 *     class=&quot;jp.co.ctc_g.jfw.jdbc.mybatis.QueryLoggingInterceptor&quot; /&gt;
 * </pre>
 * <h3>ログ出力</h3>
 * <p>
 * 実際にSQLをログ出力する実装は、{@link LoggingStrategy}の具象クラスによって提供されます。
 * デフォルトでは、{@link DefaultLoggingStrategy}が使用され、J-Frameworkの標準的なロギング機構である
 * Commons Logging　と　Log4J のAPIを通してログ出力を行います。従って、ログ出力を正常に動作させるには log4j.xml
 * を適切に設定する必要があります。 設定に必要な情報は、{@link DefaultLoggingStrategy} のAPIマニュアルを参照して下さい。<br/>
 *
 * ログ出力処理をカスタマイズするために、{@link LoggingStrategy}の具象クラスを独自に実装することが可能です。
 * カスタマイズの詳細については{@link LoggingStrategy}のAPIマニュアルを参照して下さい。
 * </p>
 * <p>
 * ログ出力されるSQLはデフォルトの状態（改行コードや空行は除去されます。
 * この振る舞いを変えるには下記の通り設定を行う必要があります。
 * <pre>
 * ...
 * &lt;bean id=&quot;"queryLoggingInterceptor"&quot;
 *     class=&quot;jp.co.ctc_g.jfw.jdbc.mybatis.QueryLoggingInterceptor&quot; &gt;
 *     &lt;property name=&quot;normalize&quot; value=&quot;false&quot; /&gt;
 * &lt;/bean&gt;
 * </pre>
 * <h3>プレースホルダの置換</h3>
 * <p>
 * プリペアードクエリ、コーラブルクエリのプレースホルダは、SQLロギング機構によって自動的に置換されます。<br>
 * デフォルトでは、パラメータのJavaの型ごとに、以下のルールに基づき置換処理が行われます。
 * </p>
 * <p>
 * <table border="1">
 *   <thead>
 *     <tr>
 *       <th>Javaデータ型</th>
 *       <th>SQLリテラルへの変換ルール</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>{@code String}</td>
 *       <td>{@code toString()}の結果をシングルクォートによりエンクローズ</td>
 *     </tr>
 *     <tr>
 *       <td>{@code Date}</td>
 *       <td>次のパターン文字列{@code yyyy-MM-dd}を用いて{@code String}に変換後、シングルクォートによりエンクローズ</td>
 *     </tr>
 *     <tr>
 *       <td>{@code java.sql.Date}</td>
 *       <td>{@code toString()}呼出しによりJDBC日付エスケープ形式{@code yyyy-mm-dd}に変換後、シングルクォートによりエンクローズ</td>
 *     </tr>
 *     <tr>
 *       <td>{@code java.sql.Time}</td>
 *       <td>{@code toString()}呼出しによりJDBC時間エスケープ形式{@code hh:mm:ss}に変換後、シングルクォートによりエンクローズ</td>
 *     </tr>
 *     <tr>
 *       <td>{@code java.sql.Timestamp}</td>
 *       <td>{@code toString()}呼出しによりJDBC タイムスタンプエスケープ形式{@code yyyy-mm-dd hh:mm:ss.fffffffff}に変換後、シングルクォートによりエンクローズ</td>
 *     </tr>
 *     <tr>
 *       <td>上記以外のクラス</td>
 *       <td>{@code toString()}呼び出しにより{@code String}に変換</td>
 *     </tr>
 *   </tbody>
 * </table>
 * </p>
 * <p>
 * なお、リテラルへの変換ルールを追加する場合は、対象のJavaのデータ型ごとに{@link LiteralConvertor}の具象クラスを定義し
 * 以下の通り設定する必要があります。
 * </p>
 * <pre>
 * ・・・
 * &lt;bean id=&quot;sqlSessionFactory&quot; class=&quot;org.mybatis.spring.SqlSessionFactoryBean&quot;&gt;
 *     &lt;property name=&quot;dataSource&quot; ref=&quot;dataSource&quot; /&gt;
 *     &lt;property name=&quot;typeAliasesPackage&quot; value=&quot;org.mybatis.jpetstore.domain&quot; /&gt;
 *     &lt;property name=&quot;plugins&quot; &gt;
 *         &lt;list&gt;
 *             &lt;ref bean=&quot;queryLoggingInterceptor&quot; /&gt;
 *         &lt;/list&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * ...
 * &lt;bean id=&quot;queryLoggingInterceptor&quot;
 *     class=&quot;jp.co.ctc_g.jfw.jdbc.mybatis.QueryLoggingInterceptor&quot; &gt;
 *   &lt;property name=&quot;properties&quot;>
 *     &lt;props&gt;
 *       &lt;prop key=&quot;CONVERTER_CLASS&quot;&gt;
 *        jp.co.ctc_g.jfw.core.jdbc.mybatis.BooleanTestLiteralConvertor,
 *        jp.co.ctc_g.jfw.core.jdbc.mybatis.BigDecimalTestLiteralConvertor
 *       &lt;/prop&gt;
 *     &lt;/props&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 */
public class QueryLogger implements InitializingBean {

    private LoggingStrategy loggingStrategy = new DefaultLoggingStrategy();

    private QueryBuilderFactory queryBuilderFactory;

    /**
     * デフォルトコンストラクタです。
     */
    public QueryLogger() {}

    /**
     * コンストラクタです。
     * @param normalize 正規化するかどうか
     */
    public QueryLogger(boolean normalize) {
        this.queryBuilderFactory = new QueryBuilderFactory(normalize);
    }

    /**
     * SQLのログを出力します。
     * @param queryLoggingSource クエリー情報にアクセスするためのAPI
     */
    public void log(QueryInformation queryLoggingSource) {
        QueryBuilder builder = queryBuilderFactory.getBuilder(queryLoggingSource.getStatementType());
        String sql = builder.build(queryLoggingSource);
        this.loggingStrategy.log(sql);
    }

    /**
     * SQLのログ出力処理を設定します。
     * @param loggingStrategy SQLのログ出力処理
     */
    public void setLoggingStrategy(LoggingStrategy loggingStrategy) {
        this.loggingStrategy = loggingStrategy;
    }

    /**
     * リテラルコンバータを設定します。
     * @param convertors コンバータ
     */
    @SuppressWarnings("rawtypes")
    public void setLiteralConvertor(List<LiteralConvertor> convertors) {
        LiteralConvertorRegistory literalConvertorRegistory = LiteralConvertorRegistory.getInstance();
        literalConvertorRegistory.regist(convertors);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (loggingStrategy == null)
            this.loggingStrategy = new DefaultLoggingStrategy();
    }
}
