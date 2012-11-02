/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.importers.transcript;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;
import java.util.TreeMap;

public class ImportPropertyFromFile {

    public void importProperty() throws IOException {

        File file = new File("property.txt");
        byte[] propertyBytes = new byte[(int) file.length()];
        InputStream stream = new FileInputStream(file);
        stream.read(propertyBytes);
        stream.close();
        SortedMap<String, String> sortedProperties = new TreeMap<String, String>();
        sortedProperties.put("key", new String(propertyBytes));
        JsonWriter jsonWriter = new JsonWriter(new FileWriter(new File("property.json")));
        jsonWriter.setIndent("    ");
        jsonWriter.setHtmlSafe(true);
        new Gson().toJson(sortedProperties, sortedProperties.getClass(), jsonWriter);
        jsonWriter.close();
        String json = "{\"key\":{\"a\":\"1\",\"b\":\"2\"}}";
        StringMap map = new Gson().fromJson(json, StringMap.class);
        String template = (String) ((StringMap) map.get("key")).get("b");
        json = "";
    }

    public static void main(String[] args) throws IOException {

        new ImportPropertyFromFile().importProperty();
    }
}
