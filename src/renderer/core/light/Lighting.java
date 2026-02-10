package renderer.core.light;

import java.util.LinkedList;
import java.util.List;

import renderer.algebra.MathUtils;
import renderer.algebra.Vector;

/**
 * The Lighting class describes a scene lighting environment.
 *
 * @author gmorin, smondet
 */
public class Lighting {

    /** List of light sources. */
    private final List<Light> lights;

    /**
     * Creates a new Lighting environment.
     */
    public Lighting() {
        lights = new LinkedList<>();
    }

    /**
     * Adds a new ambient light source of intensity @ia to the environment.
     *
     * @param ia the intensity of the ambient light
     */
    public void addAmbientLight(final double ia) {
        lights.add(new AmbientLight(ia));
    }

    /**
     * Adds a new point light source of intensity @id at position (x, y, z)
     * to the environment.
     *
     * @param x  the x coordinate of the light source
     * @param y  the y coordinate of the light source
     * @param z  the z coordinate of the light source
     * @param id the intensity of the light source
     */
    public void addPointLight(final double x, final double y,
            final double z, final double id) {
        lights.add(new PointLight(x, y, z, id));
    }

    /**
     * Computes the illuminated color of a 3D points of given position, normal and
     * color, and given the camera position and material parameters.
     *
     * @param position       the position of the point in 3 coordinates
     * @param normal         the normal at the point
     * @param color          the color of the point
     * @param cameraPosition the position of the camera
     * @param ka             the ambient reflection coefficient
     * @param kd             the diffuse reflection coefficient
     * @param ks             the specular reflection coefficient
     * @param s              the shininess coefficient
     * @return the illuminated color of the point as an array of 3 doubles
     */
    public double[] applyLights(final Vector position, final Vector normal,
            final double[] color, final Vector cameraPosition,
            final double ka, final double kd, final double ks, final double s) {

        // total light intensity
        double I = 0.0;

        for (final Light light : lights) {
            I += light.getContribution(position, normal, color,
                    cameraPosition, ka, kd, ks, s);
        }

        return new double[] {
                clampColor(color[0] * I),
                clampColor(color[1] * I),
                clampColor(color[2] * I)
        };
    }

    private static double clampColor(final double value) {
        final double min = 0.0;
        final double max = 1.0;
        return MathUtils.clamp(value, min, max);
    }

    /**
     * Reset the list of lights.
     */
    public void reset() {
        lights.clear();
    }
}
