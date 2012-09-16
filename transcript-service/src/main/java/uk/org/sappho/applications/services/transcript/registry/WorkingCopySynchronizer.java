/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class WorkingCopySynchronizer {

    private Map<String, Object> locks = new HashMap<String, Object>();

    synchronized Object getLock(String id) {

        Object lock = locks.get(id);
        if (lock == null) {
            lock = new Object();
            locks.put(id, lock);
        }
        return lock;
    }
}
