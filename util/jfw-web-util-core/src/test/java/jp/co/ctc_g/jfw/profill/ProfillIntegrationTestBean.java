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

import jp.co.ctc_g.jfw.profill.Profill.Nested;

public class ProfillIntegrationTestBean {

    private String annotationInjection;

    private String nameInjection;

    private Integer typeInjection;

    @StringLiteral(AnnotatedStringLiteralFillingProvider.INJECTED)
    public void setAnnotationInjection(String string) {
        this.annotationInjection = string;
    }

    public String getNameInjection() {
        return nameInjection;
    }

    public void setNameInjection(String nameInjection) {
        this.nameInjection = nameInjection;
    }

    public Integer getTypeInjection() {
        return typeInjection;
    }

    public void setTypeInjection(Integer typeInjection) {
        this.typeInjection = typeInjection;
    }

    public String getAnnotationInjection() {
        return annotationInjection;
    }

    public static class ParentBean {

        private String annotationInjection;

        private ChildBean child;
        
        public String getAnnotationInjection() {
            return annotationInjection;
        }

        @StringLiteral(AnnotatedStringLiteralFillingProvider.INJECTED)
        public void setAnnotationInjection(String annotationInjection) {
            this.annotationInjection = annotationInjection;
        }
        
        @Nested
        public ChildBean getChild() {
            return child;
        }
        
        public void setChild(ChildBean child) {
            this.child = child;
        }
    }

    public static class ChildBean {

        private String annotationInjection;

        public String getAnnotationInjection() {
            return annotationInjection;
        }

        @StringLiteral(AnnotatedStringLiteralFillingProvider.INJECTED)
        public void setAnnotationInjection(String annotationInjection) {
            this.annotationInjection = annotationInjection;
        }
    }
}
