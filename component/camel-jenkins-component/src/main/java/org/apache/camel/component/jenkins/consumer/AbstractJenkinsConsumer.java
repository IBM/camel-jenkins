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
package org.apache.camel.component.jenkins.consumer;

import org.apache.camel.Processor;
import org.apache.camel.component.jenkins.JenkinsConstants;
import org.apache.camel.component.jenkins.JenkinsEndpoint;
import org.apache.camel.spi.Registry;
import org.apache.camel.support.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.regex.Pattern;
import java.net.URI;
    
//import com.cdancy.jenkins.rest.JenkinsClient;
//import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;


public abstract class AbstractJenkinsConsumer extends ScheduledPollConsumer {
    private static final transient Logger LOG = LoggerFactory.getLogger(AbstractJenkinsConsumer.class);

    private JenkinsServer jenkinsServer;
    private Pattern jobFilter;
    
    
    public AbstractJenkinsConsumer(JenkinsEndpoint endpoint, Processor processor) throws Exception {
        super(endpoint, processor);
	JenkinsEndpoint ep = endpoint;

        Registry registry = endpoint.getCamelContext().getRegistry();
        Object server = registry.lookupByName(JenkinsConstants.JENKINS_SERVICE);
        if (server != null) {
            LOG.debug("Using JenkinsService found in registry {}");
            jenkinsServer = (JenkinsServer) server;
        } else {
	    //	    jenkinsServer = new JenkinsServer(new URI("http://9.65.246.37:8080/"), "admin", "admin");
	    jenkinsServer = new JenkinsServer(new URI(ep.getTargetUrl()), ep.getUsername(), ep.getPassword());
        }
	jobFilter = Pattern.compile((ep.getJobfilter() == null)? ".*" : ep.getJobfilter());
    }

    
    protected JenkinsServer getJenkinsServer() {
        return jenkinsServer;
    }
    
    protected Pattern getJobFilter() {
        return jobFilter;
    }
    
    @Override
    protected abstract int poll() throws Exception;
}
