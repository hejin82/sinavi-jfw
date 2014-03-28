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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MobilePhoneCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer fromTerminalId;
    private Integer toTerminalId;
    private String terminalName;
    private Date fromSalesDate;
    private Date toSalesDate;
    private BigDecimal flashLevel;

    public MobilePhoneCriteria() {

    }

    public Integer getFromTerminalId() {

        return fromTerminalId;
    }

    public void setFromTerminalId(Integer fromTerminalId) {

        this.fromTerminalId = fromTerminalId;
    }

    public Integer getToTerminalId() {

        return toTerminalId;
    }

    public void setToTerminalId(Integer toTerminalId) {

        this.toTerminalId = toTerminalId;
    }

    public String getTerminalName() {

        return terminalName;
    }

    public void setTerminalName(String terminalName) {

        this.terminalName = terminalName;
    }

    public Date getFromSalesDate() {

        return fromSalesDate;
    }

    public void setFromSalesDate(Date fromSalesDate) {

        this.fromSalesDate = fromSalesDate;
    }

    public Date getToSalesDate() {

        return toSalesDate;
    }

    public void setToSalesDate(Date toSalesDate) {

        this.toSalesDate = toSalesDate;
    }

    public BigDecimal getFlashLevel() {

        return flashLevel;
    }

    public void setFlashLevel(BigDecimal flashLevel) {

        this.flashLevel = flashLevel;
    }
}
