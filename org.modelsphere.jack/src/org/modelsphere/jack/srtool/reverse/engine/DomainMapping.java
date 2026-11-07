/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.jack.srtool.reverse.engine;

import java.util.HashMap;
import java.util.Map;

// Class used to defined a mapping between a String value and a domain value (of any type)
// NOTE:  toLowerCase() is applied on all key values for set and get methods

public final class DomainMapping {
    final Map<String, Object> map = new HashMap<>(5);
    final Object defaultValue;

    // defaultvalue: The default value for this domain mapping
    // Can be null (the bd set method will set a null value)
    // Warning: Before using a null value as the default value, make sure that
    // null is allowed on the metafield
    // (Example: primitive type as metafield type does not allow null)
    public DomainMapping(Object defaultvalue) {
        this.defaultValue = defaultvalue;
    }

    public void put(String s, Object value) {
        if (s != null)
            map.put(s.toLowerCase(), value);
    }

    public Object getDefault() {
        return defaultValue;
    }

    public Object get(String s) {
        if (s == null)
            return defaultValue;
        Object result = map.get(s.toLowerCase());
        return result == null ? defaultValue : result;
    }

    // For debug purposes
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("defaultValue = ").append(defaultValue); // NOT LOCALIZABLE
        s.append(", map = [ "); // NOT LOCALIZABLE
        for (String key : map.keySet()) {
            Object value = map.get(key);
            s.append("(key=").append(key); // NOT LOCALIZABLE
            s.append(", value=").append(value).append(")"); // NOT LOCALIZABLE
        }
        s.append(" ]"); // NOT LOCALIZABLE
        return s.toString();
    }
}
