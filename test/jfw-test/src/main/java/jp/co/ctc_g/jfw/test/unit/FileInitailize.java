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

package jp.co.ctc_g.jfw.test.unit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;

import org.apache.commons.io.IOUtils;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

/**
 * <p>
 * このクラスは、テスト時にファイルの内容を確認する際に指定されたファイルの内容をコピーします。
 * </p>
 * <p>
 * 読込対象のファイルはこのクラスのインスタンス生成時に指定されたクラスのクラスパスよりロードします。
 * つまり、テスト対象のクラスがjp/co/ctc_g/jfw/sample/FooServiceTestの場合は
 * jp/co/ctc_g/jfw/sampleにテスト用のファイルを配置されたものをロードします。
 * </p>
 * <p>
 * このクラスを利用した単純なテストの実施方法を以下に示します。
 * </p>
 * <pre class="brush:java">
 * public class FooServiceTest {
 *   &#64;Autowired
 *   protected FooService service;
 *   
 *   &#64;Rule
 *   public FileInitailize file = new FileInitailize(FooService.class);
 *   
 *   &#64;Test
 *   public void test() {
 *     File f = file.copy("test.csv", "temp.csv");
 *     String content = FileUtils.readFileToString(tmp, "MS932");
 *     assertThat(content, is(file.load("test.csv")));
 *   }
 * }
 * </pre>
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class FileInitailize extends TemporaryFolder {

    private static final Logger L = LoggerFactory.getLogger(FileInitailize.class);
    private static final ResourceBundle R = InternalMessages.getBundle(FileInitailize.class);
    private static final String DEFAULT_FILE_ENCODE = "MS932";
    private static final String URL_ENCODE = "UTF-8";
    private Class<?> target;
    private String encode = DEFAULT_FILE_ENCODE;

    /**
     * デフォルトコンストラクタです。
     */
    public FileInitailize() {}

    /**
     * コンストラクタです。
     * @param target 読込ファイルパスのターゲットとなるクラス
     */
    public FileInitailize(Class<?> target) {
        this.target = target;
    }

    /**
     * コンストラクタです。
     * @param target 読込ファイルパスのターゲットとなるクラス
     * @param encode ファイルエンコード
     */
    public FileInitailize(Class<?> target, String encode) {
        this(target);
        this.encode = encode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void before() throws Throwable {
        super.before();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void after() {
        super.after();
    }

    /**
     * 指定されたファイルのデータを指定されたファイルへコピーする処理です。
     * @param src コピー元ファイル名
     * @param dest コピー先ファイル名
     * @return ファイルのインスタンス
     */
    public File copy(String src, String dest) {
        L.debug(Strings.substitute(R.getString("D-TEST#0002"), Maps.hash("src", src).map("dest", dest)));
        InputStream is = null;
        File destFile = null;
        InternalException exception = null;
        try {
            is = target.getResourceAsStream(URLDecoder.decode(src, URL_ENCODE));
            String contents = IOUtils.toString(is, encode);
            destFile = super.newFile(dest);
            Files.write(contents, destFile, Charset.forName(encode));
        } catch (IOException e) {
            exception = new InternalException(FileInitailize.class, "E-TEST#0018");
        } finally {
            try {
                if (is != null) is.close();
            } catch(IOException e) {
                if (exception == null) {
                    exception = new InternalException(FileInitailize.class, "E-TEST#0019");
                }
            }
            if (exception != null) throw exception;
        }
        return destFile;
    }

    /**
     * ファイルのデータをロードします。
     * @param src ファイルパス
     * @return ファイルデータ
     */
    public String load(String src) {
        L.debug(Strings.substitute(R.getString("D-TEST#0003"), Maps.hash("src", src)));
        InputStream is = null;
        String content = "";
        InternalException exception = null;
        try {
            is = target.getResourceAsStream(URLDecoder.decode(src, URL_ENCODE));
            content = IOUtils.toString(is, encode);
        } catch (IOException e) {
            exception = new InternalException(FileInitailize.class, "E-TEST#0020");
        } finally {
            try {
                if (is != null) is.close();
            } catch(IOException e) {
                if (exception == null) {
                    exception = new InternalException(FileInitailize.class, "E-TEST#0019");
                }
            }
            if (exception != null) throw exception;
        }
        return content;
    }

}
