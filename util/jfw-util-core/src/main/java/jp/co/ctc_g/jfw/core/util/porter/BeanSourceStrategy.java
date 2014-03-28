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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.co.ctc_g.jfw.core.util.Beans;
import jp.co.ctc_g.jfw.core.util.Reflects;

/**
 * <p>
 * このクラスは、Javaビーンをデータ入力元とする{@link SourceStrategy}です。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see SourceStrategy
 * @see DestinationStrategy
 * @see Porter
 * @see PortablePair
 */
public class BeanSourceStrategy implements SourceStrategy {

    private Object source;
    private Iterator<Method> methodsIterator;

    /**
     * デフォルトコンストラクタです。
     */
    public BeanSourceStrategy() {}

    /**
     * 指定されたsourceオブジェクトを入力元として、このインスタンスを初期化します。
     * @param src データ入力元オブジェクト
     * @param dest データ出力先オブジェクト
     * @see SourceStrategy#initialize(Object, Object)
     */
    public void initialize(Object src, Object dest) {
        this.source = src;
        PropertyDescriptor[] propertyDescriptors =
            Beans.findPropertyDescriptorsFor(src.getClass());
        List<Method> methods = new ArrayList<Method>(propertyDescriptors.length);
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method m = pd.getReadMethod();
            if (m != null) methods.add(m);
        }
        methodsIterator = methods.iterator();
    }

    /**
     * 次のペアが存在するかどうかを返却します。
     * @return 次のペアが存在する場合にtrue
     * @see SourceStrategy#hasNext()
     */
    public boolean hasNext() {
        return methodsIterator.hasNext();
    }

    /**
     * 次のペアを取得します。
     * @return {@link PortablePair}
     * @see SourceStrategy#nextPair()
     */
    public PortablePair nextPair() {
        Method m = methodsIterator.next();
        m.setAccessible(true);
        String key = Beans.generatePropertyNameFor(m.getName());
        Object value = Reflects.invoke(m, source);
        return new PortablePair(key, value);
    }

}
