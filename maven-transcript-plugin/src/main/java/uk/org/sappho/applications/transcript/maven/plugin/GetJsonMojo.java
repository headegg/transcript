/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import uk.org.sappho.applications.transcript.restful.client.ApplicationConfiguration;

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
     * RESTful connection timeout.
     *
     * @parameter expression="${connectTimeout}" default-value=5000
     */
    private int connectTimeout;

    /**
     * RESTful read timeout.
     *
     * @parameter expression="${readTimeout}" default-value=5000
     */
    private int readTimeout;

    /**
     * The Maven project context.
     *
     * @parameter default-value="${project}"
     */
    private MavenProject mavenProject;

    public void execute() throws MojoExecutionException {

        try {
            ApplicationConfiguration applicationConfiguration =
                    new ApplicationConfiguration(baseUrl, environment, application, override, 0, true, true,
                            connectTimeout, readTimeout) {
                        @Override
                        protected void log(String message) {
                            getLog().info(message);
                        }
                    };
            applicationConfiguration.refresh(mavenProject.getProperties());
        } catch (Throwable throwable) {
            throw new MojoExecutionException("Unable to GET properties", throwable);
        }
    }
}
