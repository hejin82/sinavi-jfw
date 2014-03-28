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

package jp.co.ctc_g.jfw.test.unit;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * <p>
 * このクラスは、テストの実行順に依存したテストを行うために必要なテストランナーです。
 * 以下の場合、このクラスをランナーとして指定する必要があります。
 * </p>
 * <ul>
 *  <li>FrameworkResourceでフレームワーク内部の挙動を変更したいとき</li>
 * </ul>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class OrderedRunner extends BlockJUnit4ClassRunner {

    /**
     * コンストラクタです。
     * @param clazz テスト対象クラス
     * @throws InitializationError 初期処理時のエラー
     */
    public OrderedRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> list = super.computeTestMethods();
        Collections.sort(list, new Comparator<FrameworkMethod>() {
            @Override
            public int compare(FrameworkMethod f1, FrameworkMethod f2) {
                Order o1 = f1.getAnnotation(Order.class);
                Order o2 = f2.getAnnotation(Order.class);
                if (o1 == null || o2 == null) { return -1; }
                return o1.order() - o2.order();
            }
        });
        return list;
    }

}
