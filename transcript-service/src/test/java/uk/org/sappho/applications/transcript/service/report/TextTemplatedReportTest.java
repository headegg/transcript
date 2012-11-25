/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextTemplatedReportTest {

    @Test
    public void testGoodParameters() throws TranscriptException, IOException {

        TestService<TextTemplatedReport> testService = new TestService<TextTemplatedReport>();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("extra-param", "extra-value");
        parameters.put("extraParam", "extraValue");
        parameters.put("true-param", "true");
        parameters.put("false-param", "false");
        parameters.put("bad-boolean-param", "bad-value");
        TextTemplatedReport textTemplatedReport =
                testService.getService(TextTemplatedReport.class,
                        "target/test-work-space",
                        null, true, false, "def-value", false, false, parameters);
        String report = textTemplatedReport.generate("test-params", testService.getTemplateloader());
        Assert.assertEquals("Parameters are not as expected",
                FileUtils.readFileToString(new File("src/test/resources/expected-reports/test-params.txt")),
                report);
    }

    @Test(expected = TranscriptException.class)
    public void testBadWorkingPath() throws TranscriptException, IOException {

        TestService<TextTemplatedReport> testService = new TestService<TextTemplatedReport>();
        TextTemplatedReport textTemplatedReport =
                testService.getService(TextTemplatedReport.class,
                        null, null, true, false, "", false, false, new HashMap<String, String>());
        textTemplatedReport.generate("test-params", testService.getTemplateloader());
    }
}
