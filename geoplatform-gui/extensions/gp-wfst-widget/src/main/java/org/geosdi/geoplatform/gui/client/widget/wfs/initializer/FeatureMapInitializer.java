/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2013 geoSDI Group (CNR IMAA - Potenza - ITALY).
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
package org.geosdi.geoplatform.gui.client.widget.wfs.initializer;

import com.google.gwt.user.client.Timer;
import javax.inject.Inject;
import org.geosdi.geoplatform.gui.client.model.binder.ILayerSchemaBinder;
import org.geosdi.geoplatform.gui.client.puregwt.wfs.event.FeatureStatusBarEvent;
import org.geosdi.geoplatform.gui.client.widget.wfs.map.listener.FeatureSelectListener;
import org.geosdi.geoplatform.gui.client.widget.wfs.map.listener.FeatureUnSelectListener;
import org.geosdi.geoplatform.gui.client.widget.wfs.builder.FeatureMapLayerBuilder;
import org.geosdi.geoplatform.gui.client.widget.wfs.builder.GetFeatureControlBuilder;
import org.geosdi.geoplatform.gui.client.widget.wfs.statusbar.FeatureStatusBar;
import org.geosdi.geoplatform.gui.client.widget.wfs.toolbar.button.WFSButtonKeyProvider;
import org.geosdi.geoplatform.gui.client.widget.wfs.toolbar.button.WFSToggleButton;
import org.geosdi.geoplatform.gui.impl.map.control.feature.GetFeatureModel;
import org.geosdi.geoplatform.gui.model.GPLayerBean;
import org.geosdi.geoplatform.gui.model.GPVectorBean;
import org.geosdi.geoplatform.gui.puregwt.GPEventBus;
import org.geosdi.geoplatform.gui.responce.LayerSchemaDTO;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.control.GetFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
public class FeatureMapInitializer implements IFeatureMapInitializer {
    
    @Inject
    private MapWidget mapWidget;
    @Inject
    private FeatureMapLayerBuilder mapLayerBuilder;
    @Inject
    private Vector vectorLayer;
    @Inject
    private GetFeatureControlBuilder featureControlBuilder;
    @Inject
    private FeatureSelectListener selectFeature;
    @Inject
    private FeatureUnSelectListener unSelectFeature;
    @Inject
    private LonLat italyLonLat;
    @Inject
    private ILayerSchemaBinder layerSchemaBinder;
    private GPEventBus bus;
    private Layer wms;
    private GetFeature controlFeature;
    
    @Inject
    public FeatureMapInitializer(GPEventBus theBus) {
        this.bus = theBus;
    }
    
    @Override
    public void bindLayerSchema() {
        final GPLayerBean layer = layerSchemaBinder.getSelectedLayer();
        final LayerSchemaDTO schema = layerSchemaBinder.getLayerSchemaDTO();
        
        this.wms = this.mapLayerBuilder.buildLayer(layer);
        
        this.controlFeature = this.featureControlBuilder.buildControl(
                new GetFeatureModel() {
            
            @Override
            public String getFeatureNameSpace() {
                return layer instanceof GPVectorBean ? ((GPVectorBean) layer).
                        getFeatureNameSpace()
                        : schema.getTargetNamespace();
            }
            
            @Override
            public String getFeatureType() {
                int pos = layer.getName().indexOf(":");
                
                return pos > 0 ? layer.getName().substring(pos + 1,
                        layer.getName().length()) : layer.getName();
            }
            
            @Override
            public String getSrsName() {
                return layer.getCrs();
            }
            
            @Override
            public String getGeometryName() {
                return (layer instanceof GPVectorBean)
                        ? ((GPVectorBean) layer).getGeometryName()
                        : schema.getGeometry().getName();
            }
            
            @Override
            public WMS getWMSLayer() {
                return (WMS) wms;
            }
            
        });
        
        Timer t = new Timer() {
            
            @Override
            public void run() {
                loadLayerOnMap();
                notifyStatus();
            }
            
        };
        
        t.schedule(1000);
    }
    
    @Override
    public void resetMapWidget() {
        this.controlFeature.removeListener(selectFeature);
        this.controlFeature.removeListener(unSelectFeature);
        
        this.controlFeature.deactivate();
        this.mapWidget.getMap().removeControl(controlFeature);
        
        this.vectorLayer.destroyFeatures();
        
        if (wms != null) {
            this.mapWidget.getMap().removeLayer(wms);
        }
        
        this.mapWidget.getMap().removeLayer(vectorLayer);
        
        this.initMapWidget();
    }
    
    @Override
    public void initMapWidget() {
        this.mapWidget.getMap().setCenter(italyLonLat, 4);
    }
    
    protected void loadLayerOnMap() {
        this.mapWidget.getMap().addLayer(wms);
        this.mapWidget.getMap().addLayer(vectorLayer);
        
        this.mapWidget.getMap().addControl(controlFeature);
        
        controlFeature.addFeatureSelectedListener(selectFeature);
        
        controlFeature.addFeatureUnselectedListener(unSelectFeature);
        
        Bounds bb = ((WMS) this.wms).getOptions().getMaxExtent();
        
        this.mapWidget.getMap().zoomToExtent(bb);

//        this.controlFeature.activate();
        
        WFSToggleButton.fireToggleStateEvent(
                WFSButtonKeyProvider.GET_FEATURE.name());
    }
    
    protected void notifyStatus() {
        this.bus.fireEvent(new FeatureStatusBarEvent("WFS Layer loaded",
                FeatureStatusBar.FeatureStatusBarType.STATUS_OK));
    }
    
}
