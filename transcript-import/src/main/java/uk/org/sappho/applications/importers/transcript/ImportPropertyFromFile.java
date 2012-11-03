/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.importers.transcript;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

public class ImportPropertyFromFile {

    public void importProperty() throws IOException {

        File file = new File("property.txt");
        byte[] propertyBytes = new byte[(int) file.length()];
        InputStream stream = new FileInputStream(file);
        stream.read(propertyBytes);
        stream.close();
        Map<String, String> properties = new TreeMap<String, String>();
        properties.put("key", new String(propertyBytes));
        JsonWriter jsonWriter = new JsonWriter(new FileWriter(new File("property.json")));
        jsonWriter.setIndent("    ");
        jsonWriter.setHtmlSafe(true);
        new Gson().toJson(properties, properties.getClass(), jsonWriter);
        jsonWriter.close();
    }

    public static void main(String[] args) throws IOException {

        new ImportPropertyFromFile().importProperty();
    }
}
