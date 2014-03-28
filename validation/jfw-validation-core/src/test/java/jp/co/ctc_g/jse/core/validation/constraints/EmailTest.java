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
public class EmailTest {

    protected static Validator VALIDATOR;

    @RunWith(Theories.class)
    public static class CharSequenceEmailTest {

        @DataPoints
        public static final String[] VALIDS = {
            null, "", "test.test@ctc-g.co.jp", "hoge@hoge.com", "hoge@gmail.com", "hoge-hoge@ctc-g.co.jp"
        };

        @DataPoints
        public static final String[] INVALIDS = {
            " ", "hoge@hoge", "a_a.jp", "hoge.hoge at ctc-g.co.jp", "hoge\\hoge@ctc_g.co.jp", "ほげ@ほげ.com", "hoge hoge@hoge.com",
            "hoge@hoge hoge.com", " hoge@hoge.com", "hoge@hoge.com ", "hoge-hoge@ctc_g.co.jp", "123_456@123.45.67.89.abcd"
        };

        @Before
        public void setup() {

            VALIDATOR = Validations.getValidator();
        }

        @Theory
        public void invalid(String invalid) {

            Assume.assumeThat(Arrays.asList(INVALIDS), hasItem(invalid));
            class EmailTargetBean {

                @Email
                public String value;
            }
            EmailTargetBean target = new EmailTargetBean();
            target.value = invalid;
            Set<ConstraintViolation<EmailTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(1));
        }

        @Theory
        public void valid(String valid) {

            Assume.assumeThat(Arrays.asList(VALIDS), hasItem(valid));
            class EmailTargetBean {

                @Email
                public String value;
            }
            EmailTargetBean target = new EmailTargetBean();
            target.value = valid;
            Set<ConstraintViolation<EmailTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(0));
        }

    }

    public static class ObjectEmailTest {

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
            class EmailTargetBean {

                @Email
                public Object value;
            }
            EmailTargetBean target = new EmailTargetBean();
            VALIDATOR.validate(target);
        }
    }

    @RunWith(Theories.class)
    public static class MessageTest {

        @DataPoints
        public static final String[] INVALIDS = {
            " ", "hoge@hoge", "a_a.jp", "hoge.hoge at ctc-g.co.jp", "hoge\\hoge@ctc_g.co.jp", "ほげ@ほげ.com", "hoge hoge@hoge.com",
            "hoge@hoge hoge.com", " hoge@hoge.com", "hoge@hoge.com ", "hoge-hoge@ctc_g.co.jp", "123_456@123.45.67.89.abcd"
        };

        @Before
        public void setup() {

            VALIDATOR = Validations.getValidator();
        }

        @Theory
        public void default_message_test(String invalid) {

            Assume.assumeThat(Arrays.asList(INVALIDS), hasItem(invalid));
            class EmailTargetBean {

                @Email
                public String value;
            }
            EmailTargetBean target = new EmailTargetBean();
            target.value = invalid;
            Set<ConstraintViolation<EmailTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(1));
            assertEqualsErrorMessages(errors, "メールアドレスの形式で入力してください。");
        }

        @Theory
        public void override_message_test(String invalid) {

            Assume.assumeThat(Arrays.asList(INVALIDS), hasItem(invalid));
            class EmailTargetBean {

                @Email(message = "メールアドレス形式のみ入力可能です。${validatedValue}")
                public String value;
            }
            EmailTargetBean target = new EmailTargetBean();
            target.value = invalid;
            Set<ConstraintViolation<EmailTargetBean>> errors = VALIDATOR.validate(target);
            assertThat(errors, notNullValue());
            assertThat(errors.size(), is(1));
            assertEqualsErrorMessages(errors, "メールアドレス形式のみ入力可能です。" + invalid);
        }

        private static void assertEqualsErrorMessages(Set<? extends ConstraintViolation<?>> errors, String... expectedMessages) {

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
