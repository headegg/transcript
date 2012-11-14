/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service;

public class TranscriptException extends Exception {

    public TranscriptException(String message) {

        super(message);
    }

    public TranscriptException(String message, Throwable throwable) {

        super(message + "\n" + throwable.getMessage(), throwable);
    }
}
