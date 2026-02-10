package renderer.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import renderer.controller.ImageWrapper;

public class RenderPanel extends JPanel {

    /**
     * By default the size of the pixel of the render is 1 px by represented pixel.
     */
    private static final int DEFAULT_PIXEL_SIZE = 1;

    /**
     * The rendered image.
     */
    private BufferedImage renderedImage;

    /**
     * The width of the screen.
     */
    private int width;

    /**
     * The height of the screen.
     */
    private int height;

    /**
     * The size of the pixel represented.
     */
    private int pixelSize;

    /** The gui app. */
    private final GUIApp gui;

    /**
     * Creates a RenderPanel from the app, the width, the height and the pixel size.
     *
     * @param app       the app
     * @param width     the width
     * @param height    the height
     * @param pixelSize the pixel size
     */
    public RenderPanel(final GUIApp app, final int width,
            final int height, final int pixelSize) {
        // initialize the JPanel
        super();
        gui = app;

        // fields
        this.height = height;
        this.width = width;
        this.pixelSize = pixelSize;

        // set the size of the panel
        final int realWidth = width * pixelSize;
        final int realHeight = height * pixelSize;

        renderedImage = new BufferedImage(realWidth,
                realHeight,
                BufferedImage.TYPE_3BYTE_BGR);

        final Dimension dim = new Dimension(realWidth, realHeight);
        setSize(dim);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setVisible(true);
    }

    /**
     * Creates a render panel in the app, with a width and a height.
     *
     * @param app    the app
     * @param width  the width of the screen
     * @param height the height of the screen
     */
    public RenderPanel(final GUIApp app, final int width, final int height) {
        this(app, width, height, DEFAULT_PIXEL_SIZE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (renderedImage == null) {
            return;
        }
        ((Graphics2D) g).drawImage(renderedImage,
                new AffineTransform(1f, 0f, 0f, 1f, 0, 0),
                null);
    }


    /**
     * Gets the pixels size.
     *
     * @return the pixelsize
     */
    public int getPixelSize() {
        return pixelSize;
    }

    /**
     * Updates the dimensions of the screen.
     *
     * @param screenW the new width
     * @param screenH the new height
     */
    public void updateDims(final int screenW, final int screenH) {
        width = screenW;
        height = screenH;

        final Dimension dim = getSize();
        dim.width = width;
        dim.height = height;

        setSize(dim);
        setPreferredSize(dim);
        setMinimumSize(dim);
        gui.updateDims();
    }

    /**
     * Update the render with the given one.
     * @param render the new render.
     */
    public void setImage(ImageWrapper render) {
        updateDims(render.getWidth(), render.getHeight());
        this.renderedImage = render;
        repaint();
    }
}
