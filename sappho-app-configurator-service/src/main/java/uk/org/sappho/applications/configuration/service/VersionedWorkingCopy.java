package uk.org.sappho.applications.configuration.service;

public interface VersionedWorkingCopy {

    public void update(String path) throws ConfigurationException;
}
