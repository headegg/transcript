/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import uk.org.sappho.applications.configuration.service.vcs.product.NoVersionControlModule;
import uk.org.sappho.applications.configuration.service.vcs.product.SubversionModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceModule extends AbstractModule {

    private final static Map<String, Class<? extends AbstractServiceModule>> vcsModules =
            new HashMap<String, Class<? extends AbstractServiceModule>>();
    private String workingCopyPath;
    private final Map<String, String> properties = new HashMap<String, String>();

    static {
        vcsModules.put("svn", SubversionModule.class);
    }

    public ServiceModule() throws ConfigurationException {

        workingCopyPath = System.getProperty("working.copy.path");
        if (workingCopyPath == null || workingCopyPath.length() == 0) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        for (String key : System.getProperties().stringPropertyNames()) {
            setProperty(key, System.getProperty(key));
        }
    }

    public void setProperty(String key, List<String> values) {

        String value = null;
        if (values != null && values.size() > 0) {
            value = values.get(values.size() - 1);
        }
        setProperty(key, value);
    }

    public void setProperty(String key, String value) {

        properties.put(key, value != null ? value : "");
    }

    @Override
    protected void configure() {

        Names.bindProperties(binder(), properties);
    }

    public Injector getInjector() throws ConfigurationException {

        properties.put("working.copy.path", workingCopyPath);
        String workingCopyId = properties.get("working.copy.id");
        if (workingCopyId == null || workingCopyId.length() == 0) {
            properties.put("working.copy.id", "default");
        }
        AbstractServiceModule vcsModule = new NoVersionControlModule();
        String vcs = properties.get("vcs");
        if (vcs != null && vcs.length() != 0) {
            try {
                vcsModule = vcsModules.get(vcs).newInstance();
            } catch (Throwable throwable) {
                throw new ConfigurationException("Specified VCS \"" + vcs + "\" is not supported");
            }
        }
        vcsModule.fixProperties(properties);
        return Guice.createInjector(this, vcsModule);
    }
}
