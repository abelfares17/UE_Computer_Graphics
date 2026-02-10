package renderer.core.shader.colormap;

import java.awt.Color;

/**
 * A ColorMap provide a list of Colors to do a color gradient.
 */
public class ColorMap {

    /**
     * The array of Color for the ColorMap.
     */
    private final Color[] lut;

    /**
     * Creates a ColorMap.
     * @param lut the list of color.
     */
    public ColorMap(final Color[] lut) {
        this.lut = lut;
    }

    /**
     * Gets the colors of the palette.
     * @param value the id of the color.
     * @return the color of the value
     */
    public Color getColor(int value) {
        return lut[value];
    }

    /**
     * Gets the number of colors the color Map contains.
     * @return the number of colors
     */
    public int length() {
        return lut.length;
    }

}
