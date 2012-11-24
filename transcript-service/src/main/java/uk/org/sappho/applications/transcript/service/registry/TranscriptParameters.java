/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.util.Map;

public class TranscriptParameters {

    private String workingCopyPath;
    private String workingCopyId;
    private boolean readOnly;
    private boolean useCache;
    private boolean includeVersionControlProperties;
    private String defaultValue;
    private boolean merge;
    private boolean failOnValueChange;
    private Map<String, String> parameters;

    public TranscriptParameters(String workingCopyPath,
                                String workingCopyId,
                                boolean readOnly,
                                boolean useCache,
                                boolean includeVersionControlProperties,
                                String defaultValue,
                                boolean merge,
                                boolean failOnValueChange,
                                Map<String, String> parameters) {

        this.workingCopyPath = workingCopyPath;
        this.workingCopyId = workingCopyId;
        this.readOnly = readOnly;
        this.useCache = useCache;
        this.includeVersionControlProperties = includeVersionControlProperties;
        this.defaultValue = defaultValue;
        this.merge = merge;
        this.failOnValueChange = failOnValueChange;
        this.parameters = parameters;
    }

    public String getWorkingCopyPath() throws TranscriptException {

        if (workingCopyPath == null || workingCopyPath.length() == 0) {
            throw new TranscriptException("Working copy path not specified");
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

    public String getDefaultValue() {

        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {

        this.defaultValue = defaultValue;
    }

    public boolean isMerge() {

        return merge;
    }

    public void setMerge(boolean merge) {

        this.merge = merge;
    }

    public boolean isFailOnValueChange() {

        return failOnValueChange;
    }

    public void setFailOnValueChange(boolean failOnValueChange) {

        this.failOnValueChange = failOnValueChange;
    }

    public String get(String key) {

        return get(key, "");
    }

    public String get(String key, String defaultValue) {

        return parameters.containsKey(key) ? parameters.get(key) : defaultValue;
    }

    public boolean get(String key, boolean defaultValue) {

        boolean value = defaultValue;
        if (parameters.containsKey(key)) {
            String rawValue = get(key);
            boolean isTrue = rawValue.equalsIgnoreCase("true");
            if (isTrue || rawValue.equalsIgnoreCase("false")) {
                value = isTrue;
            }
        }
        return value;
    }
}
