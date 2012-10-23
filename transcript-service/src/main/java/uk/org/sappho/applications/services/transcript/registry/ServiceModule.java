/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import uk.org.sappho.applications.services.transcript.registry.vcs.product.SubversionModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceModule extends AbstractModule {

    private final static Map<String, Class<? extends AbstractServiceModule>> VCS_MODULES = new HashMap<String, Class<? extends AbstractServiceModule>>();
    private final static WorkingCopyLocking LOCKING = new WorkingCopyLocking();
    private final Map<String, String> properties = new HashMap<String, String>();

    static {
        VCS_MODULES.put("svn", SubversionModule.class);
    }

    public ServiceModule() throws ConfigurationException {

        if (System.getProperty("use.system.properties", "false").equalsIgnoreCase("true")) {
            for (String key : System.getProperties().stringPropertyNames()) {
                setProperty(key, System.getProperty(key));
            }
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

        bind(WorkingCopyLocking.class).toInstance(LOCKING);
        Names.bindProperties(binder(), properties);
    }

    public Injector getInjector() throws ConfigurationException {

        String workingCopyPath = properties.get("working.copy.path");
        if (workingCopyPath == null || workingCopyPath.length() == 0) {
            throw new ConfigurationException("Application property working.copy.path not specified");
        }
        AbstractServiceModule vcsModule;
        String vcs = properties.get("vcs");
        if (vcs != null && vcs.length() != 0) {
            try {
                vcsModule = VCS_MODULES.get(vcs).newInstance();
            } catch (Throwable throwable) {
                throw new ConfigurationException("Specified VCS " + vcs + " is not supported", throwable);
            }
        } else {
            throw new ConfigurationException("No VCS has been specified");
        }
        for (String key : vcsModule.getRequiredProperties()) {
            fixProperty(key, "");
        }
        fixProperty("working.copy.id", "default");
        fixProperty("use.cache", "false");
        fixProperty("read.only", "false");
        return Guice.createInjector(this, vcsModule);
    }

    private void fixProperty(String key, String defaultValue) {

        String value = properties.get(key);
        if (value == null || value.length() == 0) {
            properties.put(key, defaultValue);
        }
    }
}
