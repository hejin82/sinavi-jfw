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

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * <p>
 * トークIDの生成器のAPIを規定するインタフェースです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface IDGenerator {

    /**
     * トークンIDを生成します。 
     * @return トークンID
     */
    String generate();

    /**
     * <p>
     * デフォルトとして設定される{@link IDGenerator}インタフェースの実装です。
     * </p>
     * <p>
     * {@link IDGenerator}インタフェースの実装を切り替える方法については、{@link TokenManager}のAPIマニュアルを参照して下さい。
     * </p>
     * @author ITOCHU Techno-Solutions Corporation.
     * @see TokenManager
     */
    class DefaultIdGenerator implements IDGenerator {

        private final AtomicInteger counter = new AtomicInteger(0);

        public String generate() {
            return DigestUtils.md5Hex(UUID.randomUUID().toString() + String.format("%08d", counter.getAndIncrement()));
        }
    }
}
