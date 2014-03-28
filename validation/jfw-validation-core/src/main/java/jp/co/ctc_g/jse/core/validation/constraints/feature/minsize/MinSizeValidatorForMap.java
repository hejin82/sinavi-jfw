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

package jp.co.ctc_g.jse.core.validation.constraints.feature.minsize;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.ctc_g.jse.core.validation.constraints.MinSize;

/**
 * <p>
 * このクラスは、{@link MinSize}バリデータの検証アルゴリズムを実装しています。
 * </p>
 * <p>
 * {@link MinSize}バリデータの検証アルゴリズムは、検証対象の配列の要素数が指定されたサイズより大きいかどうかを検証します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see MinSize
 */
public class MinSizeValidatorForMap implements ConstraintValidator<MinSize, Map<?, ?>> {

    private int size;

    /**
     * デフォルトコンストラクタです。
     */
    public MinSizeValidatorForMap() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(MinSize constraint) {
        this.size = constraint.value();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Map<?, ?> suspect, ConstraintValidatorContext context) {
        if (suspect == null) return true;
        return suspect.size() >= size;
    }
}
