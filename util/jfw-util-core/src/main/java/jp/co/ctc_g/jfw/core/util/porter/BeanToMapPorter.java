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

package jp.co.ctc_g.jfw.core.util.porter;

import java.util.HashMap;
import java.util.Map;

import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * このクラスは、Javaビーンのプロパティを{@link Map}に移送します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see BeanSourceStrategy
 * @see MapDestinationStrategy
 * @see KeyExchangeFilter
 * @see PortablePairSelector
 * @see Porter
 */
public class BeanToMapPorter extends Porter {

    private KeyExchangeFilter exchanger;
    private PortablePairSelector selector;

    /**
     * デフォルトコンストラクタです。
     */
    public BeanToMapPorter() {}

    /**
     * 指定されたオブジェクトを移送元ソースとして、インスタンスを生成します。
     * @param source 値移送元オブジェクト
     */
    public BeanToMapPorter(Object source) {
        super();
        Args.checkNotNull(source);
        prepare();
        setSource(source);
    }

    /**
     * このインスタンスを利用する準備をします。
     * このメソッドはコンストラクタから呼び出されます。
     */
    protected void prepare() {
        exchanger = new KeyExchangeFilter();
        selector = new PortablePairSelector();
        setSourceStrategy(new BeanSourceStrategy());
        setDestinationStrategy(new MapDestinationStrategy());
        addManipulationFilter(selector);
        addManipulationFilter(exchanger);
    }

    /**
     * 移送先となるマップを生成して、そこにプロパティ値をソースから移送します。
     * @return 移送元の公開プロパティの値が移し替えられたマップオブジェクト
     */
    public Map<String, Object> create() {
        return copyTo(new HashMap<String, Object>());
    }

    /**
     * 指定されたマップ実装インスタンスに対して、プロパティ値をソースから移送します。
     * @param destination 移送先マップオブジェクト
     * @return 移送先マップオブジェクト
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> copyTo(Map<String, Object> destination) {
        Args.checkNotNull(destination);
        setDestination(destination);
        return (Map<String, Object>) process();
    }

    /**
     * プロパティ名の変換定義を追加します。
     * このメソッドは、{@link KeyExchangeFilter#exchange(String, String)}への単純な委譲です。
     * 当該クラスのJavadocも役に立つことと思います。
     * @param src 移送元クラスにて公開されているプロパティ名
     * @param dest 移送先クラスにて公開されているプロパティ名
     * @return 指定のプロパティ名変換定義が追加されたBeanPorterのインスタンス
     * @see KeyExchangeFilter
     */
    public BeanToMapPorter exchange(String src, String dest) {
        Args.checkNotEmpty(src);
        Args.checkNotEmpty(dest);
        exchanger.exchange(src, dest);
        return this;
    }

    /**
     * 設定されている変換定義を消去します。
     * @return このインスタンス
     * @see KeyExchangeFilter#clear()
     */
    public BeanToMapPorter clearExchange() {
        exchanger.clear();
        return this;
    }

    /**
     * 指定されたカラム名を無視して、移送対象から外します。
     * @param columnName 無視するカラム名
     * @return このインスタンス
     * @see PortablePairSelector#ignore(String)
     */
    public BeanToMapPorter ignore(String columnName) {
        selector.ignore(columnName);
        return this;
    }

    /**
     * 設定されている変換定義を消去します。
     * @return このインスタンス
     * @see PortablePairSelector#clear()
     */
    public BeanToMapPorter clearIgnorance() {
        selector.clear();
        return this;
    }
}
