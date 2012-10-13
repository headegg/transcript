/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.importers.transcript;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

public class Import {

    public void importProperties() throws IOException {

        String directory = System.getProperty("directory");
        if (directory == null || directory.length() == 0) {
            directory = ".";
        }
        String[] propertiesFiles = new File(directory).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".properties");
            }
        });
        for (String filename : propertiesFiles) {
            Properties properties = new Properties();
            FileReader fileReader = new FileReader(new File(directory, filename));
            properties.load(fileReader);
            fileReader.close();
            SortedMap<String, String> sortedProperties = new TreeMap<String, String>();
            for (String key : properties.stringPropertyNames()) {
                sortedProperties.put(key, properties.getProperty(key));
            }
            JsonWriter jsonWriter = new JsonWriter(new FileWriter(
                    new File(directory, filename.substring(0, filename.length() - 10) + "json")));
            jsonWriter.setIndent("    ");
            jsonWriter.setHtmlSafe(true);
            new Gson().toJson(sortedProperties, sortedProperties.getClass(), jsonWriter);
            jsonWriter.close();
        }
    }

    public static void main(String[] args) throws IOException {

        new Import().importProperties();
    }
}
