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

package jp.co.ctc_g.jfw.profill;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * このクラスは、Java ビーンを解析した結果を保持します。
 * Java ビーン 1 つに対して 1 つ作成されます。
 * この情報は {@link Profill} により{@link Profill#definitions キャッシュ}されるため、
 * 複数回の Java ビーン解析処理が実行されることはありません。
 * </p>
 * @see Profill
 * @see Profill#definitions
 */
public class ProfillDefinition {

    /**
     * 解析対象 Java ビーンクラス名です。
     */
    protected String name;
    
    /**
     * マッチしたプロパティと対応するプロバイダです。
     */
    protected Map<MatchedProperty, FillingProvider> providers;
    
    /**
     * マッチしたプロパティと対応する定義です。
     * これは {@link jp.co.ctc_g.jfw.profill.Profill.Nested} があるプロパティの場合に利用されます。
     */
    protected Map<MatchedProperty, ProfillDefinition> nesteds;
    
    /**
     * このクラスのインスタンスを生成します。
     */
    public ProfillDefinition() {
        providers = new HashMap<MatchedProperty, FillingProvider>();
        nesteds = new HashMap<MatchedProperty, ProfillDefinition>();
    }
    
    /**
     * 解析したクラスの完全修飾クラス名を返却します。
     * @return 解析したクラスの完全修飾クラス名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 解析したクラスの完全修飾クラス名を設定します。
     * @param name 解析したクラスの完全修飾クラス名
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * マッチしたプロパティと対応するプロバイダを追加します。
     * @param property マッチしたプロパティ
     * @param provider マッチしたプロパティに対応するプロバイダ
     */
    public void addProvider(MatchedProperty property, FillingProvider provider) {
        providers.put(property, provider);
    }
    
    /**
     * マッチしたプロパティと対応するプロバイダを返却します。
     * @return マッチしたプロパティと対応するプロバイダ
     */
    public Map<MatchedProperty, FillingProvider> getProviders() {
        return providers;
    }
    
    /**
     * マッチしたプロパティと対応するプロバイダを設定します。
     * @param providers マッチしたプロパティと対応するプロバイダ
     */
    public void setProviders(Map<MatchedProperty, FillingProvider> providers) {
        this.providers = providers;
    }
    
    /**
     * マッチしたプロパティと対応するネストされた定義情報を追加します。
     * @param property マッチしたプロパティ
     * @param definition マッチしたプロパティに対応する定義情報
     */
    public void addNested(MatchedProperty property, ProfillDefinition definition) {
        nesteds.put(property, definition);
    }

    /**
     * マッチしたプロパティと対応するネストされた定義情報を返却します。
     * @return マッチしたプロパティと対応するネストされた定義情報
     */
    public Map<MatchedProperty, ProfillDefinition> getNesteds() {
        return nesteds;
    }

    /**
     * マッチしたプロパティと対応するネストされた定義情報を設定します。
     * @param nesteds マッチしたプロパティと対応するネストされた定義情報
     */
    public void setNesteds(Map<MatchedProperty, ProfillDefinition> nesteds) {
        this.nesteds = nesteds;
    }
}
