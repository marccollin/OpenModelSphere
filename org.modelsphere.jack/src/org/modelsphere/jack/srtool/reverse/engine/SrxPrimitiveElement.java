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

import java.util.Locale;
import java.util.Map;

public final class SrxPrimitiveElement implements SrxElement {
    // Fields used to access and convert the SRX Value to a compatible DB Value
    private final Class<?> type; // The type of the parameter of the dbobject setter method.
    private final String fieldTag; // The tag representing the value

    public SrxPrimitiveElement(String fieldTag, Class<?> type) {
        this.fieldTag = fieldTag;
        this.type = type;
    }

    String getFieldTag() {
        return fieldTag;
    }

    @Override
    public Object getValue(Object hookContainer, Map currentObject) {
        if (fieldTag == null || currentObject == null)
            return null;
        String value = (String) currentObject.get(fieldTag);
        return convertStringValue(value);
    }

    private Object convertStringValue(String s) {
        if ((type == null) || (s == null) || s.trim().isEmpty())
            return null;
        String trimmedValue = s.trim();
        if (type == Integer.class)
            return Integer.valueOf(trimmedValue);
        if (type == Boolean.class)
            return parseBoolean(s);
        if (type == Double.class)
            return Double.valueOf(trimmedValue);
        if (type == Long.class)
            return Long.valueOf(trimmedValue);
        if (type == Float.class)
            return Float.valueOf(trimmedValue);
        if (type == String.class)
            return s;
        return null;
    }

    // Parse this string value for a Boolean.
    private Boolean parseBoolean(String s) {
        String sTrim = s.trim().toLowerCase(Locale.ROOT);

        // try with true, false, yes, no, and french values
        if (sTrim.equals("true"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("false"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's
        if (sTrim.equals("vrai"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("faux"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's

        if (sTrim.equals("yes"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("no"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's
        if (sTrim.equals("oui"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("non"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's

        // Also accept single-character boolean representations used by DBMSs.
        if (sTrim.equals("1") || sTrim.equals("t") || sTrim.equals("v") || sTrim.equals("y")
                || sTrim.equals("o"))
            return Boolean.TRUE;
        if (sTrim.equals("0") || sTrim.equals("f") || sTrim.equals("n"))
            return Boolean.FALSE;

        throw new IllegalArgumentException("Unrecognized boolean value: " + s);
    }

    @Override
    public String getDebugInfo(Map currentObject) {
        return "[SrxPrimitiveElement] tag= " + fieldTag + ", type= " + type + ", tagvalue= "
                + currentObject.get(fieldTag); // NOT LOCALIZABLE
    }

}
