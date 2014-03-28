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

package jp.co.ctc_g.jse.core.validation.constraints.feature.decimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.ctc_g.jse.core.validation.constraints.Decimal;
import jp.co.ctc_g.jse.core.validation.util.Validators;

/**
 * <p>
 * このクラスは、検証対象の数値が小数値であるかどうかの検証アルゴリズムを実装しています。
 * </p>
 * <p>
 * このフィーチャの検証アルゴリズムはBigDecimalに変換した後に有効桁数や小数点桁数を検証します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see Decimal
 */
public class DecimalValidatorForNumber implements ConstraintValidator<Decimal, java.lang.Number> {

    private boolean signed;
    private int precision;
    private int scale;

    /**
     * デフォルトコンストラクタです。
     */
    public DecimalValidatorForNumber() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Decimal constraint) {
        Decimals.validate(constraint);
        signed = constraint.signed();
        precision = constraint.precision();
        scale = constraint.scale();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(java.lang.Number suspect, ConstraintValidatorContext context) {
        if (suspect == null) return true;
        return Validators.isDecimal(suspect, signed, precision, scale);
    }
}
