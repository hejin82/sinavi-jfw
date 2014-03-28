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

package jp.co.ctc_g.jse.core.rest.springmvc.client.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.BadRequestException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.ForbiddenException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.InternalServerErrorException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.NotAcceptableException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.NotFoundException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.ProxyAuthenticationRequiredException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.RequestTimeoutException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.ServiceUnavailableException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.UnauthorizedException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.UnprocessableEntityException;
import jp.co.ctc_g.jse.core.rest.springmvc.client.exception.UnsupportedMediaTypeException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

@RunWith(Enclosed.class)
public class RestClientResponseErrorHandlerTest {

    
    public static class ClientErrorTest {

        private RestClientResponseErrorHandler handler;
        private ClientHttpResponse response;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Before
        public void setup() throws IOException {
            handler = new RestClientResponseErrorHandler();
            response = mock(ClientHttpResponse.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");
            when(response.getHeaders()).thenReturn(headers);
            when(response.getBody()).thenReturn(new ByteArrayInputStream("Body Message".getBytes()));
        }

        @Test
        public void BadRequestExceptionがスローされる() throws IOException {
            thrown.expect(BadRequestException.class);
            thrown.expectMessage("400 BAD_REQUEST");
            when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
            handler.handleError(response);
        }

        @Test
        public void UnauthorizedExceptionがスローされる() throws IOException {
            thrown.expect(UnauthorizedException.class);
            thrown.expectMessage("401 UNAUTHORIZED");
            when(response.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
            handler.handleError(response);
        }

        @Test
        public void ForbiddenExceptionがスローされる() throws IOException {
            thrown.expect(ForbiddenException.class);
            thrown.expectMessage("403 FORBIDDEN");
            when(response.getStatusCode()).thenReturn(HttpStatus.FORBIDDEN);
            handler.handleError(response);
        }

        @Test
        public void NotAcceptableExceptionがスローされる() throws IOException {
            thrown.expect(NotAcceptableException.class);
            thrown.expectMessage("406 NOT_ACCEPTABLE");
            when(response.getStatusCode()).thenReturn(HttpStatus.NOT_ACCEPTABLE);
            handler.handleError(response);
        }

        @Test
        public void NotFoundExceptionがスローされる() throws IOException {
            thrown.expect(NotFoundException.class);
            thrown.expectMessage("404 NOT_FOUND");
            when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
            handler.handleError(response);
        }

        @Test
        public void ProxyAuthenticationRequiredExceptionがスローされる() throws IOException {
            thrown.expect(ProxyAuthenticationRequiredException.class);
            thrown.expectMessage("407 PROXY_AUTHENTICATION_REQUIRED");
            when(response.getStatusCode()).thenReturn(HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
            handler.handleError(response);
        }

        @Test
        public void RequestTimeoutExceptionがスローされる() throws IOException {
            thrown.expect(RequestTimeoutException.class);
            thrown.expectMessage("408 REQUEST_TIMEOUT");
            when(response.getStatusCode()).thenReturn(HttpStatus.REQUEST_TIMEOUT);
            handler.handleError(response);
        }

        @Test
        public void UnsupportedMediaTypeExceptionがスローされる() throws IOException {
            thrown.expect(UnsupportedMediaTypeException.class);
            thrown.expectMessage("415 UNSUPPORTED_MEDIA_TYPE");
            when(response.getStatusCode()).thenReturn(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            handler.handleError(response);
        }

        @Test
        public void UnproccesableEntityExceptionがスローされる() throws IOException {
            thrown.expect(UnprocessableEntityException.class);
            thrown.expectMessage("422 UNPROCESSABLE_ENTITY");
            when(response.getStatusCode()).thenReturn(HttpStatus.UNPROCESSABLE_ENTITY);
            handler.handleError(response);
        }

        @Test
        public void HttpClientErrorExceptionがスローされる() throws IOException {
            thrown.expect(HttpClientErrorException.class);
            thrown.expectMessage("402 PAYMENT_REQUIRED");
            when(response.getStatusCode()).thenReturn(HttpStatus.PAYMENT_REQUIRED);
            when(response.getStatusText()).thenReturn(HttpStatus.PAYMENT_REQUIRED.name());
            handler.handleError(response);
        }
    }

    
    public static class ServerErrorTest {

        private RestClientResponseErrorHandler handler;
        private ClientHttpResponse response;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Before
        public void setup() throws IOException {
            handler = new RestClientResponseErrorHandler();
            response = mock(ClientHttpResponse.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=UTF-8");
            when(response.getHeaders()).thenReturn(headers);
            when(response.getBody()).thenReturn(new ByteArrayInputStream("Body Message".getBytes()));
        }

        @Test
        public void InternalServerErrorExceptionがスローされる() throws IOException {
            thrown.expect(InternalServerErrorException.class);
            thrown.expectMessage("500 INTERNAL_SERVER_ERROR");
            when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
            handler.handleError(response);
        }

        @Test
        public void ServiceUnavailableExceptionがスローされる() throws IOException {
            thrown.expect(ServiceUnavailableException.class);
            thrown.expectMessage("503 SERVICE_UNAVAILABLE");
            when(response.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);
            handler.handleError(response);
        }

        @Test
        public void HttpServerErrorExceptionがスローされる() throws IOException {
            thrown.expect(HttpServerErrorException.class);
            thrown.expectMessage("502 BAD_GATEWAY");
            when(response.getStatusCode()).thenReturn(HttpStatus.BAD_GATEWAY);
            when(response.getStatusText()).thenReturn(HttpStatus.BAD_GATEWAY.name());
            handler.handleError(response);
        }
    }

    
    public static class UnknownStatusTest {

        private RestClientResponseErrorHandler handler;
        private ClientHttpResponse response;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Before
        public void setup() {
            handler = new RestClientResponseErrorHandler();
            response = mock(ClientHttpResponse.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            when(response.getHeaders()).thenReturn(headers);
        }

        @Test
        public void 変換対象外のステータスコードの場合はRestClientExceptionがスローされる() throws IOException {
            thrown.expect(RestClientException.class);
            when(response.getStatusCode()).thenReturn(HttpStatus.UPGRADE_REQUIRED);
            handler.handleError(response);
        }
    }
}
