/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry.vcs.subversion;

import com.google.inject.AbstractModule;
import uk.org.sappho.applications.transcript.service.registry.vcs.VersionControlSystem;

public class SubversionModule extends AbstractModule {

    public final SubversionParameters subversionParameters;

    public SubversionModule(SubversionParameters subversionParameters) {

        this.subversionParameters = subversionParameters;
    }

    @Override
    protected void configure() {

        bind(SubversionParameters.class).toInstance(subversionParameters);
        bind(VersionControlSystem.class).to(SubversionVersionControl.class);
    }
}
