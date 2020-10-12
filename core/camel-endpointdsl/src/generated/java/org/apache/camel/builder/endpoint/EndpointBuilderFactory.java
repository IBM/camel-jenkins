/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//CHECKSTYLE:OFF
package org.apache.camel.builder.endpoint;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Generated;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@Generated("org.apache.camel.maven.packaging.EndpointDslMojo")
public interface EndpointBuilderFactory
        extends
            org.apache.camel.builder.endpoint.dsl.JenkinsEndpointBuilderFactory.JenkinsBuilders {

    default org.apache.camel.Expression endpoints(
            org.apache.camel.builder.EndpointProducerBuilder... endpoints) {
        return new org.apache.camel.support.ExpressionAdapter() {
            List<org.apache.camel.Expression> expressions = Stream.of(endpoints)
                .map(org.apache.camel.builder.EndpointProducerBuilder::expr)
                .collect(Collectors.toList());
        
            @Override
            public Object evaluate(org.apache.camel.Exchange exchange) {
                return expressions.stream().map(e -> e.evaluate(exchange, Object.class)).collect(Collectors.toList());
            }
        };
    }
}
//CHECKSTYLE:ON