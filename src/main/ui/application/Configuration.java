package ui.application;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Game;
import persistence.JsonLoader;
import persistence.JsonWriter;

/*
 * class with all the methods and logic for the settings panel of the game
 */
public class Configuration {
    private Game game;
    private Board parentPanel;

    private JPanel savePanel;
    private static final int SAVE_PANEL_HEIGHT = 145;
    private JList<String> savesList;
    private JScrollPane saveScrollPane;
    private JTextField textField;
    private JButton loadButton;
    private JButton saveButton;
    private JPanel inputPanel;

    private JPanel handlingPanel;
    private static final int HANDLING_PANEL_HEIGHT = 60;
    private JPanel arrRow;
    private JSlider arrSlider;
    private JLabel arrLabel;
    private JPanel dasRow;
    private JSlider dasSlider;
    private JLabel dasLabel;
    private JPanel sdfRow;
    private JSlider sdfSlider;
    private JLabel sdfLabel;

    private static final int MAX_WIDTH = 190;
    private int width;
    private int extensionX;
    private int extensionSpeed;

    // EFFECTS: new configuration, setting the target game to modify
    public Configuration(Game game, Board parentPanel) {
        this.game = game;
        this.parentPanel = parentPanel;

        width = MAX_WIDTH;
        extensionX = 1280;
        extensionSpeed = 0;

        initializeSaveElements();
        initializeHandlingElements();
    }

    // MODIFIES: this
    // EFFECTS: increments extensionSpeed by one, increments extensionX by -extensionSpeed
    //          until reaching a maximum extension
    public void extendPanel() {
        int minExtension = parentPanel.getWidth();
        if (extensionX >= minExtension) {
            extensionSpeed = 20;
        }

        int maxExtension = parentPanel.getWidth() - width;
        if (extensionX > maxExtension) {
            extensionX = Math.max(maxExtension, extensionX - extensionSpeed);
            extensionSpeed = Math.max(1, extensionSpeed - 1);
            parentPanel.repaint();
        } else {
            extensionSpeed = 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: increments extensionSpeed by one, increments extensionX by +extensionSpeed
    //          until reaching a minimum extension
    public void retractPanel() {
        int minExtension = parentPanel.getWidth();
        if (extensionX < minExtension) {
            extensionX = Math.min(minExtension, extensionX + extensionSpeed);
            extensionSpeed++;
            parentPanel.repaint();
        } else {
            extensionSpeed = 0;
        }
    }

    /***************************
     * PAINT COMPONENTS
     ***************************/

    // EFFECTS: renders the settings tab
    public void paintComponent(Graphics2D g) {
        final int MARGIN = Board.MARGIN;
        final int PADDING = Board.PADDING;
        final int SCREEN_WIDTH = parentPanel.getWidth();
        final int SCREEN_HEIGHT = parentPanel.getHeight();

        width = SCREEN_WIDTH / 5;
        width = Math.min(width, MAX_WIDTH);

        g.setColor(Board.UI_COLOUR);
        g.drawRect(extensionX, 0, width, SCREEN_HEIGHT);

        int startingHeight = MARGIN;
        
        g.setColor(Board.TEXT_COLOUR);
        g.drawString("SAVE/LOAD", extensionX + Board.PADDING, startingHeight);

        startingHeight += PADDING;

        paintSavePanels(startingHeight);

        startingHeight += SAVE_PANEL_HEIGHT + MARGIN + PADDING;

        g.drawString("HANDLING", extensionX + Board.PADDING, startingHeight);

        startingHeight += PADDING;

        paintHandlingPanels(startingHeight);

        startingHeight += HANDLING_PANEL_HEIGHT + PADDING;

        g.drawString("CONFIG", extensionX - 80, SCREEN_HEIGHT - PADDING);
        g.setFont(new Font(Board.FONT, 2, 10));
        g.drawString("Andrew Ding 2025", extensionX + PADDING, SCREEN_HEIGHT - PADDING);
    }

    // EFFECTS: moves all setting panels to the sidepanel, with a starting height of startingHeight
    private void paintSavePanels(int startingHeight) {
        int panelWidth = width - Board.PADDING * 2;

        savePanel.setBounds(extensionX + Board.PADDING, startingHeight, panelWidth, SAVE_PANEL_HEIGHT);
        savePanel.repaint();

        textField.setPreferredSize(new Dimension(panelWidth, 20));
        textField.repaint();

        inputPanel.setPreferredSize(new Dimension(panelWidth, 25));
        inputPanel.repaint();

        saveScrollPane.setPreferredSize(new Dimension(panelWidth, SAVE_PANEL_HEIGHT - 45));
        saveScrollPane.repaint();
    }

    // EFFECTS: moves all setting panels to the sidepanel, with a starting height of startingHeight
    private void paintHandlingPanels(int startingHeight) {
        int panelWidth = width - Board.PADDING * 2;

        handlingPanel.setBounds(extensionX + Board.PADDING, startingHeight, panelWidth, HANDLING_PANEL_HEIGHT);
        handlingPanel.repaint();

        arrRow.setPreferredSize(new Dimension(panelWidth, 20));
        arrRow.repaint();

        arrLabel.setText("ARR: " + arrSlider.getValue() + "ms");
        arrLabel.repaint();

        dasRow.setPreferredSize(new Dimension(panelWidth, 20));
        dasRow.repaint();

        dasLabel.setText("DAS: " + dasSlider.getValue() + "ms");
        dasLabel.repaint();
        
        sdfRow.setPreferredSize(new Dimension(panelWidth, 20));
        sdfRow.repaint();

        String sdfValue = sdfSlider.getValue() == 41 ? "INF" : sdfSlider.getValue() + "X";
        sdfLabel.setText("SDF: " + sdfValue);
        sdfLabel.repaint();
    }

    /***************************
     * SAVE/LOAD MENU
     ***************************/

    // MODIFIES: this
    // EFFECTS: set up settings elements related to loading/saving game state
    private void initializeSaveElements() {
        savePanel = new JPanel();
        savePanel.setLayout(new GridBagLayout());

        savesList = new JList<>();
        savesList.setFont(new Font(Board.FONT, 0, 12));

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());

        initializeTextField();
        initializeSaveButton();
        initializeLoadButton();

        refreshSaves("");

        saveScrollPane = new JScrollPane(savesList);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        savePanel.add(saveScrollPane, gbc);
        savePanel.add(textField, gbc);
        gbc.weighty = 1;
        savePanel.add(inputPanel, gbc);

        savePanel.setVisible(true);

        parentPanel.add(savePanel);

        paintSavePanels(Board.MARGIN);
    }

    // MODIFIES: this
    // EFFECTS: initializes specifically the text field used for saving/searching
    private void initializeTextField() {
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                refreshSaves(textField.getText());
                parentPanel.getMainFrame().requestFocus();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes specifically the button used for saving
    private void initializeSaveButton() {
        saveButton = new JButton("SAVE");
        saveButton.setFont(new Font(Board.FONT, 1, 10));
        saveButton.setContentAreaFilled(false);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JsonWriter writer = new JsonWriter(game);
                String fileName = textField.getText().isBlank() ? "default.json" : textField.getText();
                fileName = fileName.contains(".json") ? fileName : fileName + ".json";
                try {
                    writer.write("./data/" + fileName);
                    parentPanel.showAlert("Saved successfully to ./data/" + fileName);
                } catch (FileNotFoundException e) {
                    parentPanel.showAlert("ALERT: File was not found");
                } finally {
                    refreshSaves("");
                    parentPanel.repaint();
                    parentPanel.getMainFrame().requestFocus();
                }
            }
        });
        inputPanel.add(saveButton);
    }

    // MODIFIES: this
    // EFFECTS: initializes specifically the buttons used for loading
    private void initializeLoadButton() {
        loadButton = new JButton("LOAD");
        loadButton.setFont(new Font(Board.FONT, 1, 10));
        loadButton.setContentAreaFilled(false);
        loadButton.setFocusPainted(false);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    JsonLoader loader = new JsonLoader("./data/" + savesList.getSelectedValue(), game);
                    loader.loadGameState();
                    parentPanel.repaint();
                } catch (IOException e) {
                    if (savesList.getSelectedValue() == null) {
                        parentPanel.showAlert("Please select a save!");
                    } else {
                        parentPanel.showAlert("ALERT: Something went wrong when loading from this file");
                    }
                } finally {
                    parentPanel.getMainFrame().requestFocus();
                }
            }
        });
        inputPanel.add(loadButton);
    }

    // REQURIES: search is not null
    // MODIFIES: targetPanel
    // EFFECTS: lists all saves in targetPanel
    private void refreshSaves(String search) {
        // from https://www.baeldung.com/java-list-directory-files, thanks!
        Set<String> saves = Stream.of(new File("data/").listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .filter(file -> file.contains(search))
                    .collect(Collectors.toSet());

        savesList.setListData(saves.toArray(new String[saves.size()]));
        savesList.revalidate();
        savesList.repaint();
    }

    /***************************
     * HANDLING MENU
     ***************************/
    
    // MODIFIES: this
    // EFFECTS: creates elements for the handling config panel
    private void initializeHandlingElements() {
        handlingPanel = new JPanel();
        handlingPanel.setLayout(new GridBagLayout());

        initializeArrRow();
        initializeDasRow();
        initializeSdfRow();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        handlingPanel.add(arrRow, gbc);
        handlingPanel.add(dasRow, gbc);
        gbc.weighty = 1;
        handlingPanel.add(sdfRow, gbc);

        handlingPanel.setVisible(true);

        parentPanel.add(handlingPanel);

        paintHandlingPanels(Board.MARGIN);
    }

    // MODIFIES: this
    // EFFECTS: helper for initializeHandlingElements()
    //          creates the arrRow
    private void initializeArrRow() {
        arrRow = new JPanel();
        arrRow.setLayout(new GridLayout(1, 2));

        arrSlider = new JSlider(0, 83);
        arrSlider.setValue(parentPanel.getMainFrame().getArr());
        arrSlider.setInverted(true);
        arrSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                parentPanel.getMainFrame().setArr(arrSlider.getValue());
                parentPanel.repaint();
            }
        });

        arrLabel = new JLabel("ARR: " + arrSlider.getValue() + "ms");

        arrRow.add(arrSlider);
        arrRow.add(arrLabel);
    }

    // MODIFIES: this
    // EFFECTS: helper for initializeHandlingElements()
    //          creates the dasRow
    private void initializeDasRow() {
        dasRow = new JPanel();
        dasRow.setLayout(new GridLayout(1, 2));

        dasSlider = new JSlider(17, 333);
        dasSlider.setValue(parentPanel.getMainFrame().getDas());
        dasSlider.setInverted(true);
        dasSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                parentPanel.getMainFrame().setDas(dasSlider.getValue());
                parentPanel.repaint();
            }
        });

        dasLabel = new JLabel("DAS: " + dasSlider.getValue() + "ms");

        dasRow.add(dasSlider);
        dasRow.add(dasLabel);
    }

    // MODIFIES: this
    // EFFECTS: helper for initializeHandlingElements()
    //          creates the sdfRow
    private void initializeSdfRow() {
        sdfRow = new JPanel();
        sdfRow.setLayout(new GridLayout(1, 2));

        sdfSlider = new JSlider(5, 41);
        sdfSlider.setValue(parentPanel.getMainFrame().getSdf());
        sdfSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                int value = sdfSlider.getValue() == 41 ? -1 : sdfSlider.getValue();
                parentPanel.getMainFrame().setSdf(value);
                parentPanel.repaint();
            }
        });

        sdfLabel = new JLabel("SDF: " + sdfSlider.getValue() + "X");

        sdfRow.add(sdfSlider);
        sdfRow.add(sdfLabel);
    }
}
