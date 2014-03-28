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

import jp.co.ctc_g.jse.core.validation.constraints.feature.fixedafterequalsto.FixedAfterEqualsToValidator;
import jp.co.ctc_g.jse.core.validation.constraints.feature.fixedafterequalsto.FixedAfterEqualsToValidatorForDate;

/**
 * <p>
 * このバリデータ注釈は、プロパティが比較対象の値よりも日付として「後」あるいは「同じ」であるかどうかを検証します。
 * </p>
 * <h4>概要</h4>
 * <p>
 * プロパティが比較対象の値よりも日付として「後」あるいは「同じ」であるかどうかを検証します。
 * </p>
 * <p>サポートしているタイプ：</p>
 * <ul>
 * <li>{@link java.lang.CharSequence}</li>
 * <li>{@link java.util.Date}</li>
 * </ul>
 * <h4>利用方法</h4>
 * <p>
 * 使用例は下記の通りです。
 * </p>
 * <pre class="brush:java">
 * public class Domain {
 *     
 *     &#064;FixedAfterEqualsTo(value = "2012/10/22")
 *     private Date from;
 *     
 *     //&#064;FixedAfterEqualsTo(value = "2012/10/22") 読み込み専用メソッドにも付与することができます。
 *     public Date getFrom() { return from; }
 *     public Date setFrom(Date from) { this.from = from; }
 *     
 *     // 以下、アクセサ省略...
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see FixedAfterEqualsToValidator
 * @see FixedAfterEqualsToValidatorForDate
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FixedAfterEqualsToValidator.class, FixedAfterEqualsToValidatorForDate.class})
public @interface FixedAfterEqualsTo {
    
    /**
     * エラーメッセージのキーを指定します。
     */
    String message() default "{jp.co.ctc_g.jse.core.validation.constraints.FixedAfterEqualsTo.message}";

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
     * 比較対象の日付を文字列で指定します。
     */
    String value();
    
    /**
     * 日付の書式を指定します。
     * {@link java.text.SimpleDateFormat}の日付/時刻パターンで指定します。
     */
    String pattern() default "yyyy/MM/dd";
    
    /**
     * {@link FixedAfterEqualsTo}の配列を指定します。
     * この制約を複数指定したい場合に利用します。
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Documented
    @interface List {
        FixedAfterEqualsTo[] value();
    }
    
}