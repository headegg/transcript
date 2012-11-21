/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.maven.plugin;

import com.google.gson.Gson;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * Provide Transcript GET property services to Maven builds.
 *
 * @goal get
 * @phase validate
 */
public class GetJsonMojo extends AbstractMojo {

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
     * Allow defined property override, defaulting to false.
     *
     * @parameter expression="${override}" default-value=false
     */
    private boolean override;

    /**
     * The Maven project context.
     *
     * @parameter default-value="${project}"
     */
    private MavenProject mavenProject;

    public void execute() throws MojoExecutionException {

        boolean newProperties = false;
        try {
            getLog().info("Connecting to " + baseUrl);
            getLog().info("Getting properties from " + environment + ":" + application);
            String url = baseUrl + "/" + environment + "/" + application;
            InputStream inputStream = new URL(url).openConnection().getInputStream();
            Map<String, Object> jsonProperties =
                    (Map<String, Object>) new Gson().fromJson(new InputStreamReader(inputStream), Object.class);
            inputStream.close();
            Properties mavenProperties = mavenProject.getProperties();
            for (String key : jsonProperties.keySet()) {
                if (!mavenProperties.containsKey(key)) {
                    newProperties = true;
                    break;
                }
            }
            if (newProperties || override) {
                for (String key : jsonProperties.keySet()) {
                    if (!override && mavenProperties.containsKey(key)) {
                        getLog().info(key + " is already defined so ignoring it here");
                    } else {
                        Object value = jsonProperties.get(key);
                        mavenProperties.put(key, value);
                        getLog().info(key + ": " + value.toString());
                    }
                }
            }
        } catch (Throwable throwable) {
            throw new MojoExecutionException("Unable to GET properties", throwable);
        }
        getLog().info(newProperties ?
                "Properties are now available to build" : "There are no new properties to import");
    }
}
