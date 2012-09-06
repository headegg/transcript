/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service.vcs.product;

import uk.org.sappho.applications.configuration.service.vcs.VersionControlSystem;

import java.util.Map;

public class NoVersionControl implements VersionControlSystem {

    public void update(String filename, Map<String, String> workingCopyProperties) {
    }

    public void checkout() {
    }
}
