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

package jp.co.ctc_g.jfw.core.util.typeconverter;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class FloatConverterTest {

    @Test
    public void Doubleからのconvertテスト() {
        Double d = 255.255;
        Float expected = 255.255f;
        Float actual = new FloatConverter().convert(d);
        assertEquals(expected, actual);
    }
    
    @Test
    public void BigDecimalからのconvertテスト() {
        BigDecimal f = new BigDecimal("255.255");
        Float expected = 255.255f;
        Float actual = new FloatConverter().convert(f);
        assertEquals(expected, actual);
    }
    
    
    @Test
    public void Stringからのconvertテスト1() {
        String s = "255";
        Float expected = new Float("255");
        Float actual = new FloatConverter().convert(s);
        assertEquals(expected, actual);
    }
    
    @Test
    public void Stringからのconvertテスト2() {
        String s = null;
        Float expected = null;
        Float actual = new FloatConverter().convert(s);
        assertEquals(expected, actual);
    }
    
    @Test
    public void Stringからのconvertテスト3() {
        String s = "";
        Float expected = null;
        Float actual = new FloatConverter().convert(s);
        assertEquals(expected, actual);
    }
}
