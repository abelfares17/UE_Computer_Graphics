package renderer.core.shader;

/**
 * Simple shader that just copy the interpolated color to the screen.
 * @author cdehais
 */
public class SimpleShader extends Shader {

    /**
     * Shade the fragment.
     * @param fragment the fragment to shade
     */
    @Override
    public void shade(Fragment fragment) {
        screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
    }

    /**
     * Reset the shader.
     */
    @Override
    public void reset() {
        // Nothing to reset
    }
}
