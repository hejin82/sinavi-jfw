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

package jp.co.ctc_g.jse.core.amqp.config.amqp;

import jp.co.ctc_g.jse.core.amqp.config.AmqpContextConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.mock.env.MockPropertySource;

@Import(AmqpContextConfig.class)
public class OverrideProperties {

    public OverrideProperties() {
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreResourceNotFound(true);
        configurer.setIgnoreUnresolvablePlaceholders(true);
        MutablePropertySources propertySources = new MutablePropertySources();
        MockPropertySource source = new MockPropertySource()
            .withProperty("rabbitmq.host", "192.168.10.10")
            .withProperty("rabbitmq.username", "jfw")
            .withProperty("rabbitmq.password", "jfw")
            .withProperty("rabbitmq.channel-cache-size", 100);
        propertySources.addLast(source);
        configurer.setPropertySources(propertySources);
        return configurer;
    }
}