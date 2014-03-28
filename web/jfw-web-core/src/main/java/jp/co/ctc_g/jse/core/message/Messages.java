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

package jp.co.ctc_g.jse.core.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.ctc_g.jfw.core.util.Args;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

/**
 * メッセージをメッセージ・タイプごとに管理するコンテナです。
 * @author ITOCHU Techno-Solutions Corporation.
 */
public class Messages implements Serializable {

    enum MessageType {
        VALIDATION, ERROR, INFORMATION
    }

    private static final long serialVersionUID = 1L;

    private MessageType type;

    private List<Message> messageItemList;

    /**
     * デフォルトコンストラクタです。
     */
    public Messages() {}

    /**
     * コンストラクタです。
     * @param type メッセージ・タイプ
     */
    public Messages(MessageType type) {
        this.type = type;
        messageItemList = new ArrayList<Message>();
    }

    /**
     * メッセージを追加します。
     * @param message {@link Message}
     */
    public void add(Message message) {
        messageItemList.add(message);
    }

    /**
     * 指定した{@code Messages}が管理するメッセージをマージします。
     * @param messages {@code Messages}
     */
    public void add(Messages messages) {
        messageItemList.addAll(messages.getMessageItemList());
    }

    /**
     * メッセージを追加します。
     * @param messageList {@link Message}のリスト
     */
    public void add(List<Message> messageList) {
        messageItemList.addAll(messageList);
    }

    /**
     * {@link Message}のリストを返却します。
     * @return {@link Message}のリスト
     */
    public List<Message> getMessageItemList() {
        return messageItemList;
    }

    /**
     * メッセージのリストを返却します。
     * @return メッセージのリスト
     */
    public List<String> getMessageList() {
        if (messageItemList.isEmpty()) return Collections.<String>emptyList();
        List<String> messageList = new ArrayList<String>();
        for (Message message : messageItemList) {
            messageList.add(message.toString());
        }
        return messageList;
    }

    /**
     * 指定したプロパティに該当するメッセージのリストを返却します。
     * @param property プロパティ名
     * @return 指定したプロパティに該当するメッセージのリスト
     */
    public List<String> getMessageList(String property) {
        Args.checkNotNull(property);
        if (messageItemList.isEmpty()) return Collections.<String>emptyList();
        List<String> messageList = new ArrayList<String>();
        for (Message message : messageItemList) {
            if (property.equals(message.getProperty()) || property.equals("*"))
                messageList.add(message.toString());
        }
        return messageList;
    }

    /**
     * メッセージ・タイプを返却します。
     * @return メッセージ・タイプ
     */
    public MessageType getMessageType() {
        return type;
    }

    /**
     * メッセージが空かどうかを返却します。
     * @return 空の場合{@code true}
     */
    public boolean isEmpty() {
        return messageItemList == null || messageItemList.isEmpty();
    }
    
    /**
     * 指定されたメッセージのインスタンスのメッセージキーと同じものを削除します。
     * @param message メッセージのインスタンス
     */
    public void remove(Message message) {
        remove(message.getKey());
    }

    /**
     * 指定されたメッセージキーを削除します。
     * @param key メッセージキー
     */
    public void remove(String key) {
        final String k = key;
        Iterators.removeIf(messageItemList.iterator(), new Predicate<Message>() {
            @Override
            public boolean apply(Message m) {
                return m.getKey().equals(k);
            }
        });
    }

    /**
     * ストアされている全てのメッセージをNullで初期化します。
     */
    public void clear() {
        messageItemList = null;
    }

}
