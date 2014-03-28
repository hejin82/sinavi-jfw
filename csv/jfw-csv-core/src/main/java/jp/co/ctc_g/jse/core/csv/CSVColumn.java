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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * この注釈は、対象のプロパティがCSVのカラムであることを表現します。
 * </p>
 * <p>
 * この注釈を利用することにより、
 * CSVの読込順・書込順を制御することが可能になります。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD
})
public @interface CSVColumn {

    /**
     * CSVカラムの順番を定義取得します。
     */
    double seq();

}
