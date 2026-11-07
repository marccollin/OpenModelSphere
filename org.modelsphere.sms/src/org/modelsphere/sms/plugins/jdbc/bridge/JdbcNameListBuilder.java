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

package org.modelsphere.sms.plugins.jdbc.bridge;

import org.modelsphere.jack.srtool.reverse.jdbc.JdbcBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This service executes JDBC API calls
 **/
public class JdbcNameListBuilder extends JdbcBuilder {
    private static final String USERSEP = ".";
    private static final String OCCSEP = ",";
    private List<String> nameList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();
    private List<String> tableSortList = new ArrayList<>();
    private List<String> viewSortList = new ArrayList<>();
    private List<String> procSortList = new ArrayList<>();
    private String sortBuffer = null;
    private String userNames = null;

    JdbcNameListBuilder() {
    }

    /* Types */
    protected void buildTypeName(String typeName) throws Exception {
        typeList.add(typeName.concat(OCCSEP));
    };

    /* Table Owner */
    protected void buildTableSchema(String schemaName) throws Exception {
        sortBuffer = null;
        sortBuffer = schemaName == null ? "" : schemaName.concat(USERSEP);
    };

    /* Table Name */
    protected final void buildTableName(String tableName) throws Exception {
        sortBuffer = sortBuffer.concat(tableName + OCCSEP);
        tableSortList.add(sortBuffer);
    };

    /* View Owner */
    protected void buildViewSchema(String schemaName) throws Exception {
        sortBuffer = null;
        sortBuffer = schemaName == null ? "" : schemaName.concat(USERSEP);
    };

    /* View Name */
    protected final void buildViewName(String viewName) throws Exception {
        sortBuffer = sortBuffer.concat(viewName + OCCSEP);
        viewSortList.add(sortBuffer);
    };

    /* Procedure Owner */
    protected void buildProcSchema(String tokenProcSchema) throws Exception {
        sortBuffer = null;
        sortBuffer = tokenProcSchema == null ? "" : tokenProcSchema.concat(USERSEP);
    };

    /* Procedure Name */
    protected void buildProcName(String tokenProcName) throws Exception {
        sortBuffer = sortBuffer.concat(tokenProcName + OCCSEP);
        procSortList.add(sortBuffer);
    };

    void installUser(int index) {
        nameList.add(index, userNames);
    }

    void installType(int index) {
        String typeNames = "";
        Collections.sort(typeList);
        for (String s : typeList) {
            typeNames = typeNames.concat(s);
        }
        nameList.add(index, typeNames);
    }

    void installTable(int index) {
        String tableNames = "";
        Collections.sort(tableSortList);
        for (String s : tableSortList) {
            tableNames = tableNames.concat(s);
        }
        nameList.add(index, tableNames);
    }

    void installView(int index) {
        String viewNames = "";
        Collections.sort(viewSortList);
        for (String s : viewSortList) {
            viewNames = viewNames.concat(s);
        }
        nameList.add(index, viewNames);
    }

    void installProc(int index) {
        String procNames = "";
        Collections.sort(procSortList);
        for (String s : procSortList) {
            procNames = procNames.concat(s);
        }
        nameList.add(index, procNames);
    }

    List<String> getNameList() {
        return nameList;
    }
}
