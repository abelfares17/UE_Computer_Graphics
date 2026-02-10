package renderer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

/**
 * A GUI to display the mesh with the different shader.
 */
public class GUIApp extends JFrame {

    /**
     * By default the pixel of the screen is represented by 1 pixel.
     */
    private static final int DEFAULT_PIXEL_SIZE = 1;

    /**
     * By default a gui have a width of 100 px.
     */
    private static final int DEFAULT_WIDTH = 100;

    /**
     * By default a gui have a height of 100 px.
     */
    private static final int DEFAULT_HEIGHT = 100;

    /**
     * The margin of the menu.
     */
    private static final int MENU_MARGIN = 50;

    /**
     * The panel for the render.
     */
    private final RenderPanel renderPanel;

    /**
     * The panel for the menu to choose shader, draw normal, wire,
     * vertices, solid, textured.
     */
    private final MenuPanel menuPanel;

    /**
     * Create a GUI for a render of size (width * pixelSize + menuSize, height *
     * pixelSize).
     *
     * @param width     the width of the screen of the camera
     * @param height    the height of pixel on the screen of the camera
     * @param pixelSize the number of pixel on the GUI by pixel of the camera
     */
    public GUIApp(final int width, final int height, final int pixelSize) {
        super("Simple Inverse Rasterization Renderer (TSI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a grid bag layout
        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        // to get both panel at the top of the frame
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;

        renderPanel = new RenderPanel(this, width, height, pixelSize);

        menuPanel = new MenuPanel(renderPanel);

        add(renderPanel, constraints);
        add(menuPanel, constraints);

        // commit the adds and show the app
        pack();
        setVisible(true);
    }

    /**
     * Creates a GUIApp for a render of size (width + menuSize, height).
     *
     * @param width  the width of the screen of the camera
     * @param height the height of pixel on the screen of the camera
     */
    public GUIApp(final int width, final int height) {
        this(width, height, DEFAULT_PIXEL_SIZE);
    }

    /**
     * Creates a GUIApp by default.
     */
    public GUIApp() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Get the render Panel.
     *
     * @return the render panel
     */
    public RenderPanel getRenderPanel() {
        return renderPanel;
    }

    /**
     * Updates the dimensions of the App.
     */
    public void updateDims() {
        if (menuPanel != null) {
            setSize(new Dimension(renderPanel.getWidth() + menuPanel.getWidth(),
                    Math.max(renderPanel.getHeight(),
                            menuPanel.getHeight() + MENU_MARGIN)));
        }
    }

    /**
     * The main entry point.
     *
     * @param args useless here
     */
    public static void main(String[] args) {
        new GUIApp();
    }
}
