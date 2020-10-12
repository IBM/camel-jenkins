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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jenkins.JenkinsEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;

public class BuildConsumer extends AbstractJenkinsConsumer {
    private static final transient Logger LOG = LoggerFactory.getLogger(BuildConsumer.class);
    private HashMap<String, Integer> lastBuilds = new HashMap(); 
    
    public BuildConsumer(JenkinsEndpoint endpoint, Processor processor) throws Exception {
        super(endpoint, processor);
        LOG.info("Jenkins BuildConsumer");
    }

    @Override
    protected int poll() throws Exception {
        HashMap<String, Object> result = new HashMap();
        Map<String, Job> jobs = getJenkinsServer().getJobs();
	String path = "";
	for (Job job : jobs.values()) {
	    processJob(job, path, result);
        }
        Exchange e = getEndpoint().createExchange();
        e.getIn().setBody(result);
        getProcessor().process(e);
	return result.size();
    }

    private void processJob(Job job, String path, Map result) throws IOException {
    path = path + "/" + job.getName();
        LOG.info(path + ":" + job.getUrl());

        if (getJenkinsServer().getFolderJob(job).isPresent()) {
	    Map<String, Job> jobs = getJenkinsServer().getFolderJob(job).get().getJobs();
            for (Job subjob : jobs.values()) {
                processJob(subjob, path, result);
            }
	} else {
	    if (getJobFilter().matcher(path).matches()) {
                JobWithDetails details = job.details();
	        if (details.hasFirstBuildRun()) {
	            int last = details.getLastBuild().getNumber();
                    Integer lastReported = lastBuilds.get(job.getUrl());
		    if (lastReported == null) lastReported = 0;
                    LOG.info("Last build: " + last);
		    if (last > lastReported) {
		        for (int i = lastReported+1; i <= last; i++) {
		            LOG.info(details.getBuildByNumber(i).details().getDisplayName());
		            Map info = getBuildInfo(details.getBuildByNumber(i));
		            result.put(details.getDisplayName()+":"+details.getBuildByNumber(i).getNumber(), info);
		        }
		        lastBuilds.put(job.getUrl(), last);
		    }
                }
	    }
	}
    }

    private Map getBuildInfo(Build build)  throws IOException {
        HashMap<String, String> info = new HashMap();
	BuildWithDetails details = build.details();
	info.put("Id", details.getId());
	info.put("Duration", Long.toString(details.getDuration()));
	info.put("TimeStamp", Long.toString(details.getTimestamp()));
	info.put("BuildResult", details.getResult().toString());
	info.put("BuildOn", details.getBuiltOn());
	info.put("Parameters", details.getParameters().toString());

	info.put("Output", details.getConsoleOutputText());
	return info;
    }
}
