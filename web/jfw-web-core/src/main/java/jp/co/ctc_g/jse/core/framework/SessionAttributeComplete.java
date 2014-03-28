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

package jp.co.ctc_g.jse.core.framework;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * セッション・スコープにストアしたモデル・オブジェクトのライフサイクルの完了をマークする注釈です。<br/>
 * コントローラのハンドラ・メソッドにこの注釈を付加することにより、モデル・オブジェクトのライフサイクルの完了が宣言され
 * ハンドラ・メソッドの処理終了後にセッション・スコープから削除されます。<br/>
 * 引数{@code valu}に対して {@link org.springframework.web.bind.annotation.ModelAttribute} 注釈で指定したモデル名を設定することにより
 * 削除対象のモデルを指定することができます。指定がない場合は、該当コントローラの{@link org.springframework.web.bind.annotation.SessionAttributes}注釈
 * の {@code value} 属性で指定されたすべてのモデル・オブジェクトがセッションから削除されます。<br/>
 * 下記の例では、{@code bar} メソッドに対して注釈が付加されているため{@code bar} メソッドの処理、および、JSPのレンダリング処理
 * までが完了した後に削除処理が実行されます。
 * </p>
 * <pre>
 * &#64;Controller
 * public class FooController {
 * 
 *     &#64;SessionAttributeComplete("mobilePhone")
 *     &#64;RequestMapping("readyToCreate")
 *     public String bar() {
 *     ・・・
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SessionAttributeComplete {

    /**
     * モデル名の配列
     */
    String[] value() default {};
    
    /**
     * モデルタイプの配列
     */
    Class<?>[] types() default {};
}
