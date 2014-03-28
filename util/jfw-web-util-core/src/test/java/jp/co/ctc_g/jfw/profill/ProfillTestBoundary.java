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

package jp.co.ctc_g.jfw.profill;


public interface ProfillTestBoundary {

    void invokeWithNonAnnotatedParameter(
            ProfillInterceptorIntegrationTestBean bean);
    
    void invokeWithNonAnnotatedParameter(
            ProfillInterceptorIntegrationTestBean first, 
            ProfillInterceptorIntegrationTestBean second);
    
    void invokeWithDefaultAnnotatedParameter(@Profillable ProfillInterceptorIntegrationTestBean bean);
    
    void invokeWithDefaultAnnotatedParameter(
            @Profillable ProfillInterceptorIntegrationTestBean first,
            @Profillable ProfillInterceptorIntegrationTestBean second);
    
    void invokeWithDefaultAnnotatedParameter(
            @Profillable ProfillInterceptorIntegrationTestBean first,
            ProfillInterceptorIntegrationTestBean not,
            @Profillable ProfillInterceptorIntegrationTestBean second);
}
