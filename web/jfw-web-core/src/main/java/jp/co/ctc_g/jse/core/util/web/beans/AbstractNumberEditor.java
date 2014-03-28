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

package jp.co.ctc_g.jse.core.util.web.beans;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

import jp.co.ctc_g.jfw.core.internal.InternalException;
import jp.co.ctc_g.jfw.core.util.Maps;
import jp.co.ctc_g.jfw.core.util.Strings;
import jp.co.ctc_g.jfw.core.util.typeconverter.TypeConverters;

/**
 * <p>
 * このクラスは数値系プロパティエディタの抽象クラスです。<br/>
 * {@code J-Framework} では、以下の具象クラスを提供します。使用方法については各クラスのAPIマニュアルを参照して下さい。
 * </p>
 * <ul>
 *   <li>{@link HalfwidthNumberEditor}</li>
 *   <li>{@link HalfwidthNumberWithCommaEditor}</li>
 *   <li>{@link HalfAndFullwidthNumberEditor}</li>
 *   <li>{@link HalfAndFullwidthNumberWithCommaEditor}</li>
 *   <li>{@link HalfwidthDecimalEditor}</li>
 *   <li>{@link HalfwidthDecimalWithCommaEditor}</li>
 *   <li>{@link HalfAndFullwidthDecimalEditor}</li>
 *   <li>{@link HalfAndFullwidthDecimalWithCommaEditor}</li>
 * </ul>
 * @author ITOCHU Techno-Solutions Corporation.
 * @see PropertyEditorSupport
 */
public class AbstractNumberEditor extends PropertyEditorSupport {

    private static final String CODE = "number.typeMissmatch";

    protected String pattern;

    protected NumberFormat format;

    protected Class<? extends Number> propertyType;

    protected boolean allowEmpty;

    protected String message;

    protected boolean needFullWidthToHalfWidth = false;

    /**
     * デフォルトコンストラクタです。
     */
    public AbstractNumberEditor() {}

    protected boolean isNumericFormat(String suspect, String numberPattern) {

        if (Pattern.matches(numberPattern, suspect)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) {

        if (this.allowEmpty && Strings.isEmpty(text)) {
            setValue(null);
        } else if (!this.allowEmpty && Strings.isEmpty(text)) {
            throw new PropertyEditingException(CODE, Editors.REQUIRED_MESSAGE);
        } else {
            if (isNumericFormat(text, pattern)) {
                try {
                    text = fullWidthToHalfWidthIfNeeded(text);
                    if (format != null) {
                        if (format instanceof DecimalFormat && typeOfBigDecimalOrBigInteger()) {
                            ((DecimalFormat) format).setParseBigDecimal(true);
                        }
                        Number n = format.parse(text);
                        rangeOverCheck(propertyType, n.toString());
                        setValue(TypeConverters.convert(n, propertyType));
                    } else {
                        Number n = TypeConverters.convert(text, propertyType);
                        rangeOverCheck(propertyType, n.toString());
                        setValue(n);
                    }
                } catch (NumberFormatException e) {
                    throw new PropertyEditingException(CODE, Strings.substitute(message, Maps.hash("pattern", pattern)));
                } catch (ParseException e) {
                    throw new PropertyEditingException(CODE, Strings.substitute(message, Maps.hash("pattern", pattern)));
                } catch (InternalException e) {
                    if (Editors.isHandlingTargetException(e)) {
                        throw new PropertyEditingException(CODE, Strings.substitute(message, Maps.hash("pattern", pattern)));
                    }
                    throw e;
                }
            } else {
                throw new PropertyEditingException(CODE, Strings.substitute(message, Maps.hash("pattern", pattern)));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        Object value = getValue();
        if (value == null) {
            return "";
        } else {
            return formatIfNeeded(value);
        }
    }
    
    private boolean typeOfBigDecimalOrBigInteger() {
        return propertyType.equals(BigDecimal.class) || propertyType.equals(BigInteger.class);
    }

    private void rangeOverCheck(Class<? extends Number> type, String v) {
        if (type.equals(Byte.class)) {
            setValue(Byte.parseByte(v));
        } else if (type.equals(Short.class)) {
            Short.parseShort(v);
        } else if (type.equals(Integer.class)) {
            Integer.parseInt(v);
        } else if (type.equals(Long.class)) {
            Long.parseLong(v);
        } else if (type.equals(Float.class)) {
            Float.parseFloat(v);
        } else if (type.equals(Double.class)) {
            Double.parseDouble(v);
        } else if (type.equals(BigInteger.class) || type.equals(BigDecimal.class) || type.equals(Number.class)) {
            return;
        } else {
            throw new IllegalArgumentException("invalid type argument of [" + type.toString() + "].");
        }
    }

    protected String formatIfNeeded(Object value) {
        return value.toString();
    }

    protected String fullWidthToHalfWidthIfNeeded(String source) {
        if (needFullWidthToHalfWidth) {
            StringBuilder buffer = new StringBuilder(source);
            for (int i = 0; i < source.length(); i++) {
                char c = source.charAt(i);
                if ('０' <= c && c <= '９') {
                    buffer.setCharAt(i, (char) (c - '０' + '0'));
                }
                if ('－' == c) {
                    buffer.setCharAt(i, '-');
                }
                if ('，' == c) {
                    buffer.setCharAt(i, ',');
                }
                if ('．' == c) {
                    buffer.setCharAt(i, '.');
                }
            }
            return buffer.toString();
        } else {
            return source;
        }
    }
}