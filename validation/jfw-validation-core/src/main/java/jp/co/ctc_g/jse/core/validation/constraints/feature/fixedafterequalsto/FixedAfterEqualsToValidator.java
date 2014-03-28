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

package jp.co.ctc_g.jse.core.validation.constraints.feature.fixedafterequalsto;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.ctc_g.jse.core.validation.constraints.FixedAfterEqualsTo;
import jp.co.ctc_g.jse.core.validation.util.Validators;

/**
 * <p>
 *  このクラスは、{@link FixedAfterEqualsTo}バリデータの検証アルゴリズムを実装しています。
 * </p>
 * <p>
 * プロパティが比較対象の固定値よりも日付として「後」あるいは「同じ」であるかどうかを検証します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see FixedAfterEqualsTo
 */
public class FixedAfterEqualsToValidator implements ConstraintValidator<FixedAfterEqualsTo, CharSequence> {

    private String pattern;
    private Date target;

    /**
     * デフォルトコンストラクタです。
     */
    public FixedAfterEqualsToValidator() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(FixedAfterEqualsTo constraint) {
        target = Validators.toDate(constraint.value(), constraint.pattern(), true);
        pattern = constraint.pattern();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(CharSequence suspect, ConstraintValidatorContext context) {
        if (suspect == null) return true;
        return Validators.afterEqualsTo(Validators.toDate(suspect, pattern), target);
    }
}
