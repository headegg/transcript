package uk.org.sappho.applications.transcript.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * Provide Transcript PUT property services to Maven builds.
 *
 * @goal run
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

    private final Logger LOG = Logger.getLogger(TranscriptPutMojo.class.getName());

    public void execute() throws MojoExecutionException {

        try {
            LOG.info("Updating:      " + environment + ":" + application + ":" + key + " to " + value);
            LOG.info("Committing as: " + commitMessage);
            String url = baseUrl + "/" + environment + "/" + application + "/" + key +
                    "?commit.message=" + URLEncoder.encode(commitMessage, "UTF-8");
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(value);
            outputStreamWriter.close();
            LOG.info("Update completed");
        } catch (Throwable throwable) {
            throw new MojoExecutionException("Unable to PUT updated data to Devops Store", throwable);
        }
    }
}
