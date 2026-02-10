package renderer.core.shader;

import renderer.controller.ColorMapFactory.Maps;
import renderer.controller.ImageWrapper;
import renderer.controller.Renderer;

/**
 * The Shader class is responsible for writing final pixel color
 * to the screen (GraphicWrapper), from a Fragment.
 * Subclass this base class and implement the ::shade() method.
 * @author cdehais
 */

public abstract class Shader {

    /**
     * The screen to draw on.
     */
    protected ImageWrapper screen;

    /**
     * Creates a Shader with the given screen.
     */
    protected Shader() {
    }

    /**
     * Common entry point to ree-initialize the shader.
     */
    public abstract void reset();

    /**
     * Computes the fragment color and write the result to the screen.
     * @param fragment the fragment to shade
     */
    public abstract void shade(Fragment fragment);

    /**
     * Test whether the fragment falls onto the screen.
     * @param fragment the fragment to test
     * @return true if the fragment is clipped, false otherwise
     */
    public boolean isClipped(Fragment fragment) {
        return screen.isClipped(fragment);
    }

    /**
     * Initializes a shader.
     * @param renderer the renderer that contains all the information
     * @param newScreen the screen to draw on.
     */
    public void init(final Renderer renderer, final ImageWrapper newScreen) {
        screen = newScreen;
    }

    /**
     * Sets the color map for shaders that support it.
     * Default implementation does nothing.
     *
     * @param map the color map to use
     */
    public void setColorMap(Maps map) {
        // Default: do nothing
    }

    /**
     * Indicates whether this shader supports color mapping.
     *
     * @return true if the shader supports color maps, false otherwise
     */
    public boolean supportsColorMap() {
        return false;
    }
}
