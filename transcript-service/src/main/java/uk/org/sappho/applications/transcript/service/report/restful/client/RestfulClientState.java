/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.restful.client;

public class RestfulClientState {

    private byte[] data = null;
    private Object threadLock = new Object();
    private Throwable throwable = null;

    public byte[] getData() {

        return data;
    }

    public void setData(byte[] data) {

        this.data = data;
    }

    public Object getThreadLock() {

        return threadLock;
    }

    public Throwable getThrowable() {

        return throwable;
    }

    public void setThrowable(Throwable throwable) {

        this.throwable = throwable;
    }
}
