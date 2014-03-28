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

package jp.co.ctc_g.jse.core.csv.mybatis;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Dates;
import jp.co.ctc_g.jse.core.csv.CSVConfigs;
import jp.co.ctc_g.jse.core.csv.CSVConfigs.CSVConfig;
import jp.co.ctc_g.jse.core.csv.DownloadFile;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(Enclosed.class)
public class AbstractCSVResultHandlerImplTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = "classpath:/jp/co/ctc_g/jse/core/csv/mybatis/TestContext.xml")
    public static class SuccessTest {

        private SqlSession session;
        public AtomicInteger count;
        
        @Rule
        public TemporaryFolder folder = new TemporaryFolder();
        
        public CSVConfig config;

        @Before
        public void setup() throws IOException {
            String resource = "jp/co/ctc_g/jse/core/csv/mybatis/Mybatis.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            count = new AtomicInteger(0);
            config = CSVConfigs.config().tempDir(folder.getRoot().getAbsolutePath());
        }

        @After
        public void destroy() {
            session.close();
        }

        public CSVResultHandler handler = new AbstractCSVResultHandlerImpl() {

            @Override
            public void write(ResultContext context) {
                MobilePhone d = (MobilePhone) context.getResultObject();
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                String[] data = new String[] {
                    d.getTerminalId().toString(), d.getTerminalName(), format.format(d.getSalesDate()),
                    d.getFlashLevel().toString(), d.getVersion().toString()
                };
                csv.write(data);
                count.incrementAndGet();
            }

            @Override
            public String[] getHeader() {
                return new String[] {
                    "端末ID", "端末名", "発売日", "フラッシュ対応バージョン", "バージョン番号"
                };
            }
        };
        
        public CSVResultHandler skipHandler = new AbstractCSVResultHandlerImpl() {
            @Override
            public void write(ResultContext context) {
                count.incrementAndGet();
            }
            @Override
            public String[] getHeader() {
                return null;
            }
        };

        @Test
        public void 検索結果があるときにCSVファイルが出力できる() {
            try {
                handler.open(config);
                handler.header();
                session.select("jp.co.ctc_g.jse.core.csv.mybatis.MobilePhoneDaoImpl.listBy", new MobilePhoneCriteria(), handler);
            } finally {
                handler.close();
            }
            DownloadFile file = handler.get();
            assertThat(file, notNullValue());
            assertThat(file.getTempFileName(), notNullValue());
            assertThat(count.get(), is(75));
        }

        @Test
        public void 検索結果がないときにCSVファイルが出力できる() {
            try {
                handler.open(config);
                handler.header();
                MobilePhoneCriteria criteria = new MobilePhoneCriteria();
                criteria.setFromSalesDate(Dates.makeFrom(9999, 12, 31));
                criteria.setToSalesDate(Dates.makeFrom(9999, 12, 31));
                session.select("jp.co.ctc_g.jse.core.csv.mybatis.MobilePhoneDaoImpl.listBy", criteria, handler);
            } finally {
                handler.close();
            }
            DownloadFile file = handler.get();
            assertThat(file, notNullValue());
            assertThat(file.getTempFileName(), notNullValue());
            assertThat(count.get(), is(0));
        }
        
        @Test
        public void ヘッダの指定がない場合はスキップされる() {
            try {
                skipHandler.open(config);
                skipHandler.header();
                session.select("jp.co.ctc_g.jse.core.csv.mybatis.MobilePhoneDaoImpl.listBy", new MobilePhoneCriteria(), skipHandler);
            } finally {
                skipHandler.close();
            }
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = "classpath:/jp/co/ctc_g/jse/core/csv/mybatis/TestContext.xml")
    public static class ExceptionTest {

        private SqlSession session;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Before
        public void setup() throws IOException {
            String resource = "jp/co/ctc_g/jse/core/csv/mybatis/Mybatis.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
        }

        @After
        public void destroy() {
            session.close();
        }

        public CSVResultHandler handler = new AbstractCSVResultHandlerImpl() {
            @Override
            public void write(ResultContext context) {
            }
            @Override
            public String[] getHeader() {
                return null;
            }
        };

        @Test
        public void CSVファイルをオープンしないでヘッダー出力を実行すると例外が発生する() {
            thrown.expect(InternalException.class);
            thrown.expectMessage(containsString("CSVストリームがオープンされていません。"));
            handler.header();
        }

        @Test
        public void CSVファイルをオープンしないでクローズを実行すると例外が発生する() {
            thrown.expect(InternalException.class);
            thrown.expectMessage(containsString("CSVストリームがオープンされていません。"));
            handler.close();
        }
        
        @Test
        public void CSVファイルをオープンしないでドメイン取得を実行すると例外が発生する() {
            thrown.expect(InternalException.class);
            thrown.expectMessage(containsString("CSVストリームがオープンされていません。"));
            handler.get();
        }
    }
}
