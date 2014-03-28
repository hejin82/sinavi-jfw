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

package jp.co.ctc_g.jse.core.token;

/**
 * <p>
 * このクラスは{@link TokenProcessor}インタフェースの抽象的な実装を提供します。<br/>
 * 将来的にセッションスコープ以外のスコープでトークン制御を行う際に利用されることを想定しています。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see TokenProcessor
 * @see IDGenerator
 */
public abstract class AbstractTokenProcessor implements TokenProcessor {

    protected IDGenerator idGenerator;

    /**
     * デフォルトコンストラクタです。
     */
    public AbstractTokenProcessor() {}

    /**
     * コンストラクタです。 
     * @param idGenerator ID生成ジェネレータ
     */
    public AbstractTokenProcessor(IDGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}

