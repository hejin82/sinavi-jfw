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
import java.util.List;

import net.java.amateras.xlsbeans.annotation.Sheet;
import jp.co.ctc_g.jse.core.excel.JxVerticalRecords;

@Sheet(name = "vertical_mapping")
public class VerticalRecordsTestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<VerticalRecordTestBean> records;

    public List<VerticalRecordTestBean> getRecords() {
        return records;
    }

    @JxVerticalRecords(tableLabel = "テスト", recordClass = VerticalRecordTestBean.class)
    public void setRecords(List<VerticalRecordTestBean> records) {
        this.records = records;
    }

}
