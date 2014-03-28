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

package jp.co.ctc_g.jse.core.taglib;

/**
 * <p>
 * このインタフェースは、{@link NestedarameterTag}からのコールバックを受け取るためのインタフェースです。
 * これにより、ネストされたタグの値（キー：バリューパラメータ）を親タグが簡単に利用できるようになります。
 * </p>
 * <h4>単純な実装例</h4>
 * <p>
 * では、非常に簡単な例を通して、このインタフェースの利用例を提示します。
 * </p>
 * <pre class="brush:java">
 * public class ExampleTag extends SimpleTagSupport implements ParameterAware {
 *
 *     public void awareParameter(String name, String value) {
 *         System.out.println("キー: " + name + ", 値: " + value);
 *     }
 *
 *     public void awareParameter(String name, String[] values) {
 *         String value = Strings.join(",", values);
 *         System.out.println("キー: " + name + ", 値: " + value);
 *     }
 * }
 * </pre>
 * <p>
 * TLDファイルを明示しませんが、
 * 上記クラスは、exタグライブラリにてexampleという名前のタグになると考えてください。
 * では、このタグにネストされるタグを以下に示します。
 * <pre class="brush:java">
 * public class ExampleParameterTag extends NestedParameterTag {
 * }
 * </pre>
 * <p>
 * 特に実装は必要ありません。
 * 同様にTLDファイルを明示しませんが、
 * 上記クラスは、exampleタグライブラリにてparamという名前のタグになると考えてください。
 * paramには、name属性とvalue属性があります。
 * では、これを利用してみます。
 * </p>
 * <pre class="brush:jsp">
 * &lt;ex:example&gt;
 *     &lt;ex:param name="foo" value="bar" /&gt;
 * &lt;/ex:example&gt;
 * </pre>
 * <p>
 * このJSPを実行すると、標準出力には、「キー: foo, 値: bar」と出力されます。
 * このように、自作のタグライブラリでキー:バリューのパラメータをを受け取る処理を実装することが容易になります。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface ParameterAware {

    /**
     * ネストされたタグに{@link NestedParameterTag}がある場合、
     * そのタグからコールバックされます。
     * @param name パラメータ名
     * @param value パラメータ値
     */
    void awareParameter(String name, String value);

    /**
     * ネストされたタグに{@link NestedParameterTag}がある場合、
     * そのタグからコールバックされます。
     * @param name パラメータ名
     * @param values パラメータ値
     */
    void awareParameter(String name, String[] values);

}
