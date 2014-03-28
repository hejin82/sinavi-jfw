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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class SqlDateLiteralConvertorTest {

    private SqlDateLiteralConvertor target;

    @Before
    public void instantiate() {
        target = new SqlDateLiteralConvertor();
    }

    @Test
    public void Dateの値をリテラルに変換する() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 10, 1);
        assertThat(target.convert(new Date(cal.getTimeInMillis())), is("'2011-11-01'"));
        ;
    }
}
