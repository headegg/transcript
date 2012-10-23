/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry;

import java.util.HashMap;
import java.util.Map;

public class WorkingCopyLocking {

    private Map<String, Object> locks = new HashMap<String, Object>();

    synchronized public Object getLock(String workingCopyId) {

        Object lock = locks.get(workingCopyId);
        if (lock == null) {
            lock = new Object();
            locks.put(workingCopyId, lock);
        }
        return lock;
    }
}
