/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.plugins.integrity;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pierrem
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class AbstractIntegrity {
    //verification mode
    protected static final int INTEGRITY = 0;
    protected static final int CLEANUP = 1;
    //model type
    protected static final int OR_DATAMODEL = 0;
    protected static final int DOMAIN_MODEL = 1;
    protected static final int COMMONITEM_MODEL = 2;
    protected static final int OPERATION_LIBRARY = 3;
    protected static final int ER_DATAMODEL = 4;

    protected int modelErrorCount = 0;
    protected int modelWarningCount = 0;
    protected ModelIntegrityReport modelIntegrityReport;

    /*
     * Integrity
     */
    protected static final String kSolution1 = LocaleMgr.misc.getString("Solution1");
    protected static final String kSolution2 = LocaleMgr.misc.getString("Solution2");
    protected static final String kSolution3 = LocaleMgr.misc.getString("Solution3");

    /*
     * CleanUp
     */
    public static final String DELETE_ONE = "DELETE_ONE"; // NOT LOCALIZABLE 
    static final String DELETE_ALL = "DELETE_ALL"; // NOT LOCALIZABLE  
    protected static List[] occurencesToClean = null; //utiliser avec les constantes de ruleIndex

    /*
     * public methods
     */

    public List<DbObject> getOccurrences(DbObject model, MetaClass metaclass)
            throws DbException {
        List<DbObject> occurences = new ArrayList<DbObject>();

        if (model == null || metaclass == null)
            return occurences;

        DbEnumeration dbEnum = getDbObjectsToVerify(model, metaclass);
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            dbo.setValidationStatus(DbObject.VALIDATION_OK);
            occurences.add(dbo);
        }

        dbEnum.close();
        return occurences;
    }

    public static final DbEnumeration getDbObjectsToVerify(DbObject composite, MetaClass metaclass)
            throws DbException {
        return composite.componentTree(metaclass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass });
    }

    public List getEntitiesAssociations(DbObject model, MetaClass metaclass,
                                        boolean isAssociation) throws DbException {
        List<DbORTable> occurences = new ArrayList<>();
        DbORTable dbTable = null;

        if (model == null || metaclass == null)
            return occurences;

        DbEnumeration dbEnum = model.getComponents().elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            dbTable = (DbORTable) dbEnum.nextElement();
            if (isAssociation) {
                if (dbTable.isIsAssociation())
                    occurences.add(dbTable);
            } else {
                if (!dbTable.isIsAssociation())
                    occurences.add(dbTable);
            }
        }
        dbEnum.close();
        return occurences;
    }

    public List getErRoles(DbObject model, MetaClass metaclass) throws DbException {
        List<DbObject> occurences = new ArrayList<>();
        DbORAssociation association = null;

        if (model == null || metaclass == null)
            return occurences;

        DbEnumeration dbEnum = model.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            association = (DbORAssociation) dbEnum.nextElement();
            occurences.add(association.getArcEnd());
        }
        dbEnum.close();
        return occurences;
    }

    /*
     * protected methods
     */

    protected int getModelType(DbObject model) throws Exception {
        if (model instanceof DbORDataModel) {
            int mode = ((DbORDataModel) model).getLogicalMode().intValue();
            int modelType = 0;

            if (mode == DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL)
                modelType = OR_DATAMODEL;
            else
                modelType = ER_DATAMODEL;
            return modelType;
        } else if (model instanceof DbORDomainModel)
            return DOMAIN_MODEL;
        else if (model instanceof DbORCommonItemModel)
            return COMMONITEM_MODEL;
        else if (model instanceof DbOROperationLibrary)
            return OPERATION_LIBRARY;
        else
            return -1;
    }

    /**
     * CleanUp 1 occurence
     */
    protected static boolean deleteOneOccurence(DbObject dbo) throws DbException {
        dbo.remove();
        int status = dbo.getTransStatus();
        if (status == Db.OBJ_REMOVED) {
            return true;
        }
        return false;
    }

    /**
     * CleanUp occurences of a rule section
     */
    protected static boolean deleteSectionsOccurences(int ruleIndx) throws DbException {
        for (int i = 0; i < occurencesToClean[ruleIndx].size(); i++) {
            DbObject dbo = (DbObject) (occurencesToClean[ruleIndx].get(i));
            dbo.remove();
            int status = dbo.getTransStatus();
            if (status != Db.OBJ_REMOVED)
                return false;
        }
        return true;
    }

    /**
     * CleanUp All occurences
     */
    protected static boolean deleteAllSectionOccurences() throws DbException {
        for (List arrayList : occurencesToClean) {
            for (Object o : arrayList) {
                DbObject dbo = (DbObject) o;
                dbo.remove();
                int status = dbo.getTransStatus();
                if (status != Db.OBJ_REMOVED)
                    return false;
            }
        }
        return true;
    }
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
}
