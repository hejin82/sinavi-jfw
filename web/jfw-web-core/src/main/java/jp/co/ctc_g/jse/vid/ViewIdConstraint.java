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

package jp.co.ctc_g.jse.vid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.co.ctc_g.jse.core.framework.Controllers;

/**
 * <p>
 * この注釈は、画面ID制約を表現します。
 * この制約により、ある画面からのみ実行できる処理や、
 * ある画面からのみ遷移できる画面などを実現することができます。
 * </p>
 * <h4>概要</h4>
 * <p>
 * この注釈を有効に利用するには、下記の条件を満たす必要があります。
 * </p>
 * <ul>
 *  <li>{@link ViewId#is(ViewId, javax.servlet.http.HttpServletRequest) ViewId.is(..)}や{@link PankuzuTag &lt;vid:is id="..." /&gt;}を利用している</li>
 *  <li>{@link Action}を利用している</li>
 * </ul>
 * <p>
 * では、利用例を見てみます。
 * </p>
 * <pre class="brush:java">
 *  &#64;Action
 *  public class ExampleAction {
 *
 *      &#64;Action.Path("/vid/example")
 *      &#64;ViewIdConstraint(allow="VID#0001")
 *      public String example() {
 *          return "/vid/example.jsp";
 *      }
 *  }
 * </pre>
 * <p>
 * このようにすることで、<code>/vid/example</code>つまり<code>ExampleAction#example</code>は、
 * 直前の画面IDが<code>VID#0001</code>である場合にだけ実行されることになります。
 * なお、許可されていない画面IDからのアクセスがあった場合、
 * {@link InvalidViewTransitionException}が発生します。
 * </p>
 * <h4>許可IDと例外ID</h4>
 * <p>
 * 上記は<code>allow</code>属性を利用して、許可されるパターンのみを指定しました。
 * このように許可する画面IDを指定するには、<code>allow</code>に画面IDを指定します。
 * 複数の画面IDへの許可が必要な場合には、
 * </p>
 * <pre class="brush:java">
 * &#64;ViewIdConstraint(allow="VID#0001|VID#0002")
 * </pre>
 * <p>
 * のように、<code>|</code>記号で区切ってください。
 * あるいは、画面IDの一部が同じ場合には、
 * </p>
 * <pre class="brush:java">
 * &#64;ViewIdConstraint(allow="VID#000[0-9]")
 * </pre>
 * <p>
 * というように、範囲指定をすることができます。
 * つまり、これは<strong>正規表現に他なりません</strong>。
 * </p>
 * <p>
 * 許可（allow属性）だけでもうまくいきますが、
 * デフォルトでは全てを許可する性質を利用して、
 * 例外（except属性）を指定するとシンプルに設定できる場合があります。
 * </p>
 * <pre class="brush:java">
 * &#64;ViewIdConstraint(except="VID#0001")
 * </pre>
 * <p>
 * この場合、<code>VID#0001</code>以外の画面IDは許可されます。
 * 許可と例外を両方とも指定していた場合、
 * </p>
 * <pre class="brush:java">
 * &#64;ViewIdConstraint(allow="VID#000[1-3]", except="VID#0002")
 * </pre>
 * <p>
 * この場合、<code>VID#0001</code>と<code>VID#0003</code>は許可されますが、
 * <code>VID#0002</code>は<code>except</code>属性の指定により許可されません。
 * </p>
 * </p>
 *
 * @author ITOCHU Techno-Solutions Corporation.
 * @see ViewId
 * @see ViewTransitionKeeper
 * @see InvalidViewTransitionException
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ViewIdConstraint {

    /**
     * 許可する画面IDのパターンを指定します。
     * デフォルトは空文字ですが、これは{@link ViewTransitionKeeper}に従うことを意味します。
     */
    String allow() default "";

    /**
     * 画面IDの例外を指定します。
     * {@link #allow() 許可パターン}にマッチした画面IDのうち、
     * このパターンに該当する画面IDはアクセスが拒否されます。
     */
    String except() default "";

    /**
     * 画面IDが保持されているスコープを指定します。
     * @see Controllers#SCOPE_SESSION
     */
    String scope() default Controllers.SCOPE_SESSION;
}
