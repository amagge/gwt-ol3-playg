package ol.layer;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import ol.Extent;

/**
 * Vector layer options.
 *
 * @author amagge
 *
 */
@JsType(isNative = true)
public interface HeatMapLayerOptions extends VectorLayerOptions {
	@JsProperty
	void setGradient(String[] gradient);
	
	@JsProperty
	void setRadius(int radius);
	
	@JsProperty
	void setBlur(int blur);
    
	@JsProperty
	void setShadow(int gradient);
    
	@JsProperty
	void setOpacity(int opacity);
    
	@JsProperty
	void setMinResolution(int minResolution);
    
	@JsProperty
	void setMaxResolution(int maxResolution);
    
	@JsProperty
	void setExtent(Extent extent);
}
