package ol.format;

import javax.annotation.Nullable;

import jsinterop.annotations.JsType;
import ol.Feature;
import ol.Options;
import ol.proj.Projection;

/**
 * KML format.
 *
 * @author amagge
 *
 */
@JsType(isNative = true)
public class KML extends XMLFeature {
	public KML() {};

    public KML(KMLOptions kmlOptions) {};

	public native Projection readProjection(java.lang.Object source);

	public native Feature readFeature(java.lang.Object source, @Nullable Options opt_options);

	public native Feature[] readFeatures(java.lang.Object source, @Nullable Options opt_options);

	public native String readName(java.lang.Object source);
	
	public native java.lang.Object[] readNetworkLinks(java.lang.Object source);
	
	public native String writeFeatures(Feature feature, @Nullable Options opt_options);

//	public native Node writeFeaturesNode(Feature feature, @Nullable Options opt_options);

}
