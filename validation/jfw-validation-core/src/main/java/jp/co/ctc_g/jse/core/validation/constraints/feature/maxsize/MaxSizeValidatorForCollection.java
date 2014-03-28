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

package jp.co.ctc_g.jse.core.validation.constraints.feature.maxsize;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.ctc_g.jse.core.validation.constraints.MaxSize;

/**
 * <p>
 * このクラスは、{@link MaxSize}バリデータの検証アルゴリズムを実装しています。
 * </p>
 * <p>
 * {@link MaxSize}バリデータの検証アルゴリズムは、検証対象のコレクションの要素数が指定されたサイズより小さいかどうかを検証します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see MaxSize
 */
public class MaxSizeValidatorForCollection implements ConstraintValidator<MaxSize, Collection<?>> {

    private int size;

    /**
     * デフォルトコンストラクタです。
     */
    public MaxSizeValidatorForCollection() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(MaxSize constraint) {
        this.size = constraint.value();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Collection<?> suspect, ConstraintValidatorContext context) {
        if (suspect == null) return true;
        return suspect.size() <= size;
    }
}
