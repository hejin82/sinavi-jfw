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

package jp.co.ctc_g.jse.core.validation.constraints;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validator;

import jp.co.ctc_g.jse.test.util.Validations;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class MaxLengthTest {

    protected static Validator VALIDATOR;

    @RunWith(Theories.class)
    public static class CharSequenceMaxLengthTest {

        @DataPoints
        public static final String[] VALIDS = {
            null, "", "abcde", "12345", "-1234", "0.123", " 1234", "1", "1234", "あいうえお", "アイウエオ", "亜衣兎絵御"
        };

        @DataPoints
        public static final String[] INVALIDS = {
            "abcdef", "123456", "-12345", "0.1234", " 12345"
        };

        @Before
        public void setup() {

            VALIDATOR = Validations.getValidator();
        }

        @Theory
        public void valid(String valid) throws UnsupportedEncodingException {

            Assume.assumeThat(Arrays.asList(VALIDS), hasItem(valid));
            class MaxLengthTargetBean {

                @MaxLength(5)
                public String value;
            }
            MaxLengthTargetBean target = new MaxLengthTargetBean();
            target.value = valid;
            Set<ConstraintViolation<MaxLengthTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(0));
        }

        @Theory
        public void invalid(String invalid) {

            Assume.assumeThat(Arrays.asList(INVALIDS), hasItem(invalid));
            class MaxLengthTargetBean {

                @MaxLength(5)
                public String value;
            }
            MaxLengthTargetBean target = new MaxLengthTargetBean();
            target.value = invalid;
            Set<ConstraintViolation<MaxLengthTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(1));
        }
    }

    
    public static class ObjectMaxLengthTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Before
        public void setup() {

            VALIDATOR = Validations.getValidator();
        }

        @Test
        public void shouldThrowUnexpectedTypeException() {

            thrown.expect(UnexpectedTypeException.class);
            thrown.expectMessage(containsString("HV000030"));
            class MaxLengthTargetBean {

                @MaxLength(5)
                public Object value;
            }
            MaxLengthTargetBean target = new MaxLengthTargetBean();
            VALIDATOR.validate(target);
        }
    }

    @RunWith(Theories.class)
    public static class MessageTest {

        @DataPoints
        public static final String[] INVALIDS = {
            "abcdef", "123456", "-12345", "0.1234", " 12345"
        };

        @Before
        public void setup() {

            VALIDATOR = Validations.getValidator();
        }

        @Theory
        public void default_message_test(String invalid) {

            Assume.assumeThat(Arrays.asList(INVALIDS), hasItem(invalid));
            class MaxLengthTargetBean {

                @MaxLength(5)
                public String value;
            }
            MaxLengthTargetBean target = new MaxLengthTargetBean();
            target.value = invalid;
            Set<ConstraintViolation<MaxLengthTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(1));
            assertEqualsErrorMessages(errors, "5文字以内で入力してください。");
        }

        @Theory
        public void override_message_test(String invalid) {

            Assume.assumeThat(Arrays.asList(INVALIDS), hasItem(invalid));
            class MaxLengthTargetBean {

                @MaxLength(value = 5, message = "最大入力文字数を超えています。最大:{value}${validatedValue}")
                public String value;
            }
            MaxLengthTargetBean target = new MaxLengthTargetBean();
            target.value = invalid;
            Set<ConstraintViolation<MaxLengthTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(1));
            assertEqualsErrorMessages(errors, "最大入力文字数を超えています。最大:5" + invalid);
        }

        private static void assertEqualsErrorMessages(Set<? extends ConstraintViolation<?>> errors,
            String... expectedMessages) {

            List<String> expectedMessagesAsList = Arrays.asList(expectedMessages);
            List<String> actualMessages = new ArrayList<String>();
            for (ConstraintViolation<?> error : errors) {
                actualMessages.add(error.getMessage());
            }
            Collections.sort(expectedMessagesAsList);
            Collections.sort(actualMessages);
            assertThat(actualMessages, is(expectedMessagesAsList));
        }
    }

}
