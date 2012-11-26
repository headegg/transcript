/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report;

import org.apache.commons.io.IOUtils;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestfulClientThread extends Thread {

    private final String url;
    private final String key;
    private final int connectTimeout;
    private final int readTimeout;
    private final int expectedResponseCode;
    private final RestfulClientState restfulClientState;

    public RestfulClientThread(String url, String key, int connectTimeout, int readTimeout, int expectedResponseCode,
                               RestfulClientState restfulClientState) {

        this.url = url;
        this.key = key;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.expectedResponseCode = expectedResponseCode;
        this.restfulClientState = restfulClientState;
    }

    public void run() {

        Object threadLock = restfulClientState.getThreadLock();
        synchronized (threadLock) {
            threadLock.notify();
            restfulClientState.setData(null);
            restfulClientState.setThrowable(null);
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setConnectTimeout(connectTimeout);
                httpURLConnection.setReadTimeout(readTimeout);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("User-Agent", "TranscriptRESTfulClient/1.0");
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                restfulClientState.setData(IOUtils.toByteArray(inputStream));
                inputStream.close();
                inputStream = null;
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != expectedResponseCode) {
                    throw new TranscriptException("Expected " + key + " response " + expectedResponseCode +
                            " but got " + responseCode + " instead");
                }
            } catch (Throwable throwable) {
                restfulClientState.setThrowable(throwable);
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
        }
    }
}
