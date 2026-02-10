package renderer.core.light;

import renderer.algebra.Vector;

/**
 * Interface describing a light source.
 */
public abstract class Light {

    /**
     * the intensity of the light.
     */
    protected double intensity;

    /**
     * Builds a light with its intensity.
     * @param intensity The intensity of the light
     */
    public Light(double intensity) {
        this.intensity = intensity;
    }

    /**
     * Get the Intensity of the light.
     *
     * @return the intensity
     */
    public double getIntensity() {
        return this.intensity;
    }

    /**
     * Computes the illuminated color of a 3D points of given position, normal and
     * color, and given the camera position and material parameters.
     *
     * @param position       the position of the point
     * @param normal         the normal at the point
     * @param color          the color of the point
     * @param cameraPosition the position of the camera
     * @param ka             the ambient reflection coefficient
     * @param kd             the diffuse reflection coefficient
     * @param ks             the specular reflection coefficient
     * @param s              the shininess coefficient
     * @return the intensity of the light of the point
     */
    public abstract double getContribution(Vector position, Vector normal, double[] color,
            Vector cameraPosition, double ka, double kd, double ks, double s);

}
