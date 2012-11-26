/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.report.restful.client;

import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.util.SortedMap;
import java.util.TreeMap;

public class RestfulClient {

    private final SortedMap<String, RestfulClientState> clientStates = new TreeMap<String, RestfulClientState>();

    public void get(String url, String key, int connectTimeout, int readTimeout, int expectedResponseCode)
            throws InterruptedException {

        RestfulClientState restfulClientState = clientStates.get(key);
        if (restfulClientState == null) {
            restfulClientState = new RestfulClientState();
            clientStates.put(key, restfulClientState);
        }
        Object threadLock = restfulClientState.getThreadLock();
        synchronized (threadLock) {
            new RestfulClientThread(url, key, connectTimeout, readTimeout, expectedResponseCode, restfulClientState)
                    .start();
            threadLock.wait();
        }
    }

    public byte[] get(String key) throws Throwable {

        if (!clientStates.containsKey(key)) {
            throw new TranscriptException("There has been no RESTful GET on " + key);
        }
        RestfulClientState restfulClientState = clientStates.get(key);
        synchronized (restfulClientState.getThreadLock()) {
            Throwable throwable = restfulClientState.getThrowable();
            if (throwable != null) {
                throw throwable;
            }
            return restfulClientState.getData();
        }
    }
}
