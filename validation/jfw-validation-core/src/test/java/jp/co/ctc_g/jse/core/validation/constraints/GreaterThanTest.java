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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import jp.co.ctc_g.jse.test.util.Validations;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GreaterThanTest {

    private static Validator VALIDATOR;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {

        VALIDATOR = Validations.getValidator();
    }

    @GreaterThan(from = "", to = "")
    public class GreaterThanErrorBean {
    }

    @GreaterThan(from = "fromNumber", to = "toNumber")
    public class GreaterThanInvocationTargetExceptionBean {

        public BigDecimal getFromNumber() {

            throw new RuntimeException();
        }

        public BigDecimal getToNumber() {

            throw new RuntimeException();
        }
    }

    @GreaterThan(from = "fromNumber", to = "toNumber")
    public class GreaterThanBean {

        private BigDecimal fromNumber;
        private BigDecimal toNumber;

        public BigDecimal getToNumber() {

            return toNumber;
        }

        public void setToNumber(BigDecimal toNumber) {

            this.toNumber = toNumber;
        }

        public BigDecimal getFromNumber() {

            return fromNumber;
        }

        public void setFromNumber(BigDecimal date) {

            this.fromNumber = date;
        }
    }

    @GreaterThan.List({
        @GreaterThan(from = "fromNumber1", to = "toNumber1"), @GreaterThan(from = "fromNumber2", to = "toNumber2")
    })
    public class GreaterThanListBean {

        private BigDecimal fromNumber1;
        private BigDecimal toNumber1;
        private BigDecimal fromNumber2;
        private BigDecimal toNumber2;

        public BigDecimal getToNumber1() {

            return toNumber1;
        }

        public void setToNumber1(BigDecimal toNumber) {

            this.toNumber1 = toNumber;
        }

        public BigDecimal getFromNumber1() {

            return fromNumber1;
        }

        public void setFromNumber1(BigDecimal date) {

            this.fromNumber1 = date;
        }

        public BigDecimal getFromNumber2() {

            return fromNumber2;
        }

        public void setFromNumber2(BigDecimal fromNumber2) {

            this.fromNumber2 = fromNumber2;
        }

        public BigDecimal getToNumber2() {

            return toNumber2;
        }

        public void setToNumber2(BigDecimal toNumber2) {

            this.toNumber2 = toNumber2;
        }
    }

    @GreaterThan.List({
        @GreaterThan(from = "fromNumber1", to = "toNumber1", message = "fromNumber1はtoNumber1よりも{jp.co.ctc_g.jse.core.validation.constraints.GreaterThan.message}"),
        @GreaterThan(from = "fromNumber2", to = "toNumber2", message = "fromNumber2はtoNumber2よりも大きい値を入力してください。")
    })
    public class GreaterThanListOverrideMessageBean {

        private BigDecimal fromNumber1;
        private BigDecimal toNumber1;
        private BigDecimal fromNumber2;
        private BigDecimal toNumber2;

        public BigDecimal getToNumber1() {

            return toNumber1;
        }

        public void setToNumber1(BigDecimal toNumber) {

            this.toNumber1 = toNumber;
        }

        public BigDecimal getFromNumber1() {

            return fromNumber1;
        }

        public void setFromNumber1(BigDecimal date) {

            this.fromNumber1 = date;
        }

        public BigDecimal getFromNumber2() {

            return fromNumber2;
        }

        public void setFromNumber2(BigDecimal fromNumber2) {

            this.fromNumber2 = fromNumber2;
        }

        public BigDecimal getToNumber2() {

            return toNumber2;
        }

        public void setToNumber2(BigDecimal toNumber2) {

            this.toNumber2 = toNumber2;
        }
    }

    @Test
    public void no_such_method_exception_test() {

        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString("HV000028"));
        GreaterThanErrorBean target = new GreaterThanErrorBean();
        VALIDATOR.validate(target);
    }

    @Test
    public void invocation_target_exception_test() {

        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString("HV000028"));
        GreaterThanInvocationTargetExceptionBean target = new GreaterThanInvocationTargetExceptionBean();
        VALIDATOR.validate(target);
    }

    @Test
    public void validNullValue() {

        GreaterThanBean target = new GreaterThanBean();
        Set<ConstraintViolation<GreaterThanBean>> errors = VALIDATOR.validate(target);
        assertThat(errors, notNullValue());
        assertThat(errors.size(), is(0));
    }

    @Test
    public void valid() {

        GreaterThanBean target = new GreaterThanBean();
        target.setFromNumber(new BigDecimal(10001));
        target.setToNumber(new BigDecimal(10000));
        Set<ConstraintViolation<GreaterThanBean>> errors = VALIDATOR.validate(target);
        assertThat(errors, notNullValue());
        assertThat(errors.size(), is(0));
    }

    @Test
    public void validList() {

        GreaterThanListBean target = new GreaterThanListBean();
        target.setFromNumber1(new BigDecimal(10001));
        target.setToNumber1(new BigDecimal(10000));
        target.setFromNumber2(new BigDecimal(10001));
        target.setToNumber2(new BigDecimal(10000));
        Set<ConstraintViolation<GreaterThanListBean>> errors = VALIDATOR.validate(target);
        assertThat(errors, notNullValue());
        assertThat(errors.size(), is(0));
    }

    @Test
    public void invalid() {

        GreaterThanBean target = new GreaterThanBean();
        target.setFromNumber(new BigDecimal(10000));
        target.setToNumber(new BigDecimal(10000));
        Set<ConstraintViolation<GreaterThanBean>> errors = VALIDATOR.validate(target);
        assertThat(errors.size(), is(1));
        target.setFromNumber(new BigDecimal(10000));
        target.setToNumber(new BigDecimal(10001));
        errors = VALIDATOR.validate(target);
        assertThat(errors, notNullValue());
        assertThat(errors.size(), is(1));
    }

    @Test
    public void invalidListSomeErrors() {

        GreaterThanListBean target = new GreaterThanListBean();
        target.setFromNumber1(new BigDecimal(10001));
        target.setToNumber1(new BigDecimal(10000));
        target.setFromNumber2(new BigDecimal(100000));
        target.setToNumber2(new BigDecimal(100001));
        Set<ConstraintViolation<GreaterThanListBean>> errors = VALIDATOR.validate(target);
        assertThat(errors, notNullValue());
        assertThat(errors.size(), is(1));
    }

    @Test
    public void invalidListAllErrors() {

        GreaterThanListBean target = new GreaterThanListBean();
        target.setFromNumber1(new BigDecimal(10000));
        target.setToNumber1(new BigDecimal(10001));
        target.setFromNumber2(new BigDecimal(100000));
        target.setToNumber2(new BigDecimal(100001));
        Set<ConstraintViolation<GreaterThanListBean>> errors = VALIDATOR.validate(target);
        assertThat(errors, notNullValue());
        assertThat(errors.size(), is(2));
    }

    @Test
    public void default_message_test() {

        GreaterThanBean target = new GreaterThanBean();
        target.setFromNumber(new BigDecimal(10000));
        target.setToNumber(new BigDecimal(10000));
        Set<ConstraintViolation<GreaterThanBean>> errors = VALIDATOR.validate(target);
        assertThat(errors.size(), is(1));
        assertEqualsErrorMessages(errors, "大きい値を入力してください。");
    }

    @Test
    public void override_message_test() {

        GreaterThanListOverrideMessageBean target = new GreaterThanListOverrideMessageBean();
        target.setFromNumber1(new BigDecimal(10000));
        target.setToNumber1(new BigDecimal(10001));
        target.setFromNumber2(new BigDecimal(100000));
        target.setToNumber2(new BigDecimal(100001));
        Set<ConstraintViolation<GreaterThanListOverrideMessageBean>> errors = VALIDATOR.validate(target);
        assertThat(errors, notNullValue());
        assertThat(errors.size(), is(2));
        assertEqualsErrorMessages(errors, "fromNumber1はtoNumber1よりも大きい値を入力してください。", "fromNumber2はtoNumber2よりも大きい値を入力してください。");
        assertEqualsPropertyPath(errors, "fromNumber1", "fromNumber2");
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

    private static void assertEqualsPropertyPath(Set<? extends ConstraintViolation<?>> errors, String... expectedPaths) {

        List<String> expectedPathsAsList = Arrays.asList(expectedPaths);
        List<String> actualPaths = new ArrayList<String>();
        for (ConstraintViolation<?> error : errors) {
            actualPaths.add(error.getPropertyPath().toString());
        }
        Collections.sort(expectedPathsAsList);
        Collections.sort(actualPaths);
        assertThat(actualPaths, is(expectedPathsAsList));
    }
}
