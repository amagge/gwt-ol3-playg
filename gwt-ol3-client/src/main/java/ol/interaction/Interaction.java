package ol.interaction;

import jsinterop.annotations.JsType;

/**
 * Abstract base class; normally only used for creating subclasses and not
 * instantiated in apps. User actions that change the state of the map. Some are
 * similar to controls, but are not associated with a DOM element. For example,
 * {@link ol.interaction.KeyboardZoom} is functionally the same as
 * {@link ol.control.Zoom}, but triggered by a keyboard event not a button
 * element event. Although interactions do not have a DOM element, some of them
 * do render vectors and so are visible on the screen.
 *
 * @author Tino Desjardins
 *
 */
@JsType(isNative = true)
public abstract class Interaction extends ol.Object {

    /**
     * Return whether the interaction is currently active.
     *
     * @return {boolean} `true` if the interaction is active, `false` otherwise.
     */
    public native boolean getActive();

    /**
     * Activate or deactivate the interaction.
     *
     * @param active
     *            Active.
     */
    public native void setActive(boolean active);

}
