package renderer.core.light;

import renderer.algebra.Vector;

/**
 * This Class represents an ambient light.
 */
public class AmbientLight extends Light {


    /**
     * Create a AmbientLight from its intensity.
     * @param intensity the intensity of the light
     */
    public AmbientLight(double intensity) {
        super(intensity);
    }

    @Override
    public double getContribution(Vector position, Vector normal, double[] color,
            Vector cameraPosition, double ka, double kd, double ks, double s) {
        return intensity;
    }
}
