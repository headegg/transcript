/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry.vcs.product;

import uk.org.sappho.applications.services.transcript.registry.AbstractServiceModule;
import uk.org.sappho.applications.services.transcript.registry.vcs.VersionControlSystem;

import java.util.Arrays;
import java.util.List;

public class SubversionModule extends AbstractServiceModule {

    @Override
    protected void configure() {

        bind(VersionControlSystem.class).to(SubversionVersionControl.class);
    }

    @Override
    public List<String> getRequiredProperties() {

        return Arrays.asList("url", "username", "password", "commit.message", "svn", "trust.server.certificate");
    }
}
