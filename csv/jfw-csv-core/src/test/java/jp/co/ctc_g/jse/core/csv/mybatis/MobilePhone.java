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

public class MobilePhone implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer terminalId;
    private String terminalName;
    private Date salesDate;
    private BigDecimal flashLevel;
    private String delFlag;
    private String updateUserId;
    private Date updateStamp;
    private String createUserId;
    private Date createStamp;
    private Long version;

    public MobilePhone() {

    }

    public Integer getTerminalId() {

        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {

        this.terminalId = terminalId;
    }

    public String getTerminalName() {

        return terminalName;
    }

    public void setTerminalName(String terminalName) {

        this.terminalName = terminalName;
    }

    public Date getSalesDate() {

        return salesDate;
    }

    public void setSalesDate(Date salesDate) {

        this.salesDate = salesDate;
    }

    public BigDecimal getFlashLevel() {

        return flashLevel;
    }

    public void setFlashLevel(BigDecimal flashLevel) {

        this.flashLevel = flashLevel;
    }

    public String getDelFlag() {

        return delFlag;
    }

    public void setDelFlag(String delFlag) {

        this.delFlag = delFlag;
    }

    public String getUpdateUserId() {

        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {

        this.updateUserId = updateUserId;
    }

    public Date getUpdateStamp() {

        return updateStamp;
    }

    public void setUpdateStamp(Date updateStamp) {

        this.updateStamp = updateStamp;
    }

    public String getCreateUserId() {

        return createUserId;
    }

    public void setCreateUserId(String createUserId) {

        this.createUserId = createUserId;
    }

    public Date getCreateStamp() {

        return createStamp;
    }

    public void setCreateStamp(Date createStamp) {

        this.createStamp = createStamp;
    }

    public Long getVersion() {

        return version;
    }

    public void setVersion(Long version) {

        this.version = version;
    }

}
