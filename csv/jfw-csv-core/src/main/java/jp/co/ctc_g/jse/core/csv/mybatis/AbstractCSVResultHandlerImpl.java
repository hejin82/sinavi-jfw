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

package jp.co.ctc_g.jse.core.csv.mybatis;

import java.util.ResourceBundle;

import jp.co.ctc_g.jfw.core.internal.InternalMessages;
import jp.co.ctc_g.jfw.core.util.Args;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jse.core.csv.CSVConfigs.CSVConfig;
import jp.co.ctc_g.jse.core.csv.CSVWriters;
import jp.co.ctc_g.jse.core.csv.CSVs;
import jp.co.ctc_g.jse.core.csv.DownloadFile;

import org.apache.ibatis.session.ResultContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * このクラスは、RDMBSより1レコードずつ取得し、
 * CSVファイルに書き込みを行う処理を共通化しています。
 * </p>
 * <p>
 * 大量のデータをCSVへ書き込む際は1レコードずつ出力することにより、
 * メモリ使用量を低減することが可能になります。
 * 以下に実装例を示します。
 * <pre class="brush:java">
 * public class HogeCSVResultHandler extends AbstractCSVResultHandlerImpl {
 *   &#64;Override
 *   public String[] getHeader() {
 *     return new String[] {
 *       "ID",
 *       "名前"
 *     };
 *   }
 *   &#64;Override
 *   public void write(ResultContext context) {
 *     // 1レコードを取得し、CSVへ書き込む
 *     Hoge result = (Hoge) context.getResultObject();
 *     String data = new String[] {
 *       result.getId(),
 *       result.getName()
 *     };
 *     csv.write(data);
 *   }
 * }
 * 
 * &#64;Repository
 * public class HogeDaoImpl extends HogeDao {
 *   &#64;Autowired
 *   private SqlSessionTemplate template;
 *   
 *   &#64;Override
 *   public DownloadFile print() {
 *     // Handlerのインスタンスを生成
 *     CSVResultHanlder handler = new HogeCSVResultHandler();
 *     try {
 *       handler.open(); // ストリームをオープン
 *       handler.header(); // ヘッダ行を書き込む
 *       template.select("list", handler); // 1レコードずつ書き込む
 *     } finally {
 *       handler.close();
 *     }
 *     return handler.get();
 *   }
 * }
 * </pre>
 * @see CSVResultHandler
 * @author ITOCHU Techno-Solutions Corporation.
 */
public abstract class AbstractCSVResultHandlerImpl implements CSVResultHandler {

    private static final Logger L = LoggerFactory.getLogger(AbstractCSVResultHandlerImpl.class);
    private static final ResourceBundle R = InternalMessages.getBundle(AbstractCSVResultHandlerImpl.class);

    /**
     * CSV出力用のユーティリティのインスタンス
     */
    protected CSVWriters csv = null;

    /**
     * コンストラクタです。
     */
    public AbstractCSVResultHandlerImpl() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleResult(ResultContext context) {
        if (L.isDebugEnabled()) {
            int cnt = context.getResultCount();
            L.debug(Strings.substitute(R.getString("D-CSV-MYBATIS#0001"), Maps.hash("count", Integer.valueOf(cnt))));
        }
        write(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void header() {
        Args.checkNotNull(csv, R.getString("E-CSV-MYBATIS#0001"));
        String[] header = getHeader();
        if (header != null && header.length > 0) {
            csv.write(header);
        } else {
            if (L.isDebugEnabled()) {
                L.debug(R.getString("D-CSV-MYBATIS#0002"));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        csv = CSVs.writers().touch().open();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(CSVConfig config) {
        csv = CSVs.writers(config).touch().open();
    }

    /**
     * CSVのストリームを閉じます。
     * 必ずストリームをクローズするようにしてください。handler
     */
    @Override
    public void close() {
        Args.checkNotNull(csv, R.getString("E-CSV-MYBATIS#0001"));
        csv.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DownloadFile get() {
        Args.checkNotNull(csv, R.getString("E-CSV-MYBATIS#0001"));
        return csv.get();
    }

    /**
     * 1レコードをファイルに出力します。
     * このメソッドは必ず実装してください。
     * @param context 1レコード
     */
    public abstract void write(ResultContext context);

    /**
     * CSVのヘッダーに出力する文字列を設定します。
     * @return ヘッダー文字列の配列
     */
    public String[] getHeader() {
        return null;
    }

}
