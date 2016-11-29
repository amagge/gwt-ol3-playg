package ol.layer;

import jsinterop.annotations.JsType;

/**
 *
 * @author amagge
 *
 */
@JsType(isNative = true)
public class Heatmap extends Vector {

    public Heatmap() {}

    public Heatmap(HeatmapOptions layerOptions) {}
    
    public native int getBlur();
    
    public native String[] getGradient();
    
    public native int getRadius();
    
    public native void setBlur(int blur);
    
    public native void setGradient(String[] colors);
    
    public native void setRadius(int radius);

}

