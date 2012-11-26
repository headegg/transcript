/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.freemarker;

import freemarker.cache.TemplateLoader;

import java.io.Reader;
import java.io.StringReader;

public class StringTemplateLoader implements TemplateLoader {

    private final String template;

    public StringTemplateLoader(String template) {

        this.template = template;
    }

    @Override
    public Object findTemplateSource(String name) {

        return template;
    }

    @Override
    public long getLastModified(Object templateSource) {

        return System.currentTimeMillis();
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) {

        return new StringReader((String) templateSource);
    }

    @Override
    public void closeTemplateSource(Object templateSource) {
    }
}
