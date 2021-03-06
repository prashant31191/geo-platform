/**
 *
 *    geo-platform
 *    Rich webgis framework
 *    http://geo-platform.org
 *   ====================================================================
 *
 *   Copyright (C) 2008-2014 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 *   This program is free software: you can redistribute it and/or modify it
 *   under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version. This program is distributed in the
 *   hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *   even the implied warranty of MERCHANTABILITY or FITNESS FOR
 *   A PARTICULAR PURPOSE. See the GNU General Public License
 *   for more details. You should have received a copy of the GNU General
 *   Public License along with this program. If not, see http://www.gnu.org/licenses/
 *
 *   ====================================================================
 *
 *   Linking this library statically or dynamically with other modules is
 *   making a combined work based on this library. Thus, the terms and
 *   conditions of the GNU General Public License cover the whole combination.
 *
 *   As a special exception, the copyright holders of this library give you permission
 *   to link this library with independent modules to produce an executable, regardless
 *   of the license terms of these independent modules, and to copy and distribute
 *   the resulting executable under terms of your choice, provided that you also meet,
 *   for each linked independent module, the terms and conditions of the license of
 *   that module. An independent module is a module which is not derived from or
 *   based on this library. If you modify this library, you may extend this exception
 *   to your version of the library, but you are not obligated to do so. If you do not
 *   wish to do so, delete this exception statement from your version.
 */
package org.geosdi.geoplatform.connector.server.request;

import java.net.URI;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.geosdi.geoplatform.connector.server.GPServerConnector;
import org.geosdi.geoplatform.connector.server.security.GPSecurityConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 * @author Vincenzo Monteverde <vincenzo.monteverde@geosdi.org>
 */
public abstract class GPAbstractConnectorRequest<T>
        implements GPConnectorRequest<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //
    protected final URI serverURI;
    protected final GPSecurityConnector securityConnector;
    protected final CloseableHttpClient clientConnection;
    private final CredentialsProvider credentialProvider;
    private RequestConfig requestConfig;

    public GPAbstractConnectorRequest(GPServerConnector server) {
        this(server.getClientConnection(), server.getURI(),
                server.getCredentialsProvider(),
                server.getSecurityConnector());
    }

    public GPAbstractConnectorRequest(CloseableHttpClient theClientConnection,
            URI theServerURI, CredentialsProvider theCredentialProvider,
            GPSecurityConnector theSecurityConnector) {
        this.clientConnection = theClientConnection;
        this.serverURI = theServerURI;
        this.credentialProvider = theCredentialProvider;
        this.securityConnector = (theSecurityConnector == null
                ? GPSecurityConnector.MOCK_SECURITY
                : theSecurityConnector);
    }

    /**
     * <p>
     * Setting basic configuration for HttpParams
     * </p>
     * @return RequestConfig
     */
    protected RequestConfig prepareRequestConfig() {
        return this.requestConfig = (requestConfig != null) ? requestConfig
                : createRequestConfig();

    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH).setSocketTimeout(8000)
                .setConnectTimeout(8000)
                .setConnectionRequestTimeout(8000).build();
    }

    @Override
    public URI getURI() {
        return this.serverURI;
    }

    @Override
    public CloseableHttpClient getClientConnection() {
        return this.clientConnection;
    }

    @Override
    public CredentialsProvider getCredentialsProvider() {
        return this.credentialProvider;
    }

    @Override
    public void shutdown() throws Exception {
        this.clientConnection.close();
    }

}
