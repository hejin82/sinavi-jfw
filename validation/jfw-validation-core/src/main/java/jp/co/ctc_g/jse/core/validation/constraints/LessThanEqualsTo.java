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

import jp.co.ctc_g.jse.core.validation.constraints.feature.lessthanequalsto.LessThanEqualsToValidator;

/**
 * <p>
 * このバリデータ注釈は、プロパティの値が比較対象のプロパティの値よりも小さいかあるいは同じかどうかを検証します。
 * なお、固定値と比較したい場合は、{@link FixedLessThanEqualsTo}バリデータを利用して下さい。
 * </p>
 * <h4>概要</h4>
 * <p>
 * プロパティは {@link java.math.BigDecimal} オブジェクトに変更してから比較されます。
 * </p>
 * <p>サポートしているプロパティのタイプ：</p>
 * <ul>
 * <li>{@link java.lang.CharSequence}</li>
 * <li>{@link java.lang.Number}</li>
 * </ul>
 * <h4>利用方法</h4>
 * <p>
 * 使用例は下記の通りです。
 * </p>
 * <pre class="brush:java">
 * &#064;LessThanEqualsTo(from = "from", to = "to")
 * public class Domain {
 *     
 *     private String from;
 *     
 *     private String to;
 *
 *     // 以下、アクセサ省略...
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see LessThanEqualsToValidator
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LessThanEqualsToValidator.class})
public @interface LessThanEqualsTo {

    /**
     * エラーメッセージのキーを指定します。
     */
    String message() default "{jp.co.ctc_g.jse.core.validation.constraints.LessThanEqualsTo.message}";

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
     * 比較対象元のプロパティ名を指定します。
     */
    String from();

    /**
     * 比較対象のプロパティ名を指定します。
     */
    String to();

    /**
     * {@link LessThanEqualsTo}の配列を指定します。
     * この制約を複数指定したい場合に利用します。
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Documented
    @interface List {
        LessThanEqualsTo[] value();
    }
}