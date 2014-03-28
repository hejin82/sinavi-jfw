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

package jp.co.ctc_g.jse.paginate;

import jp.co.ctc_g.jse.core.taglib.NestedParameterTag;

/**
 * <p>
 * このクラスは、ページナビゲーションの際に付加するリクエストパラメータを定義します。
 * このクラスを利用すると、{@link PageNavigationTag}によって生成された、各ページに遷移するためのアンカーに、
 * 任意のリクエストパラメータを追加することができるようになります。
 * 例えば、
 * </p>
 * <pre class="brush:jsp">
 *     &lt;jse:navi action="/page" partial="${p}" param="n"&gt;
 *         &lt;jse:naviParam name="foo" value="bar" /&gt;
 *         &lt;jse:naviParam name="baz" value="qux" /&gt;
 *     &lt;/jse:navi&gt;
 * </pre>
 * <p>
 * というように定義するとします。
 * すると、サーバへのリクエストURLが、1ページ目をリクエストする場合、
 * {@code /page?n=1&foo=bar&baz=qux}のように生成されます。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see PageNavigationTag
 */
public class PageNavigationParameterTag extends NestedParameterTag {

    /**
     * デフォルトコンストラクタです。
     */
    public PageNavigationParameterTag() {}

}
