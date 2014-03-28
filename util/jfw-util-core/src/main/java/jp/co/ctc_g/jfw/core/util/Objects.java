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

package jp.co.ctc_g.jfw.core.util;

/**
 * <p>
 * このクラスはオブジェクトに関するユーティリティを提供します。
 * </p>
 */
public final class Objects {

    private Objects() {
    }

    /**
     * ふたつのオブジェクトを比較し、等しければtrueを返し、そうでなければfalseを返します。
     * もし、両方のオブジェクトがnullであればtrueを返し、片方だけがnullであればfalseを返します。
     * また、両方のオブジェクトは  {@link Object#equals(Object) } を利用して比較されます。
     *
     * @param a
     *            オブジェクト
     * @param b
     *            比較対象のオブジェクト
     * @return オブジェクトが等しいかどうか。
     *
     * @see Object#equals(Object)
     */
    public static boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else {
            return a.equals(b);
        }
    }
}
