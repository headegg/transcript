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
 * @goal put
 */
public class TranscriptPutMojo extends AbstractMojo {

    /**
     * Transctipt service base URL.
     *
     * @parameter expression="${baseUrl}" default-value="${transcript.baseUrl}"
     */
    private String baseUrl;

    /**
     * The DevOps environment.
     *
     * @parameter expression="${environment}" default-value="${transcript.environment}"
     */
    private String environment;

    /**
     * The DevOps application.
     *
     * @parameter expression="${application}" default-value="${project.artifactId}"
     */
    private String application;

    /**
     * The DevOps property key.
     *
     * @parameter expression="${key}" default-value="application.release.version"
     */
    private String key;

    /**
     * The DevOps property value.
     *
     * @parameter expression="${value}" default-value="${project.version}"
     */
    private String value;

    /**
     * The DevOps commit message.
     *
     * @parameter expression="${commitMessage}"
     */
    private String commitMessage;

    public void execute() throws MojoExecutionException {

        int responseCode = 0;
        try {
            getLog().info("Connecting to DevOps store at " + baseUrl);
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
            throw new MojoExecutionException("Unable to PUT updated data to Devops Store", throwable);
        }
        if (responseCode != 204) {
            throw new MojoExecutionException("Unable to PUT updated data to Devops Store - HTTP response code was " +
                    responseCode);
        }
        getLog().info("Update completed");
    }
}
