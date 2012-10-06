/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.restful.transcript.reporter;

import com.google.inject.Inject;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import uk.org.sappho.applications.services.transcript.registry.ConfigurationException;
import uk.org.sappho.applications.services.transcript.registry.Environments;
import uk.org.sappho.applications.services.transcript.registry.Properties;
import uk.org.sappho.applications.services.transcript.registry.WorkingCopy;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class MultiPlatformApplicationPropertiesReport {

    private WorkingCopy workingCopy;
    private Environments environments;
    private Properties properties;

    @Inject
    public MultiPlatformApplicationPropertiesReport(WorkingCopy workingCopy,
                                                    Environments environments,
                                                    Properties properties) {

        this.workingCopy = workingCopy;
        this.environments = environments;
        this.properties = properties;
    }

    public String generate(String[] environmentList, String application, boolean includeVersionControlProperties,
                           Configuration freemarkerConfiguration)
            throws ConfigurationException {

        if (environmentList == null || environmentList.length == 0) {
            environmentList = environments.getEnvironmentNames();
        } else {
            workingCopy.getUpToDatePath("");
        }
        SortedMap<String, Map<String, String>> allProperties = new TreeMap<String, Map<String, String>>();
        for (String environment : environmentList) {
            Map<String, String> rawProperties = properties.getAllProperties(
                    environment, application, includeVersionControlProperties);
            for (String key : rawProperties.keySet()) {
                Map<String, String> values = allProperties.get(key);
                if (values == null) {
                    values = new HashMap<String, String>();
                    allProperties.put(key, values);
                }
                values.put(environment, rawProperties.get(key));
            }
        }
        try {
            freemarkerConfiguration.setObjectWrapper(new DefaultObjectWrapper());
            Template template = freemarkerConfiguration.getTemplate("all-app-props.ftl");
            StringWriter stringWriter = new StringWriter();
            List<String> environments = new Vector<String>();
            for (String environment : environmentList) {
                environments.add(environment);
            }
            List<SimpleHash> keys = new Vector<SimpleHash>();
            for (String key : allProperties.keySet()) {
                SimpleHash keyData = new SimpleHash();
                keyData.put("id", key);
                List<String> values = new Vector<String>();
                for (String environment : environmentList) {
                    values.add(allProperties.get(key).get(environment));
                }
                keyData.put("values", values);
                keys.add(keyData);
            }
            SimpleHash root = new SimpleHash();
            root.put("environments", environments);
            root.put("keys", keys);
            template.process(root, stringWriter);
            stringWriter.close();
            return stringWriter.toString();
        } catch (Throwable throwable) {
            throw new ConfigurationException("Unable to generate HTML report", throwable);
        }
    }
}
