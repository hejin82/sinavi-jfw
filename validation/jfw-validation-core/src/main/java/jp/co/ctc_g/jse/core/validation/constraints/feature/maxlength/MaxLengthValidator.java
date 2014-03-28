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

package jp.co.ctc_g.jse.core.validation.constraints.feature.maxlength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import jp.co.ctc_g.jse.core.validation.constraints.MaxLength;
import jp.co.ctc_g.jse.core.validation.util.Validators;

/**
 * <p>
 * このクラスは、{@link MaxLength}バリデータの検証アルゴリズムを実装しています。
 * </p>
 * <p>
 * {@link MaxLength}バリデータの検証アルゴリズムは、{@link Validators#maxLength(CharSequence, int)}を利用しています。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see MaxLength
 */
public class MaxLengthValidator implements ConstraintValidator<MaxLength, CharSequence> {

    private int size;

    /**
     * デフォルトコンストラクタです。
     */
    public MaxLengthValidator() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(MaxLength constraint) {
        this.size = constraint.value();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(CharSequence suspect, ConstraintValidatorContext context) {
        if (Validators.isEmpty(suspect)) return true;
        return Validators.maxLength(suspect, size);
    }
}
