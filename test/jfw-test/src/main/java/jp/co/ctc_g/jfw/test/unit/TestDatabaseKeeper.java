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

package jp.co.ctc_g.jfw.test.unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Arrays;
import jp.co.ctc_g.jfw.core.util.GenCall;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Primitives;
import jp.co.ctc_g.jfw.core.util.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * <p>
 * このクラスは、ユニットテストの際のRDBMSの状態を管理します。
 * </p>
 * <p>
 * J-Frameworkが提供するユニットテストのためのRDBMS管理機能についての詳細は、
 * {@link DatabaseInitialize}を参照してください。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see DatabaseInitialize
 * @see J2Unit4ClassRunner
 */
public class TestDatabaseKeeper extends AbstractTestExecutionListener {

    private static final Logger L = LoggerFactory.getLogger(TestDatabaseKeeper.class);

    private static final ResourceBundle R = InternalMessages.getBundle(TestDatabaseKeeper.class);
    
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * デフォルトコンストラクタです。
     */
    public TestDatabaseKeeper() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeTestMethod(TestContext context) throws Exception {
        if (context.getTestMethod().isAnnotationPresent(DatabaseInitialize.class)) {
            DatabaseInitialize config = context.getTestMethod().getAnnotation(DatabaseInitialize.class);
            initializeDatabase(context, config);
        }
    }

    /**
     * データベースを初期化します。
     * @param context 現在のテスト実行コンテキスト
     * @param config コンフィグ
     */
    protected void initializeDatabase(TestContext context, DatabaseInitialize config) {
        String[] candidates = createFileLocationCandidates(context, config);
        DataSource source = findDataSource(context, config);
        String[] resolved = resolveDialect(candidates, context, config, source);
        String content = readSqlFile(resolved, context, config);
        String[] executables = createExecutables(content);
        execute(executables, source, context);
    }

    /**
     * SQLファイルの検索場所候補を作成します。
     * この候補は、同一パッケージ内の、
     * <ol>
     *  <li>クラス名-メソッド名.sql</li>
     *  <li>クラス名.sql</li>
     *  <li>DatabaseInitialize.sql</li>
     * </ol>
     * です。
     * @param context 現在のテスト実行コンテキスト
     * @param config コンフィグ
     * @return SQLファイルの検索場所候補の配列
     */
    protected String[] createFileLocationCandidates(TestContext context, DatabaseInitialize config) {
        if (config.file().equals("")) {
            // クラス名-メソッド名 ex)/jp/co/ctc_g/jfw/sample/Sample-method
            String clazzMethod = Strings.join(
                    "/",
                    context.getTestClass().getName().replace(".", "/"),
                    "-",
                    context.getTestMethod().getName());
            // クラス名 ex)/jp/co/ctc_g/jfw/sample/Sample
            String clazz = Strings.join(
                    "/",
                    context.getTestClass().getName().replace(".", "/"));
            // パッケージ ex)/jp/co/ctc_g/jfw/sample/DatabaseInitialize
            String pakkage = Strings.join(
                    "/",
                    context.getTestClass().getPackage().getName().replace(".", "/"),
                    "/",
                    DatabaseInitialize.class.getSimpleName());
            return Arrays.gen(clazzMethod, clazz, pakkage);
        } else {
            return Arrays.gen(config.file());
        }
    }

    /**
     * SQLファイル検索場所候補にさらにRDBMS方言別の検索候補を追加します。
     * @param candidates SQLファイルの検索場所候補の配列
     * @param context 現在のテスト実行コンテキスト
     * @param config コンフィグ
     * @param source SQL実行対象のデータソース
     * @return RDBMS方言別の検索候補を含む、SQLファイル検索場所候補
     */
    protected String[] resolveDialect(
            final String[] candidates,
            TestContext context,
            DatabaseInitialize config,
            DataSource source) {
        final DialectType dialect = DialectType.detect(source);
        if (dialect != null) {
            return Arrays.gen(candidates.length * 2, new GenCall<String>() {
                public String gen(int index, int total) {
                    if (index % 2 == 0) {
                        return Strings.join(candidates[index / 2], "_", dialect.name().toLowerCase());
                    } else {
                        return candidates[index / 2];
                    }
                }
            });
        } else {
            return candidates;
        }
    }

    /**
     * データソースを探します。
     * もし、{@link DatabaseInitialize#dataSourceRef()}にてデータソースのビーンIDが指定されていた場合、
     * そのデータソースを利用します。
     * 指定されたビーンIDがコンテナ内に存在しない場合、エラーID「E-TEST#0009」で終了します。
     * 一方、ビーンIDが指定されていなかった場合は、
     * {@link DataSource}型に適合可能なインスタンスをDIコンテナ内から探します。
     * 現在のコンテナに見つからない場合、全ての親コンテナを対象に含めて検索します。
     * それでも見つからない場合、エラーID「E-TEST#0010」を発生させて終了します。
     * @param context 現在のテスト実行コンテキスト
     * @param config コンフィグ
     * @return 検出されたデータソース
     * @throws InternalException 指定されたビーンIDがコンテナ内に存在しない場合（E-TEST#0009）
     * @throws InternalException {@link DataSource}型に適合可能なインスタンスがDIコンテナ内にない場合（E-TEST#0010）
     */
    protected DataSource findDataSource(TestContext context, DatabaseInitialize config) {
        String id = config.dataSourceRef();
        if ("".equals(id)) {
            // コンテナ内のデータソースオブジェクトを探索
            String[] ids = context.getApplicationContext().getBeanNamesForType(DataSource.class);
            if (ids != null && ids.length > 0) {
                id = ids[0];
            } else {
                String[] incuded = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                        context.getApplicationContext(), DataSource.class);
                if (incuded != null && incuded.length > 0) {
                    id = incuded[0];
                }
            }
            if ("".equals(id)) {
                throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0010");
            }
        }
        DataSource source = null;
        try {
            source = (DataSource) context.getApplicationContext().getBean(id);
        } catch (NoSuchBeanDefinitionException e) {
            Map<String, String> replace = Maps.hash("id", id);
            throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0009", replace);
        }
        return source;
    }

    /**
     * 指定されたSQLファイル候補の中から適切なSQLファイルを特定し、
     * そのファイルのコンテンツを読みだします。
     * @param resolved RDBMS方言別の検索候補を含む、SQLファイル検索場所候補
     * @param context 現在のテスト実行コンテキスト
     * @param config コンフィグ
     * @return 読みだしたSQL
     * @throws InternalException 想定外の文字コードの場合（E-TEST#0011）
     * @throws InternalException ファイル読み込み時に入出力例外が発生した場合（E-TEST#0008）
     */
    protected String readSqlFile(
            String[] resolved,
            TestContext context,
            DatabaseInitialize config) {
        String content = "";
        InputStream is = createInputStream(resolved);
        InputStreamReader isr = null;
        BufferedReader br = null;
        InternalException exception = null;
        try {
            isr = new InputStreamReader(is, config.charset());
            br = new BufferedReader(isr);
            StringBuilder sql = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sql.append(line).append(LINE_SEPARATOR);
            }
            content = sql.toString();
        } catch (UnsupportedEncodingException e) {
            Map<String, String> replace = Maps.hash("charset", config.charset());
            exception = new InternalException(TestDatabaseKeeper.class, "E-TEST#0011", replace, e);
        } catch (IOException e) {
            exception = new InternalException(TestDatabaseKeeper.class, "E-TEST#0008", e);
        } finally {
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (is != null) is.close();
            } catch (IOException e) {
                if (exception == null) {
                    exception = new InternalException(TestDatabaseKeeper.class, "E-TEST#0008", e);
                }
            }
            if (exception != null) throw exception;
        }
        return content;
    }

    /**
     * 指定されたSQLファイル候補の中から、有効なファイルを特定してその入力ストリームを返却します。
     * @param resolved RDBMS方言別の検索候補を含む、SQLファイル検索場所候補
     * @return 有効なファイルの入力ストリーム
     * @throws InternalException ファイルが見つからなかった場合（E-TEST#0007）
     */
    protected InputStream createInputStream(String[] resolved) {
        InputStream file = null;
        for (String location : resolved) {
            String physical = location + ".sql";
            InputStream is = getClass().getResourceAsStream(physical);
            if (is != null) {
                file = is;
                if (L.isInfoEnabled()) {
                    Map<String, String> replace = Maps.hash("file", physical);
                    L.info(Strings.substitute(R.getString("I-TEST#0001"), replace));
                }
                break;
            }
        }
        if (file == null) {
            Map<String, String> replace = Maps.hash("searchPath", Strings.joinBy(", ", resolved));
            throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0007", replace);
        }
        return file;
    }

    /**
     * SQLファイルのコンテンツから、SQL文の配列を抽出します。
     * この抽出には構文解析を利用せず、単純に ;（セミコロン）で分割しています。
     * よって、コメント内のセミコロンも分割対象になりますので、ご注意ください。
     * @param content SQLファイルのコンテンツ
     * @return SQL文の配列
     */
    protected String[] createExecutables(String content) {
        String[] fragments = Strings.split(";", content);
        List<String> executables = new ArrayList<String>(fragments.length);
        for (String fragment : fragments) {
            String f = fragment.trim();
            if (!"".equals(f)) {
                executables.add(f);
                if (L.isDebugEnabled()) {
                    Map<String, String> replace = Maps.hash("sql", f);
                    L.debug(Strings.substitute(R.getString("D-TEST#0001"), replace));
                }
            }
        }
        return executables.toArray(new String[0]);
    }

    /**
     * 指定されたSQL文を指定されたデータソースに対して実行します。
     * @param executables SQL文
     * @param source データソース
     * @param context 現在のテスト実行コンテキスト
     */
    protected void execute(
            String[] executables,
            DataSource source,
            TestContext context) {
        Connection connection = null;
        try {
            connection = connect(source);
            Statement statement = createStatement(connection);
            for (String executable : executables) {
                addBatch(statement, executable);
            }
            executeInternal(connection, statement, executables);
        } finally {
            if (connection != null) disconnect(connection);
        }
    }

    private Connection connect(DataSource source) {
        Connection connection = null;
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0012", e);
        }
        if (connection == null) {
            throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0012");
        }
        return connection;
    }

    private void disconnect(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0004", e);
        }
    }

    private Statement createStatement(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0002", e);
        }
        if (statement == null) {
            throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0002");
        }
        return statement;
    }

    private void addBatch(Statement statement, String executable) {
        try {
            statement.addBatch(executable);
        } catch (SQLException e) {
            throw new InternalException(
                    TestDatabaseKeeper.class,
                    "E-TEST#0001",
                    Maps.hash("executable", executable),
                    e);
        }
    }

    private void executeInternal(
            Connection connection,
            Statement statement,
            String[] executables) {
        InternalException exception = null;
        try {
            connection.setAutoCommit(false);
            int[] affected = statement.executeBatch();
            if (Arrays.contain(Primitives.wrap(affected), Statement.EXECUTE_FAILED) != -1) {
                String[] fails = findBatchUpdateFailureSql(affected, executables);
                Map<String, String> replace = Maps
                .hash("fails", Strings.joinBy(" ", fails))
                .map("count", String.valueOf(fails.length));
                exception = new InternalException(TestDatabaseKeeper.class, "E-TEST#0005", replace);
            }
            if (L.isInfoEnabled()) L.info(R.getString("I-TEST#0002"));
        } catch (BatchUpdateException e) {
            int[] successes = e.getUpdateCounts();
            String[] fails = findBatchUpdateFailureSql(successes, executables);
            Map<String, String> replace = Maps
                    .hash("fails", Strings.joinBy(" ", fails))
                    .map("count", String.valueOf(fails.length));
            exception = new InternalException(TestDatabaseKeeper.class, "E-TEST#0005", replace, e);
        } catch (SQLException e) {
            exception = new InternalException(TestDatabaseKeeper.class, "E-TEST#0005", e);
        } finally {
            try {
                if (exception != null) {
                    connection.rollback();
                    throw exception;
                } else {
                    connection.commit();
                }
            } catch (SQLException e) {
                if (exception != null) {
                    throw exception;
                } else {
                    throw new InternalException(TestDatabaseKeeper.class, "E-TEST#0003", e);
                }
            }
        }
    }

    private String[] findBatchUpdateFailureSql(int[] statuses, String[] executables) {
        List<String> fails = new LinkedList<String>();
        int total = 0;
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i] == Statement.EXECUTE_FAILED) {
                fails.add(Strings.join("[", String.valueOf(++total), "]", executables[i]));
            }
        }
        return fails.toArray(new String[0]);
    }
}
