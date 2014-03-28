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
 * このパッケージは、JDBCに関する機能を提供します。
 * </p>
 * <p>
 * J-FrameworkはデフォルトのJDBCアクセスライブラリとして、
 * MyBatisを利用しています。
 * このパッケージを利用することで、以下の点でMyBatisの機能を拡張することができます。
 * <ul>
 * <li>DB特化のSQL(ROWNUM, ROW_NUMBER(), LIMIT, OFFSET, TOPなど)によるページングをサポート</li>
 * <li>ページングタグライブラリとの親和性向上</li>
 * </ul>
 * </p>
 */
package jp.co.ctc_g.jfw.core.jdbc;