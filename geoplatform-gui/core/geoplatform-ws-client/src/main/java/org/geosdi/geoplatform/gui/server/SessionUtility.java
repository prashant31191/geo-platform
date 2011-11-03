/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2011 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. This program is distributed in the 
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. You should have received a copy of the GNU General 
 * Public License along with this program. If not, see http://www.gnu.org/licenses/ 
 *
 * ====================================================================
 *
 * Linking this library statically or dynamically with other modules is 
 * making a combined work based on this library. Thus, the terms and 
 * conditions of the GNU General Public License cover the whole combination. 
 * 
 * As a special exception, the copyright holders of this library give you permission 
 * to link this library with independent modules to produce an executable, regardless 
 * of the license terms of these independent modules, and to copy and distribute 
 * the resulting executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the license of 
 * that module. An independent module is a module which is not derived from or 
 * based on this library. If you modify this library, you may extend this exception 
 * to your version of the library, but you are not obligated to do so. If you do not 
 * wish to do so, delete this exception statement from your version. 
 *
 */
package org.geosdi.geoplatform.gui.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.geosdi.geoplatform.core.model.GPUser;
import org.geosdi.geoplatform.gui.utility.DefaultProjectEnum;
import org.geosdi.geoplatform.gui.utility.GPSessionTimeout;
import org.geosdi.geoplatform.gui.utility.UserLoginEnum;
import org.springframework.stereotype.Service;

/**
 * @author Nazzareno Sileno - CNR IMAA geoSDI Group
 * @email nazzareno.sileno@geosdi.org
 */
@Service("sessionUtility")
public class SessionUtility {

    public Long getDefaultProjectFromUserSession(HttpServletRequest httpServletRequest)
            throws GPSessionTimeout {
        Long projectId;
        HttpSession session = httpServletRequest.getSession();
        projectId = (Long) session.getAttribute(DefaultProjectEnum.DEFAULT_PROJECT.toString());
        if (projectId != null) {
            return projectId;
        } else {
            throw new GPSessionTimeout("Session Timeout");
        }
    }

    public GPUser getUserAlreadyFromSession(HttpServletRequest httpServletRequest)
            throws GPSessionTimeout {
        GPUser user = null;
        HttpSession session = httpServletRequest.getSession();
        Object userObj = session.getAttribute(UserLoginEnum.USER_LOGGED.toString());
        if (userObj != null && userObj instanceof GPUser) {
            user = (GPUser) userObj;
        } else {
            throw new GPSessionTimeout("Session Timeout");
        }
        return user;
    }

    public void storeUserInSession(GPUser user, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        //TODO: Set the right time in seconds before session interrupt
        session.setMaxInactiveInterval(900);
        session.setAttribute(UserLoginEnum.USER_LOGGED.toString(), user);
    }

    public void storeDefaultProjectInSession(Long projectID, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        //TODO: Set the right time in seconds before session interrupt
        session.setMaxInactiveInterval(900);
        session.setAttribute(DefaultProjectEnum.DEFAULT_PROJECT.toString(), projectID);
    }

    public void storeUserAndProjectInSession(GPUser user, Long projectID,
            HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        //TODO: Set the right time in seconds before session interrupt
        session.setMaxInactiveInterval(900);
        session.setAttribute(UserLoginEnum.USER_LOGGED.toString(), user);
        session.setAttribute(DefaultProjectEnum.DEFAULT_PROJECT.toString(), projectID);
    }
}