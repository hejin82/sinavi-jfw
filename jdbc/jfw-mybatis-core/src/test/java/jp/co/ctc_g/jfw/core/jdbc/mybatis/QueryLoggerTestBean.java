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

import java.util.Date;

public class QueryLoggerTestBean {

    private Integer id;

    private String str;

    private java.sql.Date sqlDate;

    private java.sql.Time tm;

    private java.sql.Timestamp ts;

    private Date utlDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public java.sql.Date getSqlDate() {
        return sqlDate;
    }

    public void setSqlDate(java.sql.Date sqlDate) {
        this.sqlDate = sqlDate;
    }

    public java.sql.Time getTm() {
        return tm;
    }

    public void setTm(java.sql.Time tm) {
        this.tm = tm;
    }

    public java.sql.Timestamp getTs() {
        return ts;
    }

    public void setTs(java.sql.Timestamp ts) {
        this.ts = ts;
    }

    public Date getUtlDate() {
        return utlDate;
    }

    public void setUtlDate(Date utlDate) {
        this.utlDate = utlDate;
    }
}
