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

/**
 * <p>
 * このパッケージは、例外クラスを提供します。
 * </p>
 * <h4>J-Frameworkの例外クラスについて</h4>
 * <p>
 * このパッケージが提供する3つの例外クラスは、
 * 開発者が自身の作成するアプリケーションで利用することを想定しています。
 * 3つのクラスの使い分けは、名称が示す通り、
 * アプリケーションが回復可能である場合と、アプリケーション・システムが回復不可能である場合に使い分けます。
 * </p>
 * <ol>
 * <li>{@link jp.co.ctc_g.jfw.core.exception.ApplicationRecoverableException}はアプリケーションが回復可能である場合に利用</li>
 * <li>{@link jp.co.ctc_g.jfw.core.exception.ApplicationUnrecoverableException}はアプリケーションが回復不能である場合に利用</li>
 * <li>{@link jp.co.ctc_g.jfw.core.exception.SystemException}はシステムが回復不能である場合</li>
 * </ol>
 * <p>
 * J-Frameworkではこれらの例外をすべて非チェック例外としています。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
package jp.co.ctc_g.jfw.core.exception;