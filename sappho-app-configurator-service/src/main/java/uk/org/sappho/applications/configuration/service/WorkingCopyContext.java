/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

public class WorkingCopyContext {

    private String headRevision = "unknown";
    private String repository = "unknown";

    public void setHeadRevision(String headRevision) {

        this.headRevision = headRevision;
    }

    public String getHeadRevision() {

        return headRevision;
    }

    public void setRepository(String repository) {

        this.repository = repository;
    }

    public String getRepository() {

        return repository;
    }
}
