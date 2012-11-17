package uk.org.sappho.applications.transcript.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Provide Transcript PUT property services to Maven builds.
 *
 * @goal put-string
 */
public class TranscriptPutMojo extends AbstractMojo {

    /**
     * Service base URL.
     *
     * @parameter expression="${baseUrl}" default-value="${transcript.baseUrl}"
     */
    private String baseUrl;

    /**
     * Environment, always required.
     *
     * @parameter expression="${environment}"
     */
    private String environment;

    /**
     * Application, defaulting to artifact ID.
     *
     * @parameter expression="${application}" default-value="${project.artifactId}"
     */
    private String application;

    /**
     * Property key, with default for version tracking.
     *
     * @parameter expression="${key}" default-value="application.release.version"
     */
    private String key;

    /**
     * Property value, defaulting to project version.
     *
     * @parameter expression="${value}" default-value="${project.version}"
     */
    private String value;

    /**
     * Commit message.
     *
     * @parameter expression="${commitMessage}"
     */
    private String commitMessage;

    public void execute() throws MojoExecutionException {

        int responseCode = 0;
        try {
            getLog().info("Connecting to " + baseUrl);
            getLog().info("Updating " + environment + ":" + application + ":" + key + " to " + value);
            getLog().info("Committing as " + commitMessage);
            String url = baseUrl + "/" + environment + "/" + application + "/" + key +
                    "?commit.message=" + URLEncoder.encode(commitMessage, "UTF-8");
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "text/plain");
            httpURLConnection.setRequestProperty("Content-Length", "" + value.length());
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(value.getBytes(), 0, value.length());
            outputStream.flush();
            outputStream.close();
            responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
        } catch (Throwable throwable) {
            throw new MojoExecutionException("Unable to PUT updated property", throwable);
        }
        if (responseCode != 204) {
            throw new MojoExecutionException("Unable to PUT updated property - HTTP response code was " +
                    responseCode);
        }
        getLog().info("Property update completed");
    }
}
