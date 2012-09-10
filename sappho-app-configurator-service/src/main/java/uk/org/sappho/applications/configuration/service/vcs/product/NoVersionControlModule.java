/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs.product;

import uk.org.sappho.applications.configuration.service.AbstractServiceModule;
import uk.org.sappho.applications.configuration.service.vcs.VersionControlSystem;

import java.util.ArrayList;
import java.util.List;

public class NoVersionControlModule extends AbstractServiceModule {

    @Override
    protected void configure() {

        bind(VersionControlSystem.class).to(NoVersionControl.class);
    }

    @Override
    protected List<String> getRequiredProperties() {

        return new ArrayList<String>();
    }
}
