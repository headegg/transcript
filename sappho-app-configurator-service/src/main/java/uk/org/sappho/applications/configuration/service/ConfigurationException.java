package uk.org.sappho.applications.configuration.service;

public class ConfigurationException extends Exception {

    public ConfigurationException(String message) {

        super(message);
    }

    public ConfigurationException(String message, Throwable throwable) {

        super(message, throwable);
    }
}
