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
 * このクラスは、プリミティブを利用する際のユーティリティを提供します。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Primitives {

    private Primitives() {
    }

    /**
     * 指定されたプリミティブの配列を、対応するラッパークラスの配列に変換します。
     * @param primitives プリミティブの配列
     * @return ラッパークラスの配列
     */
    public static Character[] wrap(final char[] primitives) {
        if (primitives == null) return null;
        if (primitives.length == 0) return new Character[0];
        return Arrays.gen(primitives.length, new GenCall<Character>() {
            public Character gen(int index, int size) {
                return new Character(primitives[index]);
            }
        });
    }

    /**
     * 指定されたプリミティブの配列を、対応するラッパークラスの配列に変換します。
     * @param primitives プリミティブの配列
     * @return ラッパークラスの配列
     */
    public static Integer[] wrap(final int[] primitives) {
        if (primitives == null) return null;
        if (primitives.length == 0) return new Integer[0];
        return Arrays.gen(primitives.length, new GenCall<Integer>() {
            public Integer gen(int index, int size) {
                return new Integer(primitives[index]);
            }
        });
    }

    /**
     * 指定されたプリミティブの配列を、対応するラッパークラスの配列に変換します。
     * @param primitives プリミティブの配列
     * @return ラッパークラスの配列
     */
    public static Float[] wrap(final float[] primitives) {
        if (primitives == null) return null;
        if (primitives.length == 0) return new Float[0];
        return Arrays.gen(primitives.length, new GenCall<Float>() {
            public Float gen(int index, int size) {
                return new Float(primitives[index]);
            }
        });
    }

    /**
     * 指定されたプリミティブの配列を、対応するラッパークラスの配列に変換します。
     * @param primitives プリミティブの配列
     * @return ラッパークラスの配列
     */
    public static Double[] wrap(final double[] primitives) {
        if (primitives == null) return null;
        if (primitives.length == 0) return new Double[0];
        return Arrays.gen(primitives.length, new GenCall<Double>() {
            public Double gen(int index, int size) {
                return new Double(primitives[index]);
            }
        });
    }

    /**
     * 指定されたプリミティブの配列を、対応するラッパークラスの配列に変換します。
     * @param primitives プリミティブの配列
     * @return ラッパークラスの配列
     */
    public static Byte[] wrap(final byte[] primitives) {
        if (primitives == null) return null;
        if (primitives.length == 0) return new Byte[0];
        return Arrays.gen(primitives.length, new GenCall<Byte>() {
            public Byte gen(int index, int size) {
                return new Byte(primitives[index]);
            }
        });
    }

    /**
     * 指定されたプリミティブの配列を、対応するラッパークラスの配列に変換します。
     * @param primitives プリミティブの配列
     * @return ラッパークラスの配列
     */
    public static Long[] wrap(final long[] primitives) {
        if (primitives == null) return null;
        if (primitives.length == 0) return new Long[0];
        return Arrays.gen(primitives.length, new GenCall<Long>() {
            public Long gen(int index, int size) {
                return new Long(primitives[index]);
            }
        });
    }

}
