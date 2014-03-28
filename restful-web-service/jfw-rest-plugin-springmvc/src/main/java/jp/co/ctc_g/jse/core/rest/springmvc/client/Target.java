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

package jp.co.ctc_g.jse.core.rest.springmvc.client;

import java.net.URI;
import java.util.Map;
import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Args;

import org.springframework.web.util.UriTemplate;

/**
 * <p>
 * このクラスはリクエスト対象のURLを設定するユーティリティです。
 * </p>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public final class Target {

    private final URI url;
    private static final ResourceBundle R = InternalMessages.getBundle(Target.class); 

    /**
     * 文字列から{@link URI}を生成し、設定します。
     * @param url リクエスト対象のurl
     * @return このユーティリティを返します。
     */
    public static Target target(final String url) {
        return new Target(url);
    }

    /**
     * 文字列から{@link URI}を生成し、設定します。
     * 生成時に<code>/books/{id}</code>などを置換します。
     * @param url リクエスト対象のurl
     * @param urlVariables 置換する値
     * @return このユーティリティを返します。
     */
    public static Target target(final String url, final Object... urlVariables) {
        return new Target(url, urlVariables);
    }

    /**
     * 文字列から{@link URI}を生成し、設定します。
     * 生成時に<code>/books/{id}</code>などを置換します。
     * @param url リクエスト対象のurl
     * @param urlVariables 置換する値
     * @return このユーティリティを返します。
     */
    public static Target target(final String url, final Map<String, Object> urlVariables) {
        return new Target(url, urlVariables);
    }

    private Target(final String url, final Map<String, Object> urlVariables) {
        Args.checkNotNull(url, R.getObject("E-SPRINGMVC-REST-CLIENT#0001"));
        this.url = new UriTemplate(url).expand(urlVariables);
    }

    private Target(final String url, final Object... urlVariables) {
        Args.checkNotNull(url, R.getObject("E-SPRINGMVC-REST-CLIENT#0001"));
        this.url = new UriTemplate(url).expand(urlVariables);
    }

    /**
     * URIを取得します。
     * @return 置換済みのURI
     */
    public URI getUrl() {
        return url;
    }

}
