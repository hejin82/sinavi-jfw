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
 * このパッケージは、リクエストデータをドメインにバインドする際に利用する{@link java.beans.PropertyEditor}の具象実装を提供します。<br/>
 * {@code J-Framework}では、利用頻度が高いと想定される下記の{@link java.beans.PropertyEditor}を提供しています。
 * <ul>
 *     <li>{@link HalfwidthDecimalEditor}</li>
 *     <li>{@link HalfwidthNumberWithCommaEditor}</li>
 *     <li>{@link HalfAndFullwidthNumberEditor}</li>
 *     <li>{@link HalfAndFullwidthNumberWithCommaEditor}</li>
 *     <li>{@link HalfwidthDecimalEditor}</li>
 *     <li>{@link HalfwidthDecimalWithCommaEditor}</li>
 *     <li>{@link HalfAndFullwidthDecimalEditor}</li>
 *     <li>{@link HalfAndFullwidthDecimalWithCommaEditor}</li>
 * </ul> 
 * これらの{@link java.beans.PropertyEditor}を用いたリクエストデータのバインド処理は{@code J-Framework}の内部で暗黙的に
 * 行われます。実装者は、これらの{@link java.beans.PropertyEditor}をフレームワークに設定することが主な責務となります。<br/>
 * デフォルトの状態では、{@link JseDefaultNumberPropertyEditorRegistrar}、および、{@link JseDefaultDatePropertyEditorRegistrar}によって
 * いくつかの{@link java.beans.PropertyEditor}が登録されています。これらのデフォルト設定は対応するプロパティの型すべてにマッピングされており
 * 有効に動作するようになっています。デフォルトとして設定される{@link java.beans.PropertyEditor}などの情報を確認するには各APIマニュアルを参照して下さい。
 * <p>
 * <h2>{@link java.beans.PropertyEditor}の設定方法</h2>
 * <p>
 * 前述のデフォルト設定によって要件が満たされない場合、個別に{@link java.beans.PropertyEditor}の設定を行う必要があります。
 * 設定方法は適応するスコープによって以下の２つの方法から選択します。
 * </p>
 * <h3>アプリケーション全体に適応する設定を行う場合</h3>
 * <p>
 * ドメインのプロパティの型から一意に{@link java.beans.PropertyEditor}の具象実装を特定できる場合に利用する設定方法です。
 * {@code J-Framework}が提供する{@link JseDefaultNumberPropertyEditorRegistrar}、および、{@link JseDefaultDatePropertyEditorRegistrar}
 * のように{@link PropertyEditorRegistrar#registerCustomEditors(org.springframework.beans.PropertyEditorRegistry)}を実装し
 * アプリケーション全体に適応する型と{@link java.beans.PropertyEditor}のマッピングを設定します。<br/>
 * </p>
 * <h3>コントローラの処理単位に設定を行う場合</h3>
 * <p>
 * コントローラ内の{@link org.springframework.web.bind.annotation.InitBinder}で注釈されたメソッドでプロパティ・エディタを設定することにより該当のコントローラが提供する処理に適したバインディングを行うことができます。<br/>
 * 設定は{@code NumberEditor}のインスタンスを生成し、{@link WebDataBinder#registerCustomEditor(Class<?>, String, PropertyEditor)}を利用して{@link WebBinder}にレジストしるこにより行います。<br/>
 * この設定は該当のコントーラに対するリクエスト全てにおいて有効になります。{@link NumberEditor}のインスタンス生成時に第１引数に指定した型のプロパティ 全てに対してプロパティ・エディタが有効になります。<br/>
 * 設定例を以下に示します。<br/>
 * この例の場合は、{@code FooController}に対するリクエストにて{@link BigDecimal}型プロパティに対するバインディングが必要とされた場合、すべてにおいて設定したプロパティ・エディタが有効になります。
 * </p>
 * <pre>
 * &#64;Controller
 * public class FooController {
 *
 *   &#64;InitBinder
 *   public void initBinderForm(WebDataBinder binder) {
 *       NumberEditor numberEditor = new NumberEditor(BigDecimal.class, true);
 *       binder.registerCustomEditor(BigDecimal.class, numberEditor);
 *   }
 * </pre>
 * <p>
 * また、特定プロパティに対してのみプロパティ・エディタを設定したい場合は、{@link org.springframework.web.bind.WebDataBinder#registerCustomEditor(Class, String, java.beans.PropertyEditor)}を利用してプロパティ・エディタを
 * レジストすることにり第2引数で指定したプロパティに対するバインディング時のみにプロパティ・エディタを有効にすることができます。
 * </p>
 */
package jp.co.ctc_g.jse.core.util.web.beans;
