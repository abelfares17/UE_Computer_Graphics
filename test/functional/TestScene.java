import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import renderer.algebra.Vector;
import renderer.core.mesh.Scene;


/**
 * Test class from the Scene class.
 *
 * @author: cdehais
 */
public class TestScene {

    /**
     * Print an array of doubles.
     * This method is used to print the source coordinates and material properties.
     * @param title the title to print before the array
     * @param array the array of doubles to print
     */
    private void printArray(String title, double[] array) {
        StringBuilder sb = new StringBuilder();
        if (title != null) {
            sb.append(title).append(": ");
        }
        for (double v : array) {
            sb.append(v).append(" ");
        }
        System.out.println(sb.toString().trim());
    }

    /**
     * Test the Scene class.
     * This test reads a scene file and prints out the parameters.
     * The scene file is expected to be in the "data" directory.
     */
    @Test
    public void test() throws Exception {

        Scene scene = new Scene("data/example1.scene");
        final String meshFilename = scene.getMeshFileName();
        assertNotNull(meshFilename);
        final Vector cameraPosition = scene.getCameraPosition();
        assertNotNull(cameraPosition);
        final Vector cameraLookAt = scene.getCameraLookAt();
        assertNotNull(cameraLookAt);
        final Vector cameraUp = scene.getCameraUp();
        assertNotNull(cameraUp);
        // double cameraXLimit = scene.getCameraXLimit ();
        final double cameraFocal = scene.getCameraFocal();
        final int screenW = scene.getScreenW();
        final int screenH = scene.getScreenH();
        final double ambientI = scene.getAmbientI();
        final double sourceI = scene.getSourceI();
        final double[] sourceCoord = scene.getSourceCoord();
        assertNotNull(sourceCoord);
        final double[] material = scene.getMaterial();
        assertNotNull(material);

        System.out.println("# Test output");
        System.out.println(meshFilename);

        System.out.println(cameraPosition);
        System.out.println(cameraLookAt);
        System.out.println(cameraUp);

        // System.out.println ("Camera X Limit = " + cameraXLimit);
        System.out.println("Focal = " + cameraFocal);

        System.out.println("Screen: " + screenW + " x " + screenH);

        System.out.println("Light ambient: " + ambientI);
        System.out.println("Light ambient: " + sourceI);

        printArray("Source coordinates", sourceCoord);
        printArray("Material properties", material);
    }

}
