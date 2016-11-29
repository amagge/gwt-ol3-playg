/**
 *
 */
package com.github.tdesjardins.ol3.demo.client.example;

import com.github.tdesjardins.ol3.demo.client.utils.DemoUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;

import ol.Collection;
import ol.Coordinate;
import ol.Executor;
import ol.Feature;
import ol.FeatureOptions;
import ol.Map;
import ol.MapOptions;
import ol.OLFactory;
import ol.View;
import ol.control.Rotate;
import ol.control.ScaleLine;
import ol.event.Event;
import ol.event.EventListener;
import ol.format.GeoJSON;
import ol.format.KML;
import ol.format.KMLOptions;
import ol.geom.LineString;
import ol.geom.MultiPoint;
import ol.geom.Point;
import ol.geom.SimpleGeometryMultiCoordinates;
import ol.interaction.KeyboardPan;
import ol.interaction.KeyboardZoom;
import ol.interaction.Select;
import ol.layer.Base;
import ol.layer.Heatmap;
import ol.layer.HeatmapOptions;
import ol.layer.LayerOptions;
import ol.layer.Tile;
import ol.layer.VectorLayerOptions;
import ol.proj.Projection;
import ol.source.Osm;
import ol.source.Source;
import ol.source.Stamen;
import ol.source.StamenOptions;
import ol.source.Vector;
import ol.source.VectorOptions;
import ol.source.XyzOptions;

/**
 * Example of HeatMap format.
 *
 * @author amagge
 */
public class HeatmapExample implements Example {

    @Override
    public void show(String exampleId) {

        //Setup KML
    	KMLOptions kmlOptions = OLFactory.createOptions();
    	kmlOptions.setExtractStyles(false);
    	KML kmlFormat = new KML(kmlOptions);
        
        VectorOptions vectorKMLSourceOptions = OLFactory.createOptions();
        vectorKMLSourceOptions.setUrl("https://openlayers.org/en/v3.19.1/examples/data/kml/2012_Earthquakes_Mag5.kml");
        vectorKMLSourceOptions.setFormat(kmlFormat);

        Vector kmlSource = new Vector(vectorKMLSourceOptions);
        HeatmapOptions hmlOptions = OLFactory.createOptions();
        hmlOptions.setBlur(10);
		hmlOptions.setRadius(10);
        hmlOptions.setSource(kmlSource);
        Heatmap hml = new Heatmap(hmlOptions);

        //Add event here
        
        // create tile
        StamenOptions stamenOptions = OLFactory.createOptions();
        stamenOptions.setLayer("toner");
        Stamen osmSource = new Stamen(stamenOptions);
        LayerOptions rasterLayerOptions = OLFactory.createOptions();
        rasterLayerOptions.setSource(osmSource);
        Tile rasterLayer = new Tile(rasterLayerOptions);
        
        // create a view
        View view = new View();
        Coordinate centerCoordinate = OLFactory.createCoordinate(0, 0);
        view.setCenter(centerCoordinate);
        view.setZoom(2);

        // create the map
        MapOptions mapOptions = OLFactory.createOptions();
        mapOptions.setTarget(exampleId);
        mapOptions.setView(view);
        Collection<Base> lstLayer = new Collection<Base>();
        lstLayer.push(rasterLayer);	
        lstLayer.push(hml);
        mapOptions.setLayers(lstLayer);
        Map map = new Map(mapOptions);

        // add some controls
        map.addControl(new ScaleLine());
        DemoUtils.addDefaultControls(map.getControls());

        // add some interactions
        map.addInteraction(new KeyboardPan());
        map.addInteraction(new KeyboardZoom());
        map.addControl(new Rotate());
        
    }

}
