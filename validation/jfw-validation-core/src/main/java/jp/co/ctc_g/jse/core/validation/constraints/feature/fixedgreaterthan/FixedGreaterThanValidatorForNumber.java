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

package jp.co.ctc_g.jse.core.validation.constraints.feature.fixedgreaterthan;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.ctc_g.jse.core.validation.constraints.FixedGreaterThan;
import jp.co.ctc_g.jse.core.validation.util.Validators;

/**
 * <p>
 * このクラスは、{@link FixedGreaterThan}バリデータの検証アルゴリズムを実装しています。
 * </p>
 * <p>
 * {@link FixedGreaterThan}バリデータの検証アルゴリズムは、プロパティの値が比較対象の数値よりも大きいかどうかを検証しています。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see FixedGreaterThan
 */
public class FixedGreaterThanValidatorForNumber implements ConstraintValidator<FixedGreaterThan, Number> {

    private BigDecimal target;

    /**
     * デフォルトコンストラクタです。
     */
    public FixedGreaterThanValidatorForNumber() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(FixedGreaterThan constraint) {
        target = Validators.toBigDecimal(constraint.value(), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Number suspect, ConstraintValidatorContext context) {
        if (suspect == null) return true;
        return Validators.greaterThan(suspect, target);
    }
}
