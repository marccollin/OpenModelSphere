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
package org.modelsphere.jack.srtool.services;

import org.modelsphere.jack.preference.DriverInfo;
import org.modelsphere.jack.preference.DriversManager;
import org.modelsphere.jack.srtool.international.LocaleMgr;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public final class DriverService extends DriverServiceProtocol {

    private static final String kNoSuchDrivers = LocaleMgr.message.getString("NoDriversFound");

    private DriverListMessage driverListMessage = new DriverListMessage();

    public void tryToLoad(String driver, int index) {
        try {
            boolean loaded = load(driver);
            driverListMessage.loaded[index] = loaded;
            driverListMessage.texts[index] = ""; // driver.getText();
        } catch (ClassNotFoundException ex) {
            driverListMessage.loaded[index] = false;
            driverListMessage.texts[index] = ex.getMessage();
        }
    }

    private boolean load(String driver) throws ClassNotFoundException {
        if (driver == null)
            return false;
        DriverInfo driverInfo = DriversManager.getDriver(driver);
        if (driverInfo == null)
            return false;
        Class.forName(driverInfo.getPropertyString(DriverInfo.class, DriverInfo.DRIVER_JDBC,
                DriverInfo.DEFAULT_JDBC_VALUE));
        return true;
    }

    // Builds the driver list
    public DriverService() {
        String[] drivers = DriversManager.getDrivers();

        int nb = drivers.length;
        driverListMessage.nb_drivers = nb;
        driverListMessage.driverList = new String[nb];
        driverListMessage.loaded = new boolean[nb];
        driverListMessage.texts = new String[nb];

        for (int i = 0; i < nb; i++) {
            DriverInfo driverinfo = (DriverInfo) DriversManager.getDriver(drivers[i]);
            if (driverinfo == null)
                continue;
            driverListMessage.driverList[i] = drivers[i];
            // tryToLoad(drivers[i], i);
        }
    }

    // Send the drivers' name & info
    public void serve(InputStream i, OutputStream o) throws IOException {
        // TODOServiceList.getSingleInstance();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(o));

        ObjectInputStream input = new ObjectInputStream(i);
        try {
            input.readObject(); // ignore input message
        } catch (ClassNotFoundException ex) {
            driverListMessage.errorMessage = ex.toString();
        }

        if (driverListMessage.nb_drivers == 0) {
            driverListMessage.errorMessage = kNoSuchDrivers;
        }

        ObjectOutputStream output = new ObjectOutputStream(o);
        output.writeObject(driverListMessage);

        driverListMessage.errorMessage = null; // clear error message for the
        // next call

        out.close();
        i.close();
    }

}
