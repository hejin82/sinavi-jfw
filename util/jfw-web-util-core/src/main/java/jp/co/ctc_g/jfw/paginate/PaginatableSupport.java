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

package jp.co.ctc_g.jfw.paginate;

import java.io.Serializable;

/**
 * <p>
 * このクラスは、ページング結果を{@link jp.co.ctc_g.jfw.core.util.PartialList}に加工する際に必要なデータを保持します。
 * </p>
 * <p>
 * J-Frameworkのページング機構を利用する場合、検索条件を保持するドメインクラスはこのクラスを継承して実装する必要があります。
 * また、ページの開始位置やページの終了位置は{@link Paginates#set(Paginatable, int)}などを利用して、
 * アプリケーション側で明示的に計算する必要があります。
 * J-Frameworkのページング機構を利用するサンプルコードを以下に示します。
 * </p>
 * <p>
 * まずは検索条件を保持するドメインを{@link PaginatableSupport}継承して実装します。
 * <pre>
 * <code>
 * public class DomainCriteria extends PaginatableSupport {
 *     private String name;
 *     public void setName(String name) {
 *         this.name = name;
 *     }
 *     public String getName() {
 *         return name;
 *     }
 * }
 * </code>
 * </pre>
 * </p>
 * <p>
 * 次にDaoインタフェースにサフィックスがWithPaginatingとなるメソッドを定義し、引数には検索条件を保持するドメインを指定します。
 * <pre>
 * <code>
 *  // Serviceのインタフェースと実装クラスは省略します。
 * public interface DomainDao {
 *     List&lt;Domain&gt; listWithPaginating(DomainCriteria criteria);
 * }
 * 
 * &#64;Repository
 * public class DomainDaoImpl implements DomainDao {
 * 
 *     &#64;Autowired
 *     private SqlSessionTemplate template;
 *  
 *     List&lt;Domain&gt; listWithPaginating(DomainCriteria criteria) {
 *         return template.selectList("listWithPaginating", criteria);
 *     }
 * }
 * </code>
 * </pre>
 * </p>
 * <p>
 * Controllerクラスで画面から送信されてきたページ番号をもとにページの開始位置や終了位置を計算し、検索を実行します。
 * 計算するときには{@link Paginates#set(Paginatable, int)}や{@link Paginates#set(Paginatable, int, int)}などを利用してください。
 * <pre>
 * <code>
 * &#64;Controller
 * &#64;RequestMapping("/domain")
 * public class DomainController {
 *     &#64;Autowired
 *     protected DomainService service;
 *     
 *     &#64;RequestMapping("/list")
 *     &#64;PostBack.Action.List({
 *             &#64;PostBack.Action(value = Controllers.FORWARD + "/domain/index"),
 *             &#64;PostBack.Action(type = ApplicationRecoverableException.class, value = "domain/list")})
 *     &#64;SessionAttributeComplete(types = { Domain.class })
 *     public String list(&#64;Validated &#64;ModelAttribute DomainCriteria criteria, Model model) {
 *         int pageNumber = criteria.getPageNumber().intValue();
 *         Paginates.set(criteria, pageNumber);
 *         Domains lists = service.listWithPaginating(criteria);
 *         model.addAttribute(lists);
 *         return "domain/list";
 *     }
 * }
 * </code>
 * </pre>
 * </p>
 * <p>
 * 最後にJSPにページングを表示します。
 * <pre>
 * <code>
 *     &lt;c:if test="${not empty domains.domains}"&gt;
 *         &lt;div class="page_navigation"&gt;
 *              &lt;jse:navi partial="${domains.domains}" action="/domain/list"&gt;
 *              &lt;/jse:navi&gt;
 *         &lt;/div>
 *         // 一覧表示は省略
 *     &lt;/c:if>
 * </code>
 * </pre>
 * </p>
 * @see Paginatable
 * @see Paginates
 */
public class PaginatableSupport implements Paginatable, Serializable {

    private static final long serialVersionUID = 1L;

    private int limit;
    private int offset;
    private int tail;

    /**
     * デフォルトコンストラクタです。
     */
    public PaginatableSupport() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLimit() {
        return limit;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getOffset() {
        return offset;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getTail() {
        return tail;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTail(int tail) {
        this.tail = tail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int p = 37, r = 7;
        r = p * r + limit;
        r = p * r + offset;
        r = p * r + tail;
        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PaginatableSupport)) return false;
        PaginatableSupport u = (PaginatableSupport) obj;
        return limit == u.limit
                && offset == u.offset
                && tail == u.tail;
    }
}
