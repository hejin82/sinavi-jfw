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

package jp.co.ctc_g.jse.core.csv;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.co.ctc_g.jfw.core.internal.InternalException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CSVConfigsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void チェックオプションが設定され長さが未設定の場合は例外が発生する() {
        thrown.expect(InternalException.class);
        thrown.expectMessage("読込行の長さチェックが指定されていますが、長さが指定されていません。");
        CSVConfigs.config().check(true).length();
    }

    @Test
    public void チェックオプションが設定されていない場合は設定した長さが取得できる() {
        int length = CSVConfigs.config().check(false).length(10).length();
        assertThat(length, is(10));
    }

}
