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

package jp.co.ctc_g.jfw.core.internal;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * <p>
 * このクラスは、クラスコンフィギュレーション情報を管理します。
 * この機構はJ-Framework内部で利用されるものであり、アプリケーションから利用されることを意図していません。
 * このAPIはアプリケーションに対して公開されたAPIではありませんので、バージョンポリシーにかかわらず変更される可能性があります。
 * </p>
 * <h4>コンフィグ情報を取得する</h4>
 * <p>
 * コンフィグ情報を取得するには、{@link InternalMessages }の静的メソッドを通じて行ないます。
 * 例えば、ViewIdクラスのコンフィグ情報を取得するコードは下記のようになります。
 * </p>
 * 
 * <pre>
 * Config c = Internals.getConfig(ViewId.class);
 * c.find(&quot;container_key&quot;);
 * </pre>
 * <p>
 * クラスコンフィグレーション情報は、 クラスパスの jp/co/ctc_g/jfw/core/FrameworkResources.properties
 * に定義されています。 プロパティファイルのキーはクラスのFQCNがプレフィックスとして付加されていますが、
 * コンフィグレーションの値を取得する際にはこのプレフィックスを意識する必要はありません。
 * 例えば、先ほどのViewIdクラスの例であれば、次の例のように定義されています。
 * </p>
 * 
 * <pre>
 * jp.co.ctc_g.jfw.vid.ViewId.container_key = jp.co.ctc_g.jfw.vid.ViewId.CONTAINER_KEY
 * </pre>
 * 
 * <h4>クラスコンフィグオーバライド</h4>
 * <p>
 * J-Framework内部で利用されるクラスコンフィグレーションはクラスパス直下の FrameworkResources.properties
 * ファイルで上書きすることができます。
 * </p>
 * <p>
 * このファイルに同一のキーが設定されていた場合には、その値が利用されます。同一のキーが存在しない場合には、
 * デフォルトのクラスコンフィグレーション情報が利用されます。
 * </p>
 * <p>
 * このプロパティファイルの場所は jp.co.ctc_g.jfw.setting システムプロパティによって変更可能です。
 * 設定する値はリソースバンドルの指定方法と同様になります。
 * </p>
 * 
 * @author ITOCHU Techno-Solutions Corporation.
 * @see InternalMessages
 */
public class Config {

    private ResourceBundle parameters;
    private final String classname;

    /**
     * 指定されたプロパティを利用して、
     * このクラスのインスタンスを生成します。
     * @param classname クラス名
     * @param parameters コンフィギュレーション値
     */
    public Config(String classname, ResourceBundle parameters) {
        this.classname = classname;
        this.parameters = parameters;
    }

    /**
     * 指定されたキーに関連付けられたコンフィギュレーション値を検索します。
     * キーが定義されていない場合、このメソッドは{@code null}を返却します。
     * キーが{@link jp.co.ctc_g.jfw.core.util.Strings#isEmpty(CharSequence) 空文字}である場合、
     * このメソッドは{@code null}を返却します。
     * @param key 検索対象のキー
     * @return キーに対応する値
     * @see jp.co.ctc_g.jfw.core.util.Strings#isEmpty(CharSequence)
     */
    public String find(String key) {
        return find(key, null);
    }

    /**
     * 指定されたキーに関連付けられたコンフィギュレーション値を検索し、
     * 見つからない場合は、{@code alternative}で指定された文字列を返却します。
     * キーが見つからない場合とは、
     * <ul>
     *  <li>キーが定義されていない場合</li>
     *  <li>キーが{@link jp.co.ctc_g.jfw.core.util.Strings#isEmpty(CharSequence) 空文字}である場合</li>
     * </ul>
     * のことを指します。
     * このメソッドは、
     * <pre>
     * Config c = Internals.getConfig(Foo.class);
     * String value = c.find("bar.baz");
     * value = Args.proper(value, "foobar");
     * </pre>
     * と同様です。
     * @param key 検索対象のキー
     * @param alternative キーが見つからない場合のデフォルト値
     * @return キーに対応する値、あるはデフォルト値
     * @see jp.co.ctc_g.jfw.core.util.Strings#isEmpty(CharSequence)
     * @see jp.co.ctc_g.jfw.core.util.Args#proper(Object, Object)
     */
    public String find(String key, String alternative) {
        try {
            String realKey = new StringBuilder().append(classname).append('.').append(key).toString();
            return parameters.getString(realKey);
        } catch (MissingResourceException e) {
            return alternative;
        }
    }

    /**
     * このインスタンスに関連付けられている全てのコンフィギュレーションキーを返却します。
     * キーが存在しない場合、このメソッドは空のセットを返却します。
     * @return このインスタンスに関連する全てのコンフィギュレーションキー
     */
    public Set<Object> keys() {
        HashSet<Object> set = new HashSet<Object>();
        
        Enumeration<String> keys = parameters.getKeys();
        while(keys.hasMoreElements()) {
            String key = keys.nextElement();
            if(key.startsWith(classname)) {
                String keyBody = key.substring(classname.length() + 1);
                set.add(keyBody);
            }
        }
        
        return set;
    }

}
