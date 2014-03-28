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
package jp.co.ctc_g.jfw;

/**
 * <p>
 * フレームワークの定数設定を扱うユーティリティクラスです。
 * </p>
 * <p>
 * 下記一覧のシステムプロパティを取得する為のメソッドを持ちます。記述形式は「キー : 値の説明」です。
 * </p>
 * <ul>
 * <li>jfw.home : フレームワークのホームディレクトリ。{@link #home()}メソッドから取得できます。</li>
 * </ul>
 * @author ITOCHU Techno-Solutions Corporation
 */
public final class Framework {


    /**
     * J-FWのホームディレクトリを示すシステムプロパティのキーです。
     */
    public static final String HOME_KEY = "jfw.home";

    /**
     * <p>
     * コンストラクタです。
     * </p>
     */
    private Framework() {}

    /**
     * <p>
     * キー[jfw.home]からフレームワークのホームディレクトリを決定して返却します。
     * </p>
     * <p>
     * -Dオプションによってこのキーにマッピングされる値が示された場合は、そちらを利用しますが、
     * 示されていない場合は、「ユーザーホーム + &quot/.jfw;&quot;」をデフォルトのホームディレクトリにします。
     * </p>
     * @return フレームワークのホームディレクトリ
     */
    public static String home() {
        String home = System.getProperty(Framework.HOME_KEY);
        if (home == null) {
            String userHome = System.getProperty("user.home");
            home = new StringBuffer().append(userHome).append("/.jfw").toString();
            System.setProperty(Framework.HOME_KEY, home);
        }
        return home;
    }
}
