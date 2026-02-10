import org.junit.Test;

import renderer.core.shader.Fragment;
import renderer.controller.ImageWrapper;
import renderer.core.rasterizer.Rasterizer;
import renderer.core.shader.Shader;

/**
 * Test class for the Rasterizer class.
 *
 * @author: cdehais
 */
public class TestRasterizer {

    static class TestShader extends Shader {


        @Override
        public void shade(Fragment fragment) {
            System.out.println(
                "  fragment: (" + fragment.getX() + ", " + fragment.getY() + ")"
                    + " - color = (" + fragment.getColor() + ")");
        }
        @Override
        public void reset() {
            // Nothing to reset
        }
    }

    /**
     * Test rasterizing an edge.
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

        System.out.println("OFF\n# Test Start");

        TestShader shader = new TestShader();
        ImageWrapper screen = new ImageWrapper();
        shader.init(null, screen);
        Rasterizer rasterizer = new Rasterizer(shader);

        System.out.println("Rasterizing edge");

        final int[] fragment1Coords = {0, 20};
        final int[] fragment2Coords = {5, -35};
        final float[] blackColor = {0, 0, 0};
        final float[] greenColor = {50f / 255, 100f / 255, 0};

        Fragment v1 = new Fragment(fragment1Coords[0], fragment1Coords[1]);
        v1.setColor(blackColor[0], blackColor[1], blackColor[2]);
        Fragment v2 = new Fragment(fragment2Coords[0], fragment2Coords[1]);
        v2.setColor(greenColor[0], greenColor[1], greenColor[2]);

        rasterizer.rasterizeEdge(v1, v2);
    }

}
