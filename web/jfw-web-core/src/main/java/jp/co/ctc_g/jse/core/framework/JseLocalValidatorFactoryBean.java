package jp.co.ctc_g.jse.core.framework;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.validation.metadata.ConstraintDescriptor;

import jp.co.ctc_g.jfw.core.internal.Config;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.internal.WebCoreInternals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

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
 * PostBack の例外ハンドリング時にメッセージを解決します。
 * </p>
 * @see LocalValidatorFactoryBean
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class JseLocalValidatorFactoryBean extends LocalValidatorFactoryBean {
    
    private static final Logger L = LoggerFactory.getLogger(JseLocalValidatorFactoryBean.class);
    private static final ResourceBundle R = InternalMessages.getBundle(JseLocalValidatorFactoryBean.class);
    
    private static final String[] DOMAIN_SUFFIXES;
    static {
        Config c = WebCoreInternals.getConfig(JseLocalValidatorFactoryBean.class);
        String domainSuffix = c.find("domainSuffix");
        DOMAIN_SUFFIXES = Strings.split(",", domainSuffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object[] getArgumentsForConstraint(final String objectName, String field, ConstraintDescriptor<?> descriptor) {
        Object[] args = super.getArgumentsForConstraint(objectName, field, descriptor);
        int index = 0;
        for (Object arg : args) {
            if (arg instanceof DefaultMessageSourceResolvable) {
                args[index] = getMessageSourceResolvable(objectName, field);
            }
            index++;
        }
        return args;
    }

    /**
     * オブジェクト名、フィールド名からメッセージ解決のための{@link DefaultMessageSourceResolvable}のインスタンスを生成します。
     * @param objectName オブジェクト名
     * @param field フィールド
     * @return メッセージ解決のための{@link MessageSourceResolvable}インスタンス
     */
    protected MessageSourceResolvable getMessageSourceResolvable(final String objectName, String field) {
        String domain = getDomainName(objectName);
        List<String> codes = new LinkedList<String>();
        codes.add(objectName + Errors.NESTED_PATH_SEPARATOR + field);
        if (domain.equals(objectName)) {
            for (String suffix : DOMAIN_SUFFIXES) {
                codes.add(objectName + suffix + Errors.NESTED_PATH_SEPARATOR + field);
            }
        } else {
            codes.add(domain + Errors.NESTED_PATH_SEPARATOR + field);
            String[] filtered = Collections2.filter(Lists.newArrayList(DOMAIN_SUFFIXES), new Predicate<String>() {
                @Override
                public boolean apply(String suffix) {
                    return !objectName.endsWith(suffix) ? true : false;
                }
            }).toArray(new String[1]);
            for (String suffix : filtered) {
                codes.add(domain + suffix + Errors.NESTED_PATH_SEPARATOR + field);
            }
        }
        codes.add(field);
        if (L.isDebugEnabled()) L.debug(R.getString("D-LOCALBEAN#0001"), new Object[] {codes});
        return new DefaultMessageSourceResolvable(codes.toArray(new String[1]), field);
    }

    /**
     * オブジェクト名からドメイン名を推測してドメイン名を返します。
     * もし、指定されたサフィックスに該当するものがなければオブジェクト名をドメイン名とします。
     * @param objectName オブジェクト名
     * @return ドメイン名
     */
    protected String getDomainName(String objectName) {
        for (String suffix : DOMAIN_SUFFIXES) {
            if (objectName.endsWith(suffix)) {
                return objectName.replace(suffix, "");
            }
        }
        return objectName;
    }

}
