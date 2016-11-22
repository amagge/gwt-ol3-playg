/**
 *
 */
package ol.format;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import ol.Options;

/**
 * Options for the KML
 *
 * @author amagge
 */
@JsType(isNative = true)
public interface KMLOptions extends Options {

    /**
     * @param extractStyles
     */
	@JsProperty
    void setExtractStyles(boolean extractStyles);

    /**
     * @param showPointNames
     */
	@JsProperty
    void setShowPointNames(boolean showPointNames);
    
    /**
     * @param writeStyles
     */
	@JsProperty
    void setWriteStyles(boolean writeStyles);

}
