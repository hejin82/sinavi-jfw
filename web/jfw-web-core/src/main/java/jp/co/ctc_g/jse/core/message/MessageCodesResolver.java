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
package jp.co.ctc_g.jse.core.message;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.resource.MessageSourceLocator;
import jp.co.ctc_g.jfw.core.resource.Rs;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.DefaultMessageCodesResolver;

/**
 * <p>
 * このクラスは、バリデーションエラー発生時にドメインのプロパティ名からメッセージを取得します。
 * </p>
 * <p>
 * 例えば、<code>Hoge</code>と<code>HogeCriteria</code>というクラスがあり、
 * それぞれ、同様の<code>id</code>というプロパティが存在するときに
 * 検索と更新系のそれぞれでバリデーションエラーが発生したとします。
 * このときにプロパティファイルにそれぞれのメッセージを設定すると冗長な設定となり、
 * 名称変更時の修正の手間が増えます。
 * このような問題を解決するためにこのクラスを提供しています。
 * </p>
 * <p>
 * 以下に設定例を示します。
 * <pre>
 * &lt;bean id="messageCodesResolver" class="jp.co.ctc_g.jse.core.message.MessageCodesResolver"&gt;
 *  &lt;property name="messageCodeFormatter" ref="formatter" /&gt;
 * &lt;/bean&gt;
 * &lt;bean id="formatter" class="jp.co.ctc_g.jse.core.message.MessageCodesFormat"/&gt;
 * &lt;bean id="configurableWebBindingInitializer" class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer"&gt;
 *  &lt;property name="validator" ref="validator" /&gt;
 *  &lt;property name="messageCodesResolver" ref="messageCodesResolver" /&gt;
 *  &lt;property name="conversionService" ref="conversionService" /&gt;
 *  &lt;property name="bindingErrorProcessor" ref="bindingErrorProcessor" /&gt;
 *  &lt;property name="propertyEditorRegistrars"&gt;
 *   &lt;list&gt;
 *    &lt;ref bean="defaultDatePropertyEditorRegistrar"/&gt;
 *    &lt;ref bean="defaultNumberPropertyEditorRegistrar"/&gt;
 *   &lt;/list&gt;
 *  &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 * <p>
 * メッセージコードを設定する際は以下のサフィックスを付与し、
 * 複数のメッセージコードを生成します。
 * デフォルトでは、[Criteria/Selection]というサフィックスが登録されています。
 * Hogeクラスのidプロパティにバリデーションエラーが発生したときは
 * <ul>
 *  <li>HogeCriteria.id</li>
 *  <li>HogeSelection.id</li>
 *  <li>Hoge.id</li>
 *  <li>id</li>
 * </ul>
 * という順番でリソース解決することになります。
 * HogeCriteriaクラスのidプロパティにバリデーションエラーが発生したときは
 * <ul>
 *  <li>Hoge.id</li>
 *  <li>HogeSelection.id</li>
 *  <li>HogeCriteria.id</li>
 *  <li>id</li>
 * </ul>
 * となります。
 * </p>
 * <p>
 * バリデーションエラー発生時はコードの解決のみを実施し、
 * JSPのメッセージを表示するタイミングで
 * プロパティファイルからメッセージを取得します。
 * </p>
 * @see DefaultMessageCodesResolver
 * @see MessageCodesFormat
 * @see SpringValidatorAdapter
 * @see LocalValidatorFactoryBean
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class MessageCodesResolver extends DefaultMessageCodesResolver {

    private static final long serialVersionUID = 1L;

    private static final Logger L = LoggerFactory.getLogger(MessageCodesResolver.class);

    private static final ResourceBundle R = InternalMessages.getBundle(MessageCodesResolver.class);

    private static final String[] DOMAIN_SUFFIXES;
    static {
        Config c = WebCoreInternals.getConfig(MessageCodesResolver.class);
        String domainSuffix = c.find("domainSuffix");
        DOMAIN_SUFFIXES = Strings.split(",", domainSuffix);
    }

    /**
     * デフォルトコンストラクタです。
     */
    public MessageCodesResolver() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {
        List<String> codes = new LinkedList<String>();
        for (String suffix : DOMAIN_SUFFIXES) {
            String name = objectName + suffix;
            if (objectName.endsWith(suffix)) {
                name = objectName.replace(suffix, "");
            }
            codes.add(postProcessMessageCode(Format.toDelimitedString(name, field)));
        }
        codes.addAll(Arrays.asList(super.resolveMessageCodes(errorCode, objectName, field, fieldType)));
        return StringUtils.toStringArray(codes);
    }
    
    /**
     * プロパティ名を解決します。
     * @param key リソース検索キー
     * @return プロパティ名
     */
    protected static String resolve(String key) {
        String msg = Rs.find(key);
        if (key.equals(msg)) {
            if (DOMAIN_SUFFIXES != null) {
                for (String suffix : DOMAIN_SUFFIXES) {
                    String replaceKey = key.replace(suffix, "");
                    try {
                        msg = find(replaceKey);
                    } catch (NoSuchMessageException e) {
                        L.debug(Strings.substitute(R.getString("D-MESSAGE_RESOLVER#0001"), Maps.hash("replaceKey", replaceKey)));
                        continue;
                    }
                }
            }
        }
        return msg;
    }

    private static String find(String key) throws NoSuchMessageException {
        if (Strings.isEmpty(key)) return "";
        Locale currentLocale = LocaleContextHolder.getLocale();
        return MessageSourceLocator.get().getMessage(key, null, currentLocale);
    }

}
