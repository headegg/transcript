/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.AbstractModule;

import java.util.List;
import java.util.Map;

abstract public class AbstractServiceModule extends AbstractModule {

    abstract protected List<String> getRequiredProperties();

    public void fixProperties(Map<String, String> properties) {

        for (String requiredParameter : getRequiredProperties()) {
            if (!properties.containsKey(requiredParameter)) {
                properties.put(requiredParameter, "");
            }
        }
    }
}
