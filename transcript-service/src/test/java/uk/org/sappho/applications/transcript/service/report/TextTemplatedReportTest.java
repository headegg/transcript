/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextTemplatedReportTest {

    private static final String TEST_WORK_SPACE = "target/test-work-space";
    private static final TestServiceContext<TextTemplatedReport> SERVICE_CONTEXT =
            new TestServiceContext<TextTemplatedReport>(TextTemplatedReport.class);

    @Before
    public void setUp() throws IOException {

        File directory = new File(TEST_WORK_SPACE);
        if (directory.exists()) {
            FileUtils.forceDelete(directory);
        }
    }

    @Test
    public void testGoodParameters() throws TranscriptException, IOException {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("extra-param", "extra-value");
        parameters.put("extraParam", "extraValue");
        parameters.put("true-param", "true");
        parameters.put("false-param", "false");
        parameters.put("bad-boolean-param", "bad-value");
        TextTemplatedReport textTemplatedReport =
                SERVICE_CONTEXT.getService(TEST_WORK_SPACE, null, true, false, "def-value", false, false, parameters);
        String report = textTemplatedReport.generate("test-params", SERVICE_CONTEXT.getTemplateloader());
        Assert.assertEquals("Parameters are not as expected",
                FileUtils.readFileToString(new File("src/test/files/expected-reports/test-params.txt")),
                report);
    }

    @Test(expected = TranscriptException.class)
    public void testBadWorkingPath() throws TranscriptException, IOException {

        TextTemplatedReport textTemplatedReport =
                SERVICE_CONTEXT.getService(null, null, true, false, "", false, false,
                        new HashMap<String, String>());
        textTemplatedReport.generate("test-params", SERVICE_CONTEXT.getTemplateloader());
    }

    @Test
    public void testDataDictionary() throws TranscriptException, IOException {

        TextTemplatedReport textTemplatedReport =
                SERVICE_CONTEXT.getService(TEST_WORK_SPACE, null, true, false, "", false, false,
                        new HashMap<String, String>());
        String report = textTemplatedReport.generate("test-data-dictionary", SERVICE_CONTEXT.getTemplateloader());
        Assert.assertEquals("Data dictionary is not as expected",
                FileUtils.readFileToString(new File("src/test/files/expected-reports/test-data-dictionary.txt")),
                report);
    }
}
