/*************************************************************************

This file is part of Open ModelSphere HTML Reports Project.

Open ModelSphere HTML Reports is free software; you can redistribute
it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3 of the
License, or (at your option) any later version.

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

You can contact us at :
http://www.javaforge.com/project/3219

 **********************************************************************/

package org.modelsphere.plugins.html.browser.data.extractor.dbobject;

import java.util.*;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.plugins.html.browser.data.DataComponent;
import org.modelsphere.plugins.html.browser.data.DataDbObject;
import org.modelsphere.plugins.html.browser.data.DataDbObjectsGroup;
import org.modelsphere.plugins.html.browser.data.DataDiagram;
import org.modelsphere.plugins.html.browser.data.extractor.smsdiagram.SMSDiagramExtractor;
import org.modelsphere.sms.db.DbSMSDiagram;

/**
 * This class is used to extract the necessary information of a project from a
 * point of entry defined by a DbObject set through the constructor so it can
 * be easily be accessible for the creation of the HTML report.
 *
 * @author Open ModelSphere HTML Reports Team
 * @version 1.0.0
 */
public class DbObjectExtractor {
    /**
     * DbObject from which the information will be extracted
     */
    private final DbObject sourceObject;

    /**
     * Constructor
     *
     * @param object DbObject from which the information will be extracted
     */
    public DbObjectExtractor(DbObject object) {
        this.sourceObject = object;
    }

    /**
     * extractDbObject is the main function of the class. It lists the fields,
     * the components, the diagrams and the icons of object in a DataDbObject to
     * then be used to generate the HTML report.
     *
     * @return An object containing all the information from object
     * @throws DbException If an error occurs while handling a DbObject
     */
    public DataDbObject extractDbObject() throws DbException {
        Map<DbObject, DataComponent> dbObjectAssociations = new HashMap<DbObject, DataComponent>();
        return extractDbObject(dbObjectAssociations);
    }

    private DataDbObject extractDbObject(Map<DbObject, DataComponent> dbObjectAssociations) throws DbException {
        if (dbObjectAssociations.containsKey(sourceObject)) {
            return (DataDbObject) dbObjectAssociations.get(sourceObject);
        }

        DataDbObject extractedObject = createBaseDataObject();
        dbObjectAssociations.put(sourceObject, extractedObject);

        addExtractedComponents(extractedObject, dbObjectAssociations);
        addExtractedDiagrams(extractedObject, dbObjectAssociations);

        return extractedObject;
    }

    private DataDbObject createBaseDataObject() throws DbException {
        DataDbObject extractedObject = new DataDbObject(sourceObject.getName());

        FieldsExtractor fieldsExtractor = new FieldsExtractor(sourceObject);
        extractedObject.setFields(fieldsExtractor.extractFields());

        IconExtractor iconExtractor = new IconExtractor(sourceObject);
        extractedObject.setIcon(iconExtractor.extractIcon());

        return extractedObject;
    }

    private void addExtractedComponents(DataDbObject extractedObject,
                                        Map<DbObject, DataComponent> dbObjectAssociations) throws DbException {
        ComponentsExtractor componentsExtractor = new ComponentsExtractor(sourceObject);
        Map<String, List<DbObject>> groupedComponents = componentsExtractor.extractComponents();

        for (Map.Entry<String, List<DbObject>> componentGroup : groupedComponents.entrySet()) {
            String groupName = componentGroup.getKey();
            List<DbObject> components = componentGroup.getValue();

            if (components.size() > 1) {
                addComponentGroup(extractedObject, dbObjectAssociations, groupName, components);
            } else {
                addSingleComponent(extractedObject, dbObjectAssociations, components.get(0));
            }
        }
    }

    private void addComponentGroup(DataDbObject extractedObject,
                                   Map<DbObject, DataComponent> dbObjectAssociations, String groupName,
                                   List<DbObject> components) throws DbException {
        DataDbObjectsGroup componentGroup = new DataDbObjectsGroup(groupName);

        for (DbObject component : components) {
            DataDbObject dataObject = extractDataObject(component, dbObjectAssociations);
            componentGroup.add(dataObject);
        }

        componentGroup.sortComponents();
        extractedObject.addComponent(componentGroup);
    }

    private void addSingleComponent(DataDbObject extractedObject,
                                    Map<DbObject, DataComponent> dbObjectAssociations, DbObject component)
            throws DbException {
        DataDbObject dataObject = extractDataObject(component, dbObjectAssociations);
        extractedObject.addComponent(dataObject);
    }

    private DataDbObject extractDataObject(DbObject component,
                                           Map<DbObject, DataComponent> dbObjectAssociations) throws DbException {
        DbObjectExtractor componentExtractor = new DbObjectExtractor(component);
        return componentExtractor.extractDbObject(dbObjectAssociations);
    }

    private void addExtractedDiagrams(DataDbObject extractedObject,
                                      Map<DbObject, DataComponent> dbObjectAssociations) throws DbException {
        DiagramsExtractor diagramsExtractor = new DiagramsExtractor(sourceObject);
        List<DbSMSDiagram> diagrams = diagramsExtractor.extractDiagrams();

        for (DbSMSDiagram diagram : diagrams) {
            SMSDiagramExtractor diagramExtractor = new SMSDiagramExtractor(diagram, dbObjectAssociations);
            DataDiagram dataDiagram = diagramExtractor.extractDbSMSDiagram();
            extractedObject.addDiagram(dataDiagram);
        }
    }
}