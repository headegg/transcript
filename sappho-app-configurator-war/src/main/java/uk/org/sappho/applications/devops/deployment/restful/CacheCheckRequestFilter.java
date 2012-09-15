/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.devops.deployment.restful;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

public class CacheCheckRequestFilter implements ContainerRequestFilter {

    private long lastGetTime = 0;

    @Context
    private ServletContext servletContext;

    @Override
    synchronized public ContainerRequest filter(ContainerRequest request) {

        MultivaluedMap<String, String> parameters = request.getQueryParameters();
        String useCache = "false";
        if (request.getMethod().equals("GET")) {
            if (parameters.containsKey("use.cache")) {
                useCache = null;
            } else {
                long now = System.currentTimeMillis();
                long timeSinceLastGet = now - lastGetTime;
                lastGetTime = now;
                String cacheTimeString = servletContext.getInitParameter("cache.time");
                long cacheTime = cacheTimeString != null ? Long.parseLong(cacheTimeString) : 10000;
                useCache = new Boolean(timeSinceLastGet < cacheTime).toString();
            }
        } else {
            lastGetTime = 0;
        }
        if (useCache != null) {
            parameters.putSingle("use.cache", useCache);
        };
        return request;
    }
}
