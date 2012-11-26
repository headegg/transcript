/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class BasicReportData {

    private final Map<String, Object> data;

    public BasicReportData(Map<String, Object> data) {

        this.data = data;
    }

    public Object get(String key) {

        return data.get(key);
    }

    public void put(String key, Object object) {

        data.put(key, object);
    }

    public String urlEncode(String plainText) throws UnsupportedEncodingException {

        return URLEncoder.encode(plainText, "UTF-8");
    }
}
