---
layout: page
category : manual
tags : [入力値検証]
title: "どのような入力値検証がある？"
---

SINAV J-Frameworkは以下のようなバリデータを提供しています。

<table class="table table-bordered table-hover">
  <thead>
    <tr>
      <th width="5%">#</th>
      <th>バリデータ</th>
      <th>概要</th>
      <th width="10%">関連API</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1.</td>
      <td>必須入力バリデータ</td>
      <td>検証対象のプロパティがNULL、もしくはブランク文字列ではないことかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Required.html">Required</a>
      </td>
    </tr>
    <tr>
      <td>2.</td>
      <td>正規表現バリデータ</td>
      <td>検証対象のプロパティが指定された正規表現にマッチするかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Regex.html">Regex</a>
      </td>
    </tr>
    <tr>
      <td>3.</td>
      <td>日付バリデータ</td>
      <td>検証対象のプロパティが指定された書式に従った日付であるかどうかを検証します。 </td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/DateFormat.html">DateFormat</a>
      </td>
    </tr>
    <tr>
      <td>4.</td>
      <td>小数バリデータ</td>
      <td>検証対象のプロパティが小数かつ指定された有効桁数・小数点桁数以内かどうかを検証します。 </td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Decimal.html">Decimal</a>
      </td>
    </tr>
    <tr>
      <td>5.</td>
      <td>整数バリデータ</td>
      <td>検証対象のプロパティが整数かつ指定された有効桁数以内かどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Number.html">Number</a>
      </td>
    </tr>
    <tr>
      <td>6.</td>
      <td>数値フォーマットバリデータ</td>
      <td>検証対象のプロパティが数値フォーマットかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/NumericFormat.html">NumericFormat</a>
      </td>
    </tr>
    <tr>
      <td>7.</td>
      <td>最小文字数バリデータ</td>
      <td>検証対象のプロパティの長さが指定したサイズと等しいか、それよりも大きいかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/MinLength.html">MinLength</a>
      </td>
    </tr>
    <tr>
      <td>8.</td>
      <td>最大文字数バリデータ</td>
      <td>検証対象のプロパティの長さが指定したサイズと等しいか、それよりも小さいかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/MaxLength.html">MaxLength</a>
      </td>
    </tr>
    <tr>
      <td>9.</td>
      <td>最小バイト数バリデータ</td>
      <td>検証対象のプロパティのバイト長が指定したサイズ以上かどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/MinByteLength.html">MinByteLength</a>
      </td>
    </tr>
    <tr>
      <td>10.</td>
      <td>最大バイト数バリデータ</td>
      <td>検証対象のプロパティのバイト長が指定したサイズ以内かどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/MaxByteLength.html">MaxByteLength</a>
      </td>
    </tr>
    <tr>
      <td>11.</td>
      <td>英字バリデータ</td>
      <td>検証対象のプロパティがアルファベットであるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Alphabet.html">Alphabet</a>
      </td>
    </tr>
    <tr>
      <td>12.</td>
      <td>英数字バリデータ</td>
      <td>検証対象のプロパティがアルファベットもしくはアラビア数字であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Alphameric.html">Alphameric</a>
      </td>
    </tr>
    <tr>
      <td>13.</td>
      <td>ASCIIバリデータ</td>
      <td>検証対象のプロパティがASCII印字可能文字であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/ASCII.html">ASCII</a>
      </td>
    </tr>
    <tr>
      <td>14.</td>
      <td>全角バリデータ</td>
      <td>検証対象のプロパティがいわゆる全角文字列であるかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Zenkaku.html">Zenkaku</a>
      </td>
    </tr>
    <tr>
      <td>15.</td>
      <td>ひらがなバリデータ</td>
      <td>検証対象のプロパティがひらがなであるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Hiragana.html">Hiragana</a>
      </td>
    </tr>
    <tr>
      <td>16.</td>
      <td>カタカナバリデータ</td>
      <td>検証対象のプロパティがカタカナであるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Katakana.html">Katakana</a>
      </td>
    </tr>
    <tr>
      <td>17.</td>
      <td>半角カタカナバリデータ</td>
      <td>検証対象のプロパティが半角カタカナであるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/HalfwidthKatakana.html">HalfwidthKatakana</a>
      </td>
    </tr>
    <tr>
      <td>18.</td>
      <td>郵便番号バリデータ</td>
      <td>検証対象のプロパティが郵便番号であるかを検証します。 </td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/ZipCode.html">ZipCode</a>
      </td>
    </tr>
    <tr>
      <td>19.</td>
      <td>電子メールアドレスバリデータ</td>
      <td>検証対象のプロパティが電子メールアドレス形式であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Email.html">Email</a>
      </td>
    </tr>
    <tr>
      <td>20.</td>
      <td>IPv4アドレスバリデータ</td>
      <td>検証対象のプロパティがIPv4アドレスであるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/IPv4.html">IPv4</a>
      </td>
    </tr>
    <tr>
      <td>21.</td>
      <td>クレジットカード番号バリデータ</td>
      <td>検証対象のプロパティがクレジットカード番号が適切であるかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/CreditCard.html">CreditCard</a>
      </td>
    </tr>
    <tr>
      <td>22.</td>
      <td>日付比較バリデータ-前</td>
      <td>検証対象のプロパティが指定された他のプロパティの値よりも日付として「前」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Before.html">Before</a>
      </td>
    </tr>
    <tr>
      <td>23.</td>
      <td>日付比較バリデータ-前</td>
      <td>検証対象のプロパティが指定された固定値よりも日付として「前」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedBefore.html">FixedBefore</a>
      </td>
    </tr>
    <tr>
      <td>24.</td>
      <td>日付比較バリデータ-後</td>
      <td>検証対象のプロパティが指定された他のプロパティの値よりも日付として「後」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/After.html">After</a>
      </td>
    </tr>
    <tr>
      <td>25.</td>
      <td>日付比較バリデータ-後</td>
      <td>検証対象のプロパティが指定された固定値よりも日付として「後」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedAfter.html">FixedAfter</a>
      </td>
    </tr>
    <tr>
      <td>26.</td>
      <td>日付比較バリデータ-以前</td>
      <td>検証対象のプロパティが指定されたプロパティの値よりも日付として「前」あるいは「同じ」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/BeforeEqualsTo.html">BeforeEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>27.</td>
      <td>日付比較バリデータ-以前</td>
      <td>検証対象のプロパティが指定された固定値よりも日付として「前」あるいは「同じ」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedBeforeEqualsTo.html">FixedBeforeEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>28.</td>
      <td>日付比較バリデータ-以後</td>
      <td>検証対象のプロパティが指定されたプロパティの値よりも日付として「後」あるいは「同じ」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/AfterEqualsTo.html">AfterEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>29.</td>
      <td>日付比較バリデータ-以後</td>
      <td>検証対象のプロパティが指定された固定値よりも日付として「後」あるいは「同じ」であるかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedAfterEqualsTo.html">FixedAfterEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>30.</td>
      <td>等価性バリデータ</td>
      <td>検証対象のプロパティが指定されたプロパティの値との等価性を検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/EqualsTo.html">EqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>31.</td>
      <td>等価性バリデータ</td>
      <td>検証対象のプロパティが指定された固定値との等価性を検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedEqualsTo.html">FixedEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>32.</td>
      <td>文字数一致バリデータ</td>
      <td>検証対象のプロパティの長さが指定したサイズと等しいかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/EqualsLength.html">EqualsLength</a>
      </td>
    </tr>
    <tr>
      <td>33.</td>
      <td>バイト数一致バリデータ</td>
      <td>検証対象のプロパティのバイト長が指定したサイズと等しいかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/EqualsByteLength.html">EqualsByteLength</a>
      </td>
    </tr>
    <tr>
      <td>34.</td>
      <td>数値比較バリデータ-未満 </td>
      <td>検証対象のプロパティが指定されたプロパティの値よりも小さいかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/LessThan.html">LessThan</a>
      </td>
    </tr>
    <tr>
      <td>35.</td>
      <td>数値比較バリデータ-未満 </td>
      <td>検証対象のプロパティが指定された固定値よりも小さいかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedLessThan.html">FixedLessThan</a>
      </td>
    </tr>
    <tr>
      <td>36.</td>
      <td>数値比較バリデータ-超</td>
      <td>検証対象のプロパティが指定されたプロパティの値よりも大きいかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/GreaterThan.html">GreaterThan</a>
      </td>
    </tr>
    <tr>
      <td>37.</td>
      <td>数値比較バリデータ-超</td>
      <td>検証対象のプロパティが指定された固定値よりも大きいかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedGreaterThan.html">FixedGreaterThan</a>
      </td>
    </tr>
    <tr>
      <td>38.</td>
      <td>数値比較バリデータ-以下</td>
      <td>検証対象のプロパティが指定された他のプロパティの値よりも小さいかあるいは同じかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/LessThanEqualsTo.html">LessThanEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>39.</td>
      <td>数値比較バリデータ-以下</td>
      <td>検証対象のプロパティが指定された固定値よりも小さいかあるいは同じかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedLessThanEqualsTo.html">FixedLessThanEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>40.</td>
      <td>数値比較バリデータ-以上</td>
      <td>検証対象のプロパティが指定された他のプロパティの値よりも大きいかあるいは同じかどうかを検証します。 </td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/GreaterThanEqualsTo.html">GreaterThanEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>41.</td>
      <td>数値比較バリデータ-以上</td>
      <td>検証対象のプロパティが指定された固定値よりも大きいかあるいは同じかどうかを検証します。 </td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/FixedGreaterThanEqualsTo.html">FixedGreaterThanEqualsTo</a>
      </td>
    </tr>
    <tr>
      <td>42.</td>
      <td>サイズ比較バリデータ-等価</td>
      <td>検証対象のプロパティの配列またはコレクションの要素数が指定したサイズと等しいかどうかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/EqualsSize.html">EqualsSize</a>
      </td>
    </tr>
    <tr>
      <td>43.</td>
      <td>サイズ比較バリデータ-最大</td>
      <td>検証対象のプロパティの配列またはコレクションの要素数が指定したサイズと等しいか、それよりも小さいかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/MaxSize.html">MaxSize</a>
      </td>
    </tr>
    <tr>
      <td>44.</td>
      <td>サイズ比較バリデータ-最小</td>
      <td>検証対象のプロパティの配列またはコレクションの要素数が指定したサイズと等しいか、それよりも大きいかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/MinSize.html">MinSize</a>
      </td>
    </tr>
    <tr>
      <td>44.</td>
      <td>必須入力バリデータ-配列/コレクション</td>
      <td>検証対象のプロパティの配列またはコレクションの要素がnull、もしくはブランク文字列ではないかを検証します。</td>
      <td>
        <a href="{{ site.baseurl}}docs/projects/jfw-validation-core/{{ site.jfw-validation-core.version }}/api/jp/co/ctc_g/jse/core/validation/constraints/Requireds.html">Requireds</a>
      </td>
    </tr>
  </tbody>
</table>