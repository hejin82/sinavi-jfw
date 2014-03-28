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

package jp.co.ctc_g.jse.core.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import jp.co.ctc_g.jse.core.validation.constraints.feature.number.NumberValidator;
import jp.co.ctc_g.jse.core.validation.constraints.feature.number.NumberValidatorForNumber;

/**
 * <p>
 * このバリデータ注釈は、プロパティの文字列が整数かつ指定された有効桁数以内かどうかを検証します。
 * </p>
 * <h4>概要</h4>
 * <p>
 * この検証は、検証対象をBigDecimal型に変換し、整数かつ指定された有効桁数以内かどうかを検証します。
 * 符号を含む文字列を許可する場合は、signed属性で指定することができます。
 * このときの符号は半角・全角の区別はありません。
 * </p>
 * <p>サポートしているタイプ：</p>
 * <ul>
 * <li>{@link java.lang.CharSequence}</li>
 * <li>{@link java.lang.Number}</li>
 * </ul>
 * <h4>利用方法</h4>
 * <p>
 * 使用例は下記の通りです。
 * </p>
 * <pre class="brush:java">
 * public class Domain {
 *     &#064;NumericFormat
 *     &#064;Number(precision = 10)
 *     private String value;
 *     //&#064;Number 読み込み専用メソッドにも付与することができます。
 *     public String getValue() { return value };
 *     public String setValue(String value) { this.value = value };
 * </code>
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see NumberValidator
 * @see NumberValidatorForNumber
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NumberValidator.class, NumberValidatorForNumber.class})
public @interface Number {

    /**
     * エラーメッセージのキーを指定します。
     */
    String message() default "{jp.co.ctc_g.jse.core.validation.constraints.Number.message}";

    /**
     * 検証グループを指定します。
     */
    Class<?>[] groups() default {};

    /**
     * ペイロードを指定します。
     * デフォルトの検証プロセスでは利用されません。
     */
    Class<? extends Payload>[] payload() default {};

    /** 
     * 有効桁数を指定します。
     */
    int precision();

    /** 
     * 符号の有無を指定します。
     */
    boolean signed() default false;

    /**
     * {@link Number}の配列を指定します。
     * この制約を複数指定したい場合に利用します。
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Documented
    @interface List {
        Number[] value();
    }

}