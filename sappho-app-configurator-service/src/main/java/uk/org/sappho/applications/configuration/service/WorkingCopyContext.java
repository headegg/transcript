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
