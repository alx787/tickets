package ru.ath.asu.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class portalAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(portalAction.class);

    @Override
    public String execute() throws Exception {

        return super.execute(); //returns SUCCESS
    }
}
