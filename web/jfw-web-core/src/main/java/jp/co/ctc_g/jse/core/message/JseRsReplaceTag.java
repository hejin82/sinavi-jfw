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

package jp.co.ctc_g.jse.core.message;

import jp.co.ctc_g.jse.core.taglib.NestedParameterTag;

/**
 * <p>
 * このクラスは、{@link JseRsTag}と強調して、リソース値のプレースホルダを置換する機能を提供します。
 * </p>
 * <h4>タグの利用</h4>
 * <p>
 * このタグの利用方法や利用可能な属性については{@link JseRsTag}にて解説されていますので、そちらを参照してください。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see JseRsTag
 * @see NestedParameterTag
 */
public class JseRsReplaceTag extends NestedParameterTag {

    /**
     * デフォルトコンストラクタです。
     */
    public JseRsReplaceTag() {}

}
