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
package org.apache.camel.component.jenkins;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.jenkins.consumer.BuildConsumer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.util.StringHelper;

/**
 * Interact with the Jenkins API.
 *
 * The endpoint encapsulates portions of the Jenkins API, relying on the  Java SDK.
 * Available endpoint URIs include:
 *
 * CONSUMERS
 * jenkins://build
  *
 * The endpoints will respond with org.eclipse.egit.github.core-provided POJOs (Build)
 */
@UriEndpoint(firstVersion = "2.15.0", scheme = "jenkins", title = "Jenkins", syntax = "jenkins:type", category = {Category.FILE, Category.CLOUD, Category.API})
public class JenkinsEndpoint extends DefaultEndpoint {

    @UriPath @Metadata(required = true)
    private JenkinsType type;    @UriPath(label = "consumer")
    private String targetUrl;
    @UriParam
    private String username;
    @UriParam
    private String password;
    @UriParam
    private String oauthToken;
    @UriParam
    private String jobfilter;

    public JenkinsEndpoint(String uri, JenkinsComponent component) {
        super(uri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        throw new IllegalArgumentException("Cannot create producer with type " + type);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        if (type == JenkinsType.build) {
            return new BuildConsumer(this, processor);
        }
        throw new IllegalArgumentException("Cannot create consumer with type " + type);
    }

    public JenkinsType getType() {
        return type;
    }

    /**
     * What Jenkins operation to execute
     */
    public void setType(JenkinsType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Jenkins User name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Jenkins password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    /**
     * Jenkins Oauth Token
     */
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public boolean hasOauth() {
        return oauthToken != null && oauthToken.length() > 0;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    /**
     * Jenkins URL
     */
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getJobfilter() {
        return jobfilter;
    }

    /**
     * Job filter
     */
    public void setJobfilter(String jobfilter) {
        this.jobfilter = jobfilter;
    }
}
