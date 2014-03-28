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

package jp.co.ctc_g.jfw.xlsbeans.bean.setter;

import java.io.Serializable;

import jp.co.ctc_g.jse.core.excel.JxVerticalRecords;
import net.java.amateras.xlsbeans.annotation.Sheet;

@Sheet(name = "mapping")
public class BadTypeTestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    public Integer getId() {
        return id;
    }

    @JxVerticalRecords(recordClass = Integer.class)
    public void setId(Integer id) {
        this.id = id;
    }

}
