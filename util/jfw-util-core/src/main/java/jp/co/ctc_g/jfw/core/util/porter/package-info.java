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
 * このパッケージは、あるオブジェクトからあるオブジェクトへ&quot;値&quot;を移送するための機能を提供します。
 * </p>
 * <p>
 * J-Frameworkは、アプリケーションを厳密に階層化することで保守性や拡張性に貢献しますが、
 * それにより各レイヤにおいてのトランスファーオブジェクトがレイヤ境界により制約を受けます。
 * 具体的には、プレゼンテーションレイヤのトランスファーオブジェクトであるフォームオブジェクトは、
 * 直接ビジネスレイヤに渡すことはできません。必ずドメインに詰め替える必要があります。
 * さらに、インテグレーションレイヤにおいて発行されたSQLの結果も、直接ビジネスレイヤに渡すことはできず、
 * これもまたドメインに詰め替える必要があります。
 * このような項目を移し替えるだけの処理を1つ1つ記述することは退屈でくだらない作業です。
 * </p>
 * <p>
 * J-Frameworkは、このような単純作業に煩わされないように、&quot;値&quot;の移送ユーティリティを提供しています。
 * それが、このporterパッケージです。
 * porterパッケージは容易に利用することができるようにAPIが設計されています。
 * BeanからBeanへ値を転送したいのであれば、
 * {@link jp.co.ctc_g.jfw.core.util.porter.BeanPorter BeanPorter}がきっと役に立ってくれるでしょう。
 * 使い方はいたってシンプルです。その単純明快な利用方法の詳細は
 * {@link jp.co.ctc_g.jfw.core.util.porter.BeanPorter BeanPorter}を参照してください。
 * 一方で、porterパッケージは、その単純なAPIとは裏腹に、
 * 単純で静的なユーティリティではなく複雑で動的なユーティリティです。
 * なぜなら、あるオブジェクトからあるオブジェクトへの値移送という命題を可能な限りの拡張性を確保して実装されているからです。
 * 内部は完全にカスタマイズ可能なように設計され、{@link jp.co.ctc_g.jfw.core.util.porter.Porter Porter}クラスは、
 * その中心的な役割を演じます。
 * </p>
 * <p>
 * porterパッケージは、「値移送」をいくつかのプロセスに分割し、
 * それぞれを変更可能なストラテジ（Strategy）として位置付けています。
 * 移送元から値を逐次的に取り出すストラテジは、
 * {@link jp.co.ctc_g.jfw.core.util.porter.SourceStrategy}として、
 * 移送先に値を逐次的に代入するストラテジは、
 * {@link jp.co.ctc_g.jfw.core.util.porter.DestinationStrategy}として
 * 定義され、移送元の型や移送先の型を自由に組み合わせることができます。
 * また、これら2つのストラテジの間には複数のフィルタを設置することができます。
 * これらのフィルタは、{@link jp.co.ctc_g.jfw.core.util.porter.ManipulationFilter}
 * として定義されています。これにより、移送元と移送先の間で、
 * 移送値や移送識別子に変更を加えることができようになります。
 * </p>
 * <p>
 * porterパッケージに含まれる全てのクラスは特に明記されていない限り、
 * <strong>複数スレッドからのアクセスに対して安全ではありません</strong>。
 * よって、このパッケージに属するクラスをスレッドセーフにするべきクラスのフィールドに
 * 直接保持することは推奨しません。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
package jp.co.ctc_g.jfw.core.util.porter;