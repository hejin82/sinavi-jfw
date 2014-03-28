/*
 *  Copyright (c) 2013 ITOCHU Techno-Solutions Corporation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package jp.co.ctc_g.jse.core.framework;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * <p>
 * このクラスは{@link PostBack.Action} によって注釈されたコントローラのハンドラ・メソッドのアドバイスとして機能し、
 * {@link PostBack.Action} 注釈の属性として定義されたポストバック・アクションを実行します。<br/>
 * </p>
 * 
 * @author ITOCHU Techno-Solutions corporation.
 */
@Aspect
public class PostBackAroundAdvice {

    /**
     * デフォルトコンストラクタです。
     */
    public PostBackAroundAdvice() {}

    /**
     * ポストバックアクションを実行するためのインターセプタです。
     * @param joinPoint ポイント
     * @return 実行結果
     * @throws Throwable 予期しない例外
     */
    @Around("execution(@org.springframework.web.bind.annotation.RequestMapping * *(..)) && (execution(@jp.co.ctc_g.jse.core.framework.PostBack.Action * *(..)) || execution(@jp.co.ctc_g.jse.core.framework.PostBack.Action.List * *(..)))")
    public Object processPostBackAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Object model = null;
        try {
            PostBackManager manager = PostBackManager.getCurrentPostBackManager();
            Class<?> modelAttributeType = manager.getModelAttributeType();
            if (modelAttributeType != null) {
                for (Object o : joinPoint.getArgs()) {
                    if (o.getClass().getName().equals(modelAttributeType.getName())) {
                        model = o;
                    }
                }
            }
            result = joinPoint.proceed();
        } catch (Throwable t) {
            result = build(t, model);
        }
        return result;
    }

    /**
     * 例外に設定されているエラーメッセージをそれぞれのスコープに格納し、
     * PostBack.Actionアノテーションで指定されている遷移先の情報を取得します。
     * @param t 例外
     * @param model モデルオブジェクト
     * @return 遷移先
     * @throws Throwable 予期しない例外
     */
    protected Object build(Throwable t, Object model) throws Throwable {
        if (PostBackManager.isPostBackTargetException(t)) {
            PostBackManager.save(new PostBack(t));
            PostBackManager.saveMessage(t);
            return PostBackManager.buildUri(t, model, true);
        } else {
            throw t;
        }
    }
}
