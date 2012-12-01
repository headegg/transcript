/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public abstract class ApplicationConfiguration {

    private final String baseUrl;
    private final String environment;
    private final String application;
    private final boolean override;
    private final long cacheTime;
    private final boolean logIgnores;
    private final boolean logAllPropertyPuts;
    private long lastUpdateTime;

    public ApplicationConfiguration(String baseUrl, String environment, String application, boolean override,
                                    long cacheTime, boolean logIgnores, boolean logAllPropertyPuts) {

        this.baseUrl = baseUrl;
        this.environment = environment;
        this.application = application;
        this.override = override;
        this.cacheTime = cacheTime;
        this.logIgnores = logIgnores;
        this.logAllPropertyPuts = logAllPropertyPuts;
        lastUpdateTime = System.currentTimeMillis() - cacheTime;
    }

    public void refresh(Properties properties) throws IOException {

        long timeNow = System.currentTimeMillis();
        if ((timeNow - lastUpdateTime) >= cacheTime) {
            lastUpdateTime = timeNow;
            boolean newProperties = false;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                log("Connecting to " + baseUrl);
                log("Getting properties for " + environment + ":" + application);
                String url = baseUrl + "/" + environment + "/" + application;
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                inputStream = httpURLConnection.getInputStream();
                Map<String, Object> jsonProperties =
                        (Map<String, Object>) new Gson().fromJson(new InputStreamReader(inputStream), Object.class);
                inputStream.close();
                for (String key : jsonProperties.keySet()) {
                    if (!properties.containsKey(key)) {
                        newProperties = true;
                        break;
                    }
                }
                if (newProperties || override) {
                    for (String key : jsonProperties.keySet()) {
                        if (!override && properties.containsKey(key)) {
                            if (logIgnores) {
                                log(key + " is already defined so ignoring it here");
                            }
                        } else {
                            Object oldValue = properties.get(key);
                            Object value = jsonProperties.get(key);
                            properties.put(key, value);
                            if (value != null && (logAllPropertyPuts || oldValue == null || !value.equals(oldValue))) {
                                log(key + ": " + value.toString());
                            }
                        }
                    }
                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable throwable) {
                    }
                }
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Throwable throwable) {
                    }
                }
            }
            log(newProperties ? "Properties are now available" : "There are no new properties");
        }
    }

    protected abstract void log(String message);
}
