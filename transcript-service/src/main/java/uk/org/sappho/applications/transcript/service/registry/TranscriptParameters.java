/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

public class TranscriptParameters {

    private String workingCopyPath;
    private String workingCopyId;
    private boolean readOnly;
    private boolean useCache;
    private boolean includeVersionControlProperties;
    private String dictionaryEnvironment;
    private String dictionaryApplication;
    private String templatesEnvironment;
    private String templatesApplication;
    private String reportId;
    private String[] environments;
    private String[] applications;
    private String[] keys;
    private boolean includeUndefinedEnvironments;

    public TranscriptParameters(String workingCopyPath,
                                String workingCopyId,
                                boolean readOnly,
                                boolean useCache,
                                boolean includeVersionControlProperties,
                                String dictionaryEnvironment,
                                String dictionaryApplication,
                                String templatesEnvironment,
                                String templatesApplication,
                                String reportId,
                                String[] environments,
                                String[] applications,
                                String[] keys,
                                boolean includeUndefinedEnvironments) {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.readOnly = readOnly;
        this.useCache = useCache;
        this.includeVersionControlProperties = includeVersionControlProperties;
        this.dictionaryEnvironment = dictionaryEnvironment;
        this.dictionaryApplication = dictionaryApplication;
        this.templatesEnvironment = templatesEnvironment;
        this.templatesApplication = templatesApplication;
        this.reportId = reportId;
        this.environments = environments;
        this.applications = applications;
        this.keys = keys;
        this.includeUndefinedEnvironments = includeUndefinedEnvironments;
    }

    public String getWorkingCopyPath() throws ConfigurationException {

        if (workingCopyPath == null || workingCopyPath.length() == 0) {
            throw new ConfigurationException("Working copy path not specified");
        }
        return workingCopyPath;
    }

    public void setWorkingCopyPath(String workingCopyPath) {

        this.workingCopyPath = workingCopyPath;
    }

    public String getWorkingCopyId() {

        return workingCopyId != null && workingCopyId.length() != 0 ? workingCopyId : "default";
    }

    public void setWorkingCopyId(String workingCopyId) {

        this.workingCopyId = workingCopyId;
    }

    public boolean isReadOnly() {

        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {

        this.readOnly = readOnly;
    }

    public boolean isUseCache() {

        return useCache;
    }

    public void setUseCache(boolean useCache) {

        this.useCache = useCache;
    }

    public boolean isIncludeVersionControlProperties() {

        return includeVersionControlProperties;
    }

    public void setIncludeVersionControlProperties(boolean includeVersionControlProperties) {

        this.includeVersionControlProperties = includeVersionControlProperties;
    }

    public String getDictionaryEnvironment() {

        return dictionaryEnvironment != null && dictionaryEnvironment.length() != 0 ?
                dictionaryEnvironment : ".devops";
    }

    public void setDictionaryEnvironment(String dictionaryEnvironment) {

        this.dictionaryEnvironment = dictionaryEnvironment;
    }

    public String getDictionaryApplication() {

        return dictionaryApplication != null && dictionaryApplication.length() != 0 ?
                dictionaryApplication : ".dictionary";
    }

    public void setDictionaryApplication(String dictionaryApplication) {

        this.dictionaryApplication = dictionaryApplication;
    }

    public String getTemplatesEnvironment() {

        return templatesEnvironment != null && templatesEnvironment.length() != 0 ?
                templatesEnvironment : ".devops";
    }

    public void setTemplatesEnvironment(String templatesEnvironment) {

        this.templatesEnvironment = templatesEnvironment;
    }

    public String getTemplatesApplication() {

        return templatesApplication != null && templatesApplication.length() != 0 ?
                templatesApplication : ".templates";
    }

    public void setTemplatesApplication(String templatesApplication) {

        this.templatesApplication = templatesApplication;
    }

    public String getReportId() {

        return reportId;
    }

    public void setReportId(String reportId) {

        this.reportId = reportId;
    }

    public String[] getEnvironments() {

        return environments != null ? environments : new String[0];
    }

    public void setEnvironments(String[] environments) {

        this.environments = environments;
    }

    public String getEnvironment() {

        String environment = null;
        if (environments != null && environments.length == 1) {
            environment = environments[0];
        }
        return environment;
    }

    public void setEnvironment(String environment) {

        environments = new String[]{environment};
    }

    public String[] getApplications() {

        return applications != null ? applications : new String[0];
    }

    public void setApplications(String[] applications) {

        this.applications = applications;
    }

    public String getApplication() {

        String application = null;
        if (applications != null && applications.length == 1) {
            application = applications[0];
        }
        return application;
    }

    public void setApplication(String application) {

        applications = new String[]{application};
    }

    public String[] getKeys() {

        return keys != null ? keys : new String[0];
    }

    public void setKeys(String[] keys) {

        this.keys = keys;
    }

    public boolean isIncludeUndefinedEnvironments() {

        return includeUndefinedEnvironments;
    }

    public void setIncludeUndefinedEnvironments(boolean includeUndefinedEnvironments) {

        this.includeUndefinedEnvironments = includeUndefinedEnvironments;
    }
}
