/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.vcs.mock;

import com.google.inject.AbstractModule;
import uk.org.sappho.applications.transcript.service.vcs.VersionControlSystem;

public class MockVersionControlModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(VersionControlSystem.class).to(MockVersionControl.class);
    }
}
