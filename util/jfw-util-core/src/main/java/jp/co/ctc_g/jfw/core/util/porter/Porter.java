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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Args;

/**
 * <p>
 * このクラスはあるオブジェクトからあるオブジェクトへ&quot;値&quot;を移送するための機能を提供します。
 * このクラスは抽象実装であるため、同機能はサブクラス化されたいくつかのクラスにより提供されます。
 * </p>
 * <p>
 * このクラスは値移送の中心的な役割を演じます。パッケージ説明に記述されている通り、
 * 値移送のプロセスはいくつかのクラスに分割され、このクラスがそれらの協調動作の指揮をとります。
 * （パッケージ説明を読んでいない方は、ここでいったんパッケージ説明に戻ることをお勧めします）
 * </p>
 * <p>
 * もし独自の移送元ストラテジと移送先ストラテジをPorterに組み込みたい場合、
 * その方法は単純です。
 * ここでは、HttpSessionオブジェクトを移送元として、Mapを移送先として例を挙げてみます。
 * まず、{@link SourceStrategy}を
 * 実装したクラスを作成します。
 * ここでは、HttpSessionオブジェクトのパラメータをソースとするSessionSourceStrategyというクラスを作成しました。
 * <pre>
 * public class SessionSourceStrategy implements SourceStrategy {
 *     private HttpSession session;
 *     private Enumeration<?> enumeration;
 *     public void initialize(Object source, Object dest) {
 *         assert source instanceof HttpSession: "I need HttpSession!";
 *         session = (HttpSession) source;
 *         enumeration = session.getAttributeNames();
 *     }
 *     public boolean hasNext() {
 *         return enumeration.hasMoreElements();
 *     }
 *     public PortablePair nextPair() {
 *         String name = enumeration.nextElement();
 *         Object value = session.getAttribute(name);
 *         return new PortablePair(name, value);
 *     }
 * }
 * </pre>
 * <p>
 * そして、移送先です。移送先はMapですので、porterパッケージが提供している
 * {@link MapDestinationStrategy}
 * を利用します。
 * これで準備は整いました。porterクラスを以下のように拡張します。
 * </p>
 * <pre>
 * public class SessionToMapPorter extends Porter {
 *     public SessionToMapPorter() {
 *         super();
 *         super.setSourceStrategy(new SessionSourceStrategy());
 *         super.setDestinationStrategy(new MapDestinationStrategy());
 *     }
 *     public SessionToMapPorter carry(HttpSession source) {
 *         super.setSource(source);
 *         return this;
 *     }
 *     public void into(Map<String, Object> dest) {
 *         super.setDestination(dest);
 *         super.process();
 *     }
 * }
 * </pre>
 * <p>
 * このクラスは以下のように利用します。
 * </p>
 * <pre>
 * HttpSession session = // 何らかのHttpSession取得手段
 * Map<String, Object> result = new HashMap<String, Object>();
 * new SessionToMapPorter().carry(session).into(result);
 * </pre>
 * <p>
 * 上記のようにSessionToMapPorterはcarry().into()というメソッド呼び出しにてその機能を実行していますが、
 * これが必須なわけではありません。以下のように書いても構いません。
 * </p>
 * <pre>
 * public class SessionToMapPorter extends Porter {
 *     public SessionToMapPorter() {
 *         // 上記と同様
 *     }
 *     public void copy(Object source, Object destination) {
 *         super.setSource(source);
 *         super.setDestination(destination);
 *         super.process();
 *     }
 * }
 * </pre>
 * <p>
 * また、結果を出力引数として受け取っていますが、そうしなければならないわけでもありません。
 * SessionToMapPorterが内部的に空のHashMapを作成してPorterに渡し、結果を戻り値で受け取るようにAPIを設計しても構いません。
 * 出力引数を嫌う場合には、
 * </p>
 * <pre>
 * public class SessionToMapPorter extends Porter {
 *     public SessionToMapPorter() {
 *         // 上記と同様
 *     }
 *     &#64;SuppressWarnings("unchecked")
 *     public Map<String, Object> transportToMap(Object source) {
 *         super.setSource(source);
 *         super.setDestination(new HashMap());
 *         return (Map<String, Object>) super.process();
 *     }
 * }
 * </pre>
 * <p>
 * とすればよいでしょう。
 * </p>
 * <p>
 * porterパッケージ内には
 * {@link SourceStrategy}や、
 * {@link DestinationStrategy}
 * を実装したクラスが複数あります。また、
 * {@link ManipulationFilter}を実装したクラスも複数あります。
 * もし、既存のporterで実現できないような値移送がある場合には、
 * これらの実装を参考にして、値移送という退屈で面倒な作業を是非自動化してみてください。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see SourceStrategy
 * @see DestinationStrategy
 * @see ManipulationFilter
 * @see PortablePair
 */
public abstract class Porter {

    /**
     * データ入力元となるオブジェクトです。
     * {@link SourceStrategy SourceStrategy}により利用されます。
     */
    private Object source;

    /**
     * データ出力先となるオブジェクトです。
     * {@link DestinationStrategy　DestinationStrategy}により利用されます。
     */
    private Object destination;

    private SourceStrategy sourceStrategy;
    private DestinationStrategy destinationStrategy;
    private List<ManipulationFilter> manipulationFilters;

    /**
     * Porterを生成します。
     */
    protected Porter() {
        manipulationFilters = new ArrayList<ManipulationFilter>(4);
    }

    /**
     * 値移送処理を開始します。
     * このメソッドは、まず、{@link #checkState()}を呼び出し、自身の状態を検証します。
     * 検証にて問題なければ、{@link #initialize() initialize()}を呼びだし、
     * 次に{@link #transport() transport()}を呼び出して実際に値の移送をします。
     * 値移送処理が完了すると、このメソッドは戻り値としてオブジェクト型を返します。
     * これは、もちろん、データ出力先のオブジェクトなのですが、{@link #setDestination(Object) setDestinationメソッド}で指定したオブジェクトであるかどうかは、
     * {@link DestinationStrategy#complete() DestinationPairAssignmentStrategyのcompleteメソッド}
     * の戻り値が{@link #setDestination(Object) setDestinationメソッド}に渡された引数をそのまま返却するかどうかに依存します。
     * @return データ出力先オブジェクト
     * @see #setDestination(Object)
     * @see DestinationStrategy#complete()
     */
    protected Object process() {
        checkState();
        initialize();
        transport();
        Object result = destinationStrategy.complete();
        return result;
    }

    /**
     * 自身に{@link SourceStrategy SourceStrategy}、
     * {@link DestinationStrategy DestinationStrategy}、
     * {@link #source データ入力元オブジェクト}、
     * {@link #destination データ出力先オブジェクト}が関連付けられているかどうかを検証します。
     * @throws FrameworkException このオブジェクトの状態が不正であった場合
     */
    protected void checkState() {
        if (sourceStrategy == null) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("parameter", "sourceStrategy");
            throw new InternalException(
                    Porter.class, "E-UTIL-PORTER#0001", replace);
        }
        if (destinationStrategy == null) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("parameter", "destinationStrategy");
            throw new InternalException(
                    Porter.class, "E-UTIL-PORTER#0001", replace);
        }
        if (source == null) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("parameter", "source");
            throw new InternalException(
                    Porter.class, "E-UTIL-PORTER#0001", replace);
        }
        if (destination == null) {
            Map<String, String> replace = new HashMap<String, String>(1);
            replace.put("parameter", "destination");
            throw new InternalException(
                    Porter.class, "E-UTIL-PORTER#0001", replace);
        }
    }

    /**
     * このPorterに関連付けられている{@link SourceStrategy SourceStrategy}、
     * {@link DestinationStrategy DestinationStrategy}、
     * そして{@link ManipulationFilter PortablePairManipulationFilter群}を初期化します。
     * このメソッドは{@link #process() process()}メソッドから呼び出されますので、
     * カスタムPorterを作成する開発者がオーバライドする必要はありません。
     * 初期化処理をカスタマイズしたい場合にのみオーバライドしてください。
     */
    protected void initialize() {
        sourceStrategy.initialize(source, destination);
        destinationStrategy.initialize(source, destination);
        for (ManipulationFilter filter : manipulationFilters) {
            filter.initialize(source, destination);
        }
    }

    /**
     * 値をデータ入力元からデータ出力先へと移送します。
     * このメソッドは、
     * <ol>
     * <li>{@link SourceStrategy SourceStrategy}から{@link PortablePair PortablePair}の取得</li>
     * <li>設定されている{@link ManipulationFilter ManipulationFilter}の適用</li>
     * <li>{@link DestinationStrategy DestinationStrategy}による値の代入</li>
     * </ol>
     * を繰り返します。
     * カスタムPorterを作成する開発者がオーバライドする必要はありません。
     * このメソッドは{@link #process() process()メソッド}から呼び出されます。
     * もし何らかの理由により移送処理をカスタマイズしたい場合には、このメソッドをオーバライドしてください。
     */
    protected void transport() {
        TRANSPORT_MAIN: while (sourceStrategy.hasNext()) {
            PortablePair sourcePair = sourceStrategy.nextPair();
            if (sourcePair == null) continue;
            PortablePair filteredPair = sourcePair;
            for (ManipulationFilter filter : manipulationFilters) {
                filteredPair = filter.manipulate(filteredPair);
                if (filteredPair == null) continue TRANSPORT_MAIN;
            }
            destinationStrategy.assign(filteredPair);
        }
    }

    /**
     * {@link ManipulationFilter ManipulationFilter}を追加します。
     * @param filter 追加したい{@link ManipulationFilter ManipulationFilter}
     * @return　指定されたフィルタが追加されたPorterインスタンス
     */
    protected Porter addManipulationFilter(ManipulationFilter filter) {
        Args.checkNotNull(filter);
        manipulationFilters.add(filter);
        return this;
    }

    /**
     * {@link ManipulationFilter ManipulationFilter}を削除します。
     * @param filter 削除したい{@link ManipulationFilter ManipulationFilter}
     * @return　削除されたフィルタのインスタンス。フィルタが登録されていなかった場合にはnull
     */
    protected ManipulationFilter removeManipulationFilter(
            ManipulationFilter filter) {
        Args.checkNotNull(filter);
        if (manipulationFilters.remove(filter)) {
            return filter;
        } else {
            return null;
        }
    }

    /**
     * {@link ManipulationFilter ManipulationFilter}をまとめて設定します。
     * @param filters 追加したい複数の{@link ManipulationFilter ManipulationFilter}のリスト
     * @return　指定されたフィルタが設定されたPorterインスタンス
     */
    protected Porter setManipulationFilters(List<ManipulationFilter> filters) {
        Args.checkNotEmpty(filters);
        manipulationFilters.addAll(filters);
        return this;
    }

    /**
     * 設定されている{@link ManipulationFilter ManipulationFilter}を返却します。
     * @return 設定されている{@link ManipulationFilter ManipulationFilter}
     */
    protected List<ManipulationFilter> getManipulationFilters() {
        return manipulationFilters;
    }

    /**
     * 設定されているデータ入力元オブジェクトを返却します。
     * @return 設定されているデータ入力元オブジェクト
     */
    protected Object getSource() {
        return source;
    }

    /**
     * データ入力元オブジェクトを設定します。
     * 引数に<code>null</code>を設定することはできません。
     * @param source データ入力元オブジェクト
     * @return 指定されたデータ入力元オブジェクトが設定されたPorterインスタンス
     */
    protected Porter setSource(Object source) {
        Args.checkNotNull(source);
        this.source = source;
        return this;
    }

    /**
     * データ出力先オブジェクトを設定します。
     * @param destination データ出力先オブジェクト
     * @return 指定されたデータ出力先オブジェクトが設定されたPorterインスタンス
     */
    protected Porter setDestination(Object destination) {
        Args.checkNotNull(destination);
        this.destination = destination;
        return this;
    }

    /**
     * 設定されているデータ出力先オブジェクトを返却します。
     * @return 設定されているデータ出力先オブジェクト
     */
    protected Object getDestination() {
        return destination;
    }

    /**
     * 設定されている{@link SourceStrategy}を返却します。
     * @return 設定されている{@link SourceStrategy}
     * @see SourceStrategy
     */
    public SourceStrategy getSourceStrategy() {
        return sourceStrategy;
    }

    /**
     * {@link SourceStrategy}を設定します。
     * @param sourceStrategy 設定したい{@link SourceStrategy}
     * @return 指定された{@link SourceStrategy}が設定されたPorterインスタンス
     * @see SourceStrategy
     */
    public Porter setSourceStrategy(
            SourceStrategy sourceStrategy) {
        Args.checkNotNull(sourceStrategy);
        this.sourceStrategy = sourceStrategy;
        return this;
    }

    /**
     * 設定されている{@link DestinationStrategy}を返却します。
     * @return 設定されている{@link DestinationStrategy}
     * @see DestinationStrategy
     */
    public DestinationStrategy getDestinationStrategy() {
        return destinationStrategy;
    }

    /**
     * {@link DestinationStrategy}を設定します。
     * @param destinationStrategy 設定したい{@link DestinationStrategy}
     * @return 指定された{@link DestinationStrategy}が設定されたPorterインスタンス
     * @see DestinationStrategy
     */
    public Porter setDestinationStrategy(
            DestinationStrategy destinationStrategy) {
        Args.checkNotNull(destinationStrategy);
        this.destinationStrategy = destinationStrategy;
        return this;
    }

}
