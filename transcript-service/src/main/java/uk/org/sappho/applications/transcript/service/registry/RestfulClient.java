/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

import com.google.gson.Gson;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestfulClient {

    public Object get(String url) throws TranscriptException {

        Object reply;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            inputStream = httpURLConnection.getInputStream();
            reply = new Gson().fromJson(new InputStreamReader(inputStream), Object.class);
        } catch (Throwable throwable) {
            throw new TranscriptException("Unable to GET from " + url, throwable);
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
        return reply;
    }
}
