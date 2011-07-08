package com.mycompany.jira.plugins.conditions;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.user.User;
import com.atlassian.jira.exception.DataAccessException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;

import java.util.Map;
import java.util.Collection;

import org.apache.log4j.Logger;

public class ConditionWithConfig extends AbstractJiraCondition {
    private static final Logger log = Logger.getLogger(ConditionWithConfig.class);

    public boolean passesCondition(Map transientVars,
                                   Map args, 
                                   PropertySet ps) throws WorkflowException {
        log.debug("Checking the condition");
        
        try {
            String configparam = (String) args.get("mycurrentvalue");
            log.debug("This condition was configured with a value: " + configparam);
            if (configparam == null) {
                // If the condition was not configured, it isn't active
                return true;
            }

            Issue issue = getIssue(transientVars);
            Collection<Version> versions = issue.getFixVersions();
            for (Version version: versions) {
                if (version.getName().equals(configparam)) {
                    // Only show the workflow transition if the issue
                    // has a Fix Version that matches the configured
                    // value by name.
                    return true;
                }
            }
            return false;
        } catch (DataAccessException e) {
            log.warn("Failed to find the expected issue in the database");
        }
        return false;
    }

}
