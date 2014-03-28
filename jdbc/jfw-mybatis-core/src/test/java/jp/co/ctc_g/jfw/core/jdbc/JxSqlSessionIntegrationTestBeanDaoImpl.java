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

package jp.co.ctc_g.jfw.core.jdbc;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class JxSqlSessionIntegrationTestBeanDaoImpl implements JxSqlSessionIntegrationTestBeanDao {

    private static final String NS = JxSqlSessionIntegrationTestBeanDao.class.getName();

    @Autowired
    private SqlSessionTemplate template;

    @Override
    public List<JxSqlSessionIntegrationTestBean> listWithPaginating(JxSqlSessionIntegrationTestCriteria query) {
        return template.selectList(w("listWithPaginating"), query);
    }

    @Override
    public List<JxSqlSessionIntegrationTestBean> listWithBoundsWithPaginating(JxSqlSessionIntegrationTestCriteria query, RowBounds bounds) {
        return template.selectList(w("listWithBoundsWithPaginating"), query, bounds);
    }

    private String w(String statement) {
        return NS + "." + statement;
    }
}
