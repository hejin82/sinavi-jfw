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

import java.math.BigInteger;

public class BigIntegerTypeHandlerTestBean {

    private Integer id;
    
    private BigInteger num19;
    
    private BigInteger num25;
    
    private BigInteger num30;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getNum19() {
        return num19;
    }

    public void setNum19(BigInteger num19) {
        this.num19 = num19;
    }

    public BigInteger getNum25() {
        return num25;
    }

    public void setNum25(BigInteger num25) {
        this.num25 = num25;
    }

    public BigInteger getNum30() {
        return num30;
    }

    public void setNum30(BigInteger num30) {
        this.num30 = num30;
    }
}
