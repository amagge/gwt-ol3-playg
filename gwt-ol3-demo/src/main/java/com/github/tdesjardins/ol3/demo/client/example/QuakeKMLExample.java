/**
 *
 */
package com.github.tdesjardins.ol3.demo.client.example;

import java.util.HashMap;

import com.github.tdesjardins.ol3.demo.client.utils.DemoUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;

import ol.Collection;
import ol.Coordinate;
import ol.Feature;
import ol.GenericFunction;
import ol.Map;
import ol.MapBrowserEvent;
import ol.MapEvent;
import ol.MapOptions;
import ol.OLFactory;
import ol.OLUtil;
import ol.Pixel;
import ol.View;
import ol.color.Color;
import ol.control.Rotate;
import ol.control.ScaleLine;
import ol.event.Event;
import ol.event.EventListener;
import ol.format.KML;
import ol.format.KMLOptions;
import ol.interaction.KeyboardPan;
import ol.interaction.KeyboardZoom;
import ol.interaction.Pointer;
import ol.interaction.Select;
import ol.interaction.SelectOptions;
import ol.layer.Base;
import ol.layer.LayerOptions;
import ol.layer.Tile;
import ol.layer.VectorLayerOptions;
import ol.source.Stamen;
import ol.source.StamenOptions;
import ol.source.Vector;
import ol.source.VectorOptions;
import ol.style.Circle;
import ol.style.Fill;
import ol.style.Stroke;
import ol.style.Style;

/**
 * Example of HeatMap format.
 *
 * @author amagge
 */
public class QuakeKMLExample implements Example {

	static HashMap<Double,Style> styleHash = new HashMap<Double,Style>();
	static DecoratedPopupPanel simplePopup = null;
	
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
        VectorLayerOptions vectorLayerOptions = OLFactory.createOptions();
        vectorLayerOptions.setSource(kmlSource);
        ol.layer.Vector vectorLayer = new ol.layer.Vector(vectorLayerOptions);
        GenericFunction<Feature, Style[]> style = new GenericFunction<Feature, Style[]>() {
        	@Override
			public Style[] call(Feature feature) {
				String name = (String)feature.get("name");
				double magnitude = 5;
				try {
					int beginInd = name.indexOf('M') + 2;
					int endInd = name.indexOf('-') - 1;
					magnitude = Double.parseDouble(name.substring(beginInd, endInd));
				}catch(Exception e){
					GWT.log("ERROR" + e.getMessage() + " in " + name);
				}
				double radius = ((magnitude-5)*20)+5;
				//GWT.log(name);
				Style style = styleHash.get(radius);
				if(style == null){
					Color fillColor = OLFactory.createColor(255, 153, 0, 0.4);
					Color strokeColor = OLFactory.createColor(255, 204, 0, 0.2);
					Fill fill = OLFactory.createFill(fillColor);
					Stroke stroke = OLFactory.createStroke(strokeColor, 1);
					Circle circle = OLFactory.createCircleStyle(fill, stroke, radius);
					style = OLFactory.createStyle(circle);
				}
				Style[] styleArr = {style};
				return styleArr;
			}
		};
        vectorLayer.setStyle(style); 
        
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
        lstLayer.push(vectorLayer);
        mapOptions.setLayers(lstLayer);
        Map map = new Map(mapOptions);


        final Select selectFeature = createSelectFeature(map);
        map.addInteraction(selectFeature);
        
        map.on("pointermove", createPointerFeature(map));
        
        // add some controls
        map.addControl(new ScaleLine());
        DemoUtils.addDefaultControls(map.getControls());

        // add some interactions
        map.addInteraction(new KeyboardPan());
        map.addInteraction(new KeyboardZoom());
        map.addControl(new Rotate());
    }

	private EventListener<MapBrowserEvent> createPointerFeature(Map map) {
		EventListener<MapBrowserEvent> selectListener = new EventListener<MapBrowserEvent>() {
			@Override
			public void onEvent(MapBrowserEvent event) {
				GenericFunction<Feature, Feature> featureFunc = new GenericFunction<Feature, Feature>(){
					@Override
					public Feature call(Feature feature){
						return feature;
					}
				};
				Feature feature = map.forEachFeatureAtPixel(event.getPixel(), featureFunc);
						
				if(feature!=null){
					String name = (String)feature.get("name");
					GWT.log(name);
					int left = event.getPixel().getX();
		            int top = event.getPixel().getY() - 15;
		            if(simplePopup==null){
		            	simplePopup = new DecoratedPopupPanel(true);
			            simplePopup.setWidth("150px");
			            simplePopup.getElement().getStyle().setProperty("color", "white");
			            simplePopup.getElement().getStyle().setProperty("fontSize", "small");
			            simplePopup.getElement().getStyle().setProperty("borderColor", "white");
			            simplePopup.getElement().getStyle().setProperty("borderStyle", "solid");
			            simplePopup.getElement().getStyle().setProperty("borderWidth", "2px");
			            simplePopup.getElement().getStyle().setProperty("backgroundColor", "black");
		            }
		            simplePopup.setPopupPosition(left, top);
		            SafeHtmlBuilder msg = new SafeHtmlBuilder();
		            simplePopup.setWidget(new HTML(msg.appendHtmlConstant(name).toSafeHtml()));
		            simplePopup.show();
				} else {
					if(simplePopup!=null)
						simplePopup.hide();
				}
			}
		};
        return selectListener;
	}

	private Select createSelectFeature(Map map) {
        // create a select interaction
        final Select selectFeature = new Select((SelectOptions)OLFactory.createOptions());
        EventListener<Select.Event> selectListener = new EventListener<Select.Event>() {
            @Override
            public void onEvent(Select.Event event) {
            	Collection<Feature> selectedFeatures = selectFeature.getFeatures();
            	if (selectedFeatures.getLength() > 0) {
            		Feature feature = selectedFeatures.item(0);
            		String output = "You selected feature with id '" + feature.getId() + "'"
            				+ " and name '" + feature.get("name") + "'"
            				+ " and geometry name '" + feature.getGeometryName() + "'"
            				+ ".";
            		Window.alert(output);
            	}
            }
        };
        OLUtil.observe(selectFeature, "select", selectListener);
        return selectFeature;
	}

}
