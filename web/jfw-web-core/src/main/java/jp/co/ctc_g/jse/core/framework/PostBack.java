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

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

/**
 * <p>
 * 例外発生時のポスト・バックに関連する情報を管理します。<br/>
 * このクラスが管理する情報を利用することで、例外発生時の状況に基づく処理を実装することができます。
 * </p>
 * <h4>利用方法</h4>
 * <p>
 * このクラスから例外発生の情報を取得するには、コントローラのハンドラ・メソッドにこのクラスの型の引数を定義します。
 * リクエストがディパッチされ該当コントローラのハンドラ・メソッドを呼び出される際にフレームワークが引数定義に従ってこのクラスのインスタンスを設定します。<br/>
 * 下記が定義例です。この例では、{@link PostBack#isBack()}によって例外発生後のポスト・バックリクエストかを判定し、正常系の画面遷移リクエストに
 * 対する処理と例外発生後のポスト・バックリクエストに対する処理を分岐しています。
 * <pre>
 * &#64;Controller
 * public class FooController {
 *
 *     &#64;RequestMapping("index")
 *     public String index(PostBack postBack) {
 *         if (!postBack.isBack()) {
 *             // 正常系の画面遷移リクエストに対する処理
 *             ・・・
 *         } else {
 *             // 例外発生後のポスト・バックリクエストに対する処理
 *             ・・・
 *         }
 * </pre>
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class PostBack implements Serializable {

    private static final long serialVersionUID = -2687179966260285932L;

    /**
     * ポストバックを格納するキー
     */
    public static final String POST_BACK_ATTRIBUTE_KEY = PostBack.class.getName() + ".postBack";

    /* ポストバックのトリガーとなった例外のタイプ */
    private Class<?> exceptionType;

    /* ポストバックのトリガーとなった例外インスタンス */
    private Throwable t;

    /* モデルの名称 */
    private String modelName;

    /* モデルのインスタンス */
    private Object model;

    /* BindingResult instance */
    private BindingResult bindingResult;

    /**
     * デフォルトコンストラクタです。
     */
    public PostBack() {}

    /**
     * <p>
     * コンストラクタ。{@code PostBack}インスタンスを生成します。
     * </p>
     * @param t ポストバックのトリガーとなった例外
     */
    public PostBack(Throwable t) {
        this.t = t;
        if (t instanceof BindException) {
            BindException be = (BindException)t;
            this.exceptionType = be.getClass();
            this.modelName = be.getObjectName();
            this.model = be.getTarget();
            this.bindingResult = be.getBindingResult();
        } else {
            this.exceptionType = t.getClass();
            this.modelName = null;
            this.model = null;
            this.bindingResult = null;
        }
    }

    /**
     * 現在のリクエストがポスト・バックされたリクエストであるかを判定します。<br/>
     * @return true 現在のリクエストがポスト・バックされたリクエストの場合
     */
    public boolean isPostBackRequest() {
        if (exceptionType != null)
            return true;
        else
            return false;
    }

    /**
     * ポストバックのトリガーとなった例外インスタンスを返却します。
     * @return 例外インスタンス
     */
    Throwable getException() {
        return t;
    }

    /**
     * ポスト・バックのトリガーとなった例外クラスを返却します。<br/>
     * @return 例外クラス
     */
    public Class<?> getType() {
        return exceptionType;
    }

    /**
     * モデル名を返却します。<br/>
     * @return モデル名
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * モデル・インスタンスを返却します。<br/>
     * @return モデル・インスタンス
     */
    public Object getModel() {
        return model;
    }

    /**
     * リクエスト・パラメータのバインディング結果を管理する{@link BindingReuslt}インスタンスを返却します。<br/>
     * @return {@link BindingReuslt}インスタンス
     */
    public BindingResult getBindingResult() {
        return bindingResult;
    }

    /**
     * <p>
     * 例外発生時のアクションを表現する注釈です。</br>
     * 例外のタイプに応じたディスパッチ処理、及び、URLに設定するパラメータを指定します。
     * 注釈{@link org.springframework.web.bind.annotation.ModelAttribute}が指定されたビーンは自動的にディスパッチ先の処理に引き継がれるため、
     * パラメータの指定はディスパッチ処理でリダイレクト選択時に有効に動作します。
     * </p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    public @interface Action {

        /**
         * ディスパッチURLを指定します。
         */
        String value();

        /**
         * {@link Action}注釈によって定義されたアクションのトリガーとなる例外クラスを指定します。<br/>
         * ポストバックのデフォルト値はBindExceptionを設定し、
         * type属性が省略された場合は入力値検証に失敗したときの遷移先になります。
         * @return
         */
        Class<? extends Throwable> type() default BindException.class;

        /**
         * ディスパッチを行う際に引き継ぐパラメータを指定します。<br/>
         */
        String[] params() default "";
        
        /**
         * ディスパッチを行う際に引き継ぐパスパラメータを指定します
         */
        String[] pathParams() default "";
        
        /**
         * バリデーションエラー時にメッセージテンプレートを利用するかどうかを指定します。
         */
        boolean template() default true;

        /**
         * {@link Action}の配列を指定します。<br/>
         * コントローラのハンドラ―・メソッドに対して複数のアクションを定義する際に利用します。
         */
        @Target({
            ElementType.METHOD,
        })
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        @interface List {
            Action[] value();
        }
    }
}
