package renderer.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import renderer.controller.ColorMapFactory;
import renderer.controller.Renderer;
import renderer.controller.ShaderFactory;

public class MenuPanel extends JPanel {

    /**
     * This panel isn't double buffered.
     */
    private static final boolean IS_DOUBLE_BUFFERED = false;
    /**
     * Selected state.
     */
    private static final boolean SELECTED = true;
    /**
     * The path to the cube scene.
     */
    private static final String CUBE_ENDPOINT = "data/example0.scene";
    /**
     * The path to the rabbit.
     */
    private static final String RABBIT_ENDPOINT = "data/example1.scene";
    /**
     * The path to suzanne.
     */
    private static final String SUZANNE_ENDPOINT = "data/example2.scene";
    /**
     * The path to the textured wall.
     */
    private static final String TEXTURE_ENDPOINT = "data/example_textured.scene";
    /**
     * The shift of the button.
     */
    private static final int TAB_SIZE_PIXEL = 30;

    /**
     * A Disabled constant.
     */
    private static final boolean DISABLED = false;

    /**
     * The render panel of the app.
     */
    private final RenderPanel renderPanel;
    /**
     * the constraints to build the pane.
     */
    private final GridBagConstraints constraints;

    /**
     * The insets to display nice radio button.
     */
    private final Insets insetsRadio;
    /**
     * The insets to display nice checkbox button.
     */
    private final Insets insetsCheckBox;

    /**
     * The Group of mesh selection.
     */
    private final ButtonGroup meshGroup;
    /**
     * The cube mesh selection button.
     */
    private final JRadioButton cube;
    /**
     * The rabbit mesh selection button.
     */
    private final JRadioButton rabbit;
    /**
     * The suzanne mesh selection button.
     */
    private final JRadioButton suzanne;
    /**
     * The wall texture selection button.
     */
    private final JRadioButton texture;
    /**
     * The file name input text field.
     */
    private final JTextField filenameTextField;

    /**
     * The ColorMap selection comboBox.
     */
    private final JComboBox<ColorMapFactory.Maps> colorMapComboBox;

    /**
     * The texture ComboBox Input.
     */
    private final JComboBox<String> textureComboBox;

    /**
     * The shader combo box.
     */
    private final JComboBox<String> shaderComboBox;

    /**
     * The render radio button group.
     */
    private final ButtonGroup renderGroup;

    /**
     * The draw vertex radio button.
     */
    private final JRadioButton drawVertexRadio;

    /**
     * The draw wire frame radio button.
     */
    private final JRadioButton drawWireframeRadio;
    /**
     * The draw solid radio button.
     */
    private final JRadioButton drawSolidRadio;
    /**
     * The rasterizer group button.
     */
    private final ButtonGroup rasterizerGroup;
    /**
     * The simple rasterizer button.
     */
    private final JRadioButton simpleRasterizer;
    /**
     * The perspective rasterizer button.
     */
    private final JRadioButton persperctiveRasterizer;

    /**
     * The draw normal option check box.
     */
    private final JCheckBox drawNormalCheckBox;
    /**
     * The lighting option check box.
     */
    private final JCheckBox lightingCheckBox;
    /**
     * The combine texture with color check box.
     */
    private final JCheckBox combineColorCheckBox;

    // ===================================================================================
    // controller part
    /**
     * The renderer used to make a render.
     */
    private final Renderer render;

    /**
     * Creates a MenuPanel from a RenderPanel.
     *
     * @param renderPanel the render panel
     */
    public MenuPanel(final RenderPanel renderPanel) {
        // set up the panel
        super(IS_DOUBLE_BUFFERED);

        // set up the renderer
        Renderer tmp;
        try {
            tmp = new Renderer();
        } catch (IOException e) {
            // should not be reach
            e.printStackTrace();
            tmp = null;
            ShaderFactory.init();
        }
        render = tmp;

        // set up the renderPanel
        this.renderPanel = renderPanel;

        // add a grid bag layout
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // set up the button insets

        // radio insets
        insetsRadio = new JButton().getInsets();
        // create a tab
        insetsRadio.left = TAB_SIZE_PIXEL;

        // checkbox insets
        insetsCheckBox = new JCheckBox().getInsets();
        // create a tab
        insetsCheckBox.left = TAB_SIZE_PIXEL;

        // fill the panel
        // add a title
        constraints.gridx = 0;
        constraints.gridy++;
        add(new JLabel("MENU"), constraints);

        // mesh radio
        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Filename"), constraints);
        meshGroup = new ButtonGroup();
        cube = new JRadioButton("Cube");
        rabbit = new JRadioButton("Rabbit");
        suzanne = new JRadioButton("Suzanne");
        texture = new JRadioButton("Texture");

        filenameTextField = new JTextField();
        // set up the buttons
        meshRadioConfiguration();

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Shader"), constraints);

        // shader ComboBox
        shaderComboBox = new JComboBox<>(ShaderFactory.getShaderSetAsStringArray());
        shaderComboBoxConfiguration();

        // DepthShader ColorMap

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Depth Shader Color Map"), constraints);

        // Combo box
        colorMapComboBox = new JComboBox<>(ColorMapFactory.Maps.values());
        colorMapConfiguration();

        // Texture Part

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Texture"), constraints);

        String[] availableTexture = getAvailableTexture();
        textureComboBox = new JComboBox<>(availableTexture);
        if (availableTexture.length == 0) {
            textureComboBox.setEnabled(DISABLED);
        }
        textureConfiguration();

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Render"), constraints);

        renderGroup = new ButtonGroup();
        // radio button to draw the normals
        drawVertexRadio = new JRadioButton("Draw vertices");

        // radio button to draw the normals
        drawWireframeRadio = new JRadioButton("Draw wireframe");

        // radio button to enable the lighting
        drawSolidRadio = new JRadioButton("Draw solid");

        // set up the buttons
        renderConfiguration();

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Rasterizer"), constraints);

        rasterizerGroup = new ButtonGroup();
        simpleRasterizer = new JRadioButton("Rasterizer");
        persperctiveRasterizer = new JRadioButton("Perspective Rasterizer");
        // set up the buttons
        rasterizerConfiguration();

        // add a subtitle
        constraints.gridy++;
        add(new JLabel("Options"), constraints);

        // check box to draw the normals
        drawNormalCheckBox = new JCheckBox("Draw normals");

        // check box to enable the lighting
        lightingCheckBox = new JCheckBox("Lighting");

        // check box to combine texture and origin color
        combineColorCheckBox = new JCheckBox("Combine color with texture");

        // set up the buttons
        optionConfiguration();

        // add a update button (useless normally)
        // constraints.gridy++;
        // final JButton but = new JButton("Render");
        // add(but, constraints);
        // but.addActionListener(e -> {
        //     updateRender();
        // });

        // start configuration
        setConfiguration();
    }

    /**
     * Sets the color Map part up.
     */
    private void colorMapConfiguration() {
        colorMapComboBox.setSelectedItem(ColorMapFactory.Maps.VERIDIS);
        colorMapComboBox.addItemListener(e -> {
            ColorMapFactory.Maps map =
                    (ColorMapFactory.Maps) colorMapComboBox.getSelectedItem();
            render.setColorMap(map);
            updateRender();
        });
        constraints.gridy++;
        add(colorMapComboBox, constraints);
    }

    /**
     * return the list of path from data which finished by `.jpg`.
     *
     * @return a array of String
     */
    private String[] getAvailableTexture() {
        final Set<String> tmp = new HashSet<>();
        String endpoint = "data/";
        File data = new File(endpoint);

        String[] fileList = data.list();
        if (fileList == null) {
            return new String[0];
        }

        for (String file : fileList) {
            if (file.endsWith(".jpg")) {
                tmp.add(file);
            }
        }

        // transform to array
        final String[] res = new String[tmp.size()];
        Iterator<String> it = tmp.iterator();
        int i = 0;
        while (it.hasNext()) {
            res[i++] = it.next();
        }
        return res;
    }

    private void textureConfiguration() {
        constraints.gridy++;
        textureComboBox.addActionListener(e -> {
            final String textureSelected = "data/"
                    + (String) textureComboBox.getSelectedItem();
            if (!render.setTexture(textureSelected)) {
                JOptionPane.showMessageDialog(textureComboBox,
                        "Error : Texture could not been correctly loaded.",
                        "Texture Loading",
                        JOptionPane.ERROR_MESSAGE);
            }
            updateRender();
        });
        add(textureComboBox, constraints);
    }

    private void shaderComboBoxConfiguration() {
        constraints.gridy++;
        shaderComboBox.addActionListener(e -> {
            final String shaderSelected = (String) shaderComboBox.getSelectedItem();
            if (!render.setShader(shaderSelected)) {
                JOptionPane.showMessageDialog(shaderComboBox,
                        "Error : creating the shader using the Factory.",
                        "Shader creation failed",
                        JOptionPane.ERROR_MESSAGE);
            }
            textureComboBox.setEnabled(shaderSelected.contains("Texture"));
            colorMapComboBox.setEnabled(shaderSelected.contains("Depth"));
            updateRender();
        });
        add(shaderComboBox, constraints);
    }

    /**
     * Set up the mesh button group.
     */
    private void meshRadioConfiguration() {
        // Radio mesh

        // cube radio
        cube.setMargin(insetsRadio);
        constraints.gridy++;
        cube.addItemListener(e -> {
            if (!cube.isSelected()) {
                return;
            }
            try {
                render.setScene(CUBE_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(cube, constraints);
        meshGroup.add(cube);

        // rabbit radio
        rabbit.setMargin(insetsRadio);
        constraints.gridy++;
        rabbit.addItemListener(e -> {
            if (!rabbit.isSelected()) {
                return;
            }
            try {
                render.setScene(RABBIT_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(rabbit, constraints);
        meshGroup.add(rabbit);

        // suzanne radio
        suzanne.setMargin(insetsRadio);
        constraints.gridy++;
        suzanne.addItemListener(e -> {
            if (!suzanne.isSelected()) {
                return;
            }
            try {
                render.setScene(SUZANNE_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(suzanne, constraints);
        meshGroup.add(suzanne);

        // texture radio
        texture.setMargin(insetsRadio);
        constraints.gridy++;
        texture.addItemListener(e -> {
            if (!texture.isSelected()) {
                return;
            }
            try {
                render.setScene(TEXTURE_ENDPOINT);
            } catch (final IOException e1) {
                // should not be reach
                e1.printStackTrace();
            }
            updateRender();
        });
        add(texture, constraints);
        meshGroup.add(texture);

        // another filename
        constraints.gridy++;
        add(new JLabel("With another scene file"), constraints);
        constraints.gridy++;
        filenameTextField.addActionListener(e -> {
            try {
                render.setScene("data/" + filenameTextField.getText());
            } catch (final IOException e1) {
                JOptionPane.showMessageDialog(filenameTextField,
                        e1.getMessage(),
                        e1.getClass().getSimpleName(),
                        JOptionPane.ERROR_MESSAGE);
            }
            updateRender();
        });
        add(filenameTextField, constraints);
    }

    /**
     * Set up the render button group.
     */
    private void renderConfiguration() {
        // remove the border on the component
        drawVertexRadio.setMargin(insetsRadio);
        constraints.gridy++;
        add(drawVertexRadio, constraints);


        // remove the border on the component
        drawWireframeRadio.setMargin(insetsRadio);
        constraints.gridy++;
        add(drawWireframeRadio, constraints);

        // remove the border on the component
        drawSolidRadio.setMargin(insetsRadio);
        constraints.gridy++;
        add(drawSolidRadio, constraints);

        drawVertexRadio.addItemListener(e -> {
            render.setVertexRendered(drawVertexRadio.isSelected());
            updateRender();
        });

        // add the interdependant Item listener
        drawWireframeRadio.addItemListener(e -> {
            render.setWiredRendered(drawWireframeRadio.isSelected());
            updateRender();
        });

        drawSolidRadio.addItemListener(e -> {
            render.setSolidRendered(drawSolidRadio.isSelected());
            updateRender();
        });

        renderGroup.add(drawVertexRadio);
        renderGroup.add(drawWireframeRadio);
        renderGroup.add(drawSolidRadio);

    }

    /**
     * Set up the rasterizer button group.
     */
    private void rasterizerConfiguration() {

        // rasterizer
        simpleRasterizer.setMargin(insetsRadio);
        constraints.gridy++;
        simpleRasterizer.addItemListener(e -> {
            if (!simpleRasterizer.isSelected()) {
                return;
            }
            render.setRasterizer();
            updateRender();
        });
        add(simpleRasterizer, constraints);
        rasterizerGroup.add(simpleRasterizer);

        // rasterizer
        persperctiveRasterizer.setMargin(insetsRadio);
        constraints.gridy++;
        persperctiveRasterizer.addItemListener(e -> {
            if (!persperctiveRasterizer.isSelected()) {
                return;
            }
            render.setPerspectiveCorrectRasterizer();
            updateRender();
        });
        add(persperctiveRasterizer, constraints);
        rasterizerGroup.add(persperctiveRasterizer);

    }

    /**
     * Set up the option button group.
     */
    private void optionConfiguration() {
        drawNormalCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                render.setNormalsRendered(drawNormalCheckBox.isSelected());
                updateRender();
            }
        });

        // remove the border on the component
        drawNormalCheckBox.setMargin(insetsCheckBox);
        constraints.gridy++;
        add(drawNormalCheckBox, constraints);

        lightingCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                render.setLightingEnabled(lightingCheckBox.isSelected());
                updateRender();
            }
        });

        // remove the border on the component
        lightingCheckBox.setMargin(insetsCheckBox);
        constraints.gridy++;
        add(lightingCheckBox, constraints);

        combineColorCheckBox.addItemListener(e -> {
            render.setCombineWithBaseColor(combineColorCheckBox.isSelected());
            updateRender();
        });

        // remove the border on the component
        combineColorCheckBox.setMargin(insetsCheckBox);
        constraints.gridy++;
        add(combineColorCheckBox, constraints);
    }

    /**
     * Set up the selected button and make a first render.
     */
    private void setConfiguration() {
        // set the start configuration
        simpleRasterizer.setSelected(SELECTED);
        cube.setSelected(SELECTED);
        shaderComboBox.setSelectedItem("SimpleShader");
        textureComboBox.setSelectedItem("brick.jpg");
        drawVertexRadio.setSelected(SELECTED);
    }

    /**
     * Update the render.
     */
    private void updateRender() {
        renderPanel.setImage(render.render());
    }
}
