/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.maven.plugin;

import com.google.gson.Gson;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.SortedMap;

/**
 * Provide Transcript PUT property services to Maven builds.
 *
 * @goal put
 */
public class PutJsonMojo extends AbstractMojo {

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
     * Property data.
     *
     * @parameter expression="${data}"
     */
    private SortedMap<String, Object> data;

    /**
     * Merge to current set, defaulting to true.
     *
     * @parameter expression="${merge}" default-value=true
     */
    private boolean isMerge;

    /**
     * Fail is values change, defaulting to false.
     *
     * @parameter expression="${failOnValueChange}" default-value=false
     */
    private boolean failOnValueChange;

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
            getLog().info("Updating " + environment + ":" + application);
            for (String key : data.keySet()) {
                getLog().info(key + ": " + data.get(key).toString());
            }
            getLog().info("Committing as " + commitMessage);
            String json = new Gson().toJson(data);
            String url = baseUrl + "/" + environment + "/" + application +
                    "?merge=" + isMerge + "&fail.change=" + failOnValueChange +
                    "&commit.message=" + URLEncoder.encode(commitMessage, "UTF-8");
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Content-Length", "" + json.length());
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(json.getBytes(), 0, json.length());
            outputStream.flush();
            outputStream.close();
            responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
        } catch (Throwable throwable) {
            throw new MojoExecutionException("Unable to PUT updated properties", throwable);
        }
        if (responseCode != 204) {
            throw new MojoExecutionException("Unable to PUT updated properties - HTTP response code was " +
                    responseCode);
        }
        getLog().info("Property update completed");
    }
}
