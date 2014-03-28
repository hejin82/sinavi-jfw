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

package jp.co.ctc_g.jfw.core.jdbc.mybatis.type;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BigIntegerTypeHandlerTestBeanDaoImpl implements BigIntegerTypeHandlerTestBeanDao {
    
    @Autowired
    private SqlSessionTemplate template;

    private static final String NAME_SPACE = BigIntegerTypeHandlerTestBeanDaoImpl.class.getName();

    @Override
    public BigIntegerTypeHandlerTestBean findById(Integer id) {
        return template.selectOne(getSqlStatementId("findById"), id);
    }

    @Override
    public int create(BigIntegerTypeHandlerTestBean domain) {
        return template.insert(getSqlStatementId("create"), domain);
    }

    @Override
    public int update(BigIntegerTypeHandlerTestBean domain) {
        return template.update(getSqlStatementId("update"), domain);
    }

    @Override
    public int delete(Integer id) {
        return template.delete(getSqlStatementId("delete"), id);
    }

    private String getSqlStatementId(String statement) {
        return NAME_SPACE + "." + statement;
    }
}
