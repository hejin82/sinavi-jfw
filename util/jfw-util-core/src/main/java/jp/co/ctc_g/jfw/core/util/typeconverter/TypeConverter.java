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

package jp.co.ctc_g.jfw.core.util.typeconverter;

/**
 * <p>
 * このインタフェースは、型変換クラスが実装するそれです。
 * このインタフェース自体は抽象メソッドを持たず、マーカインタフェースとして機能します。
 * </p>
 * <p>
 * このインタフェースを実装したクラスは、以下のメソッドを、
 * 変換元となる型の数だけ実装する必要があります。
 * </p>
 * <pre>
 * public class FooConverter implements TypeConverter&lt;Foo&gt; {
 *
 *     public Foo convert(String value) {
 *         return new Foo(value);
 *     }
 *
 *     public Foo convert(Integer value) {
 *         return new Foo(vlaue.toString());
 *     }
 *
 *     ...
 * }
 *
 * </pre>
 * <p>
 * 上記のように、変換したい型を引数としてconvertという名称のメソッドを作成します。
 * </p>
 * @param <T> このコンバータの変換先型
 * @author ITOCHU Techno-Solutions Corporation.
 */
public interface TypeConverter<T> {
}
