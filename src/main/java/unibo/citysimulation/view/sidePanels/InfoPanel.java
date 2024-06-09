package unibo.citysimulation.view.sidepanels;

import unibo.citysimulation.utilities.ConstantAndResourceLoader;
import unibo.citysimulation.view.StyledPanel;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Panel that displays information about the zone clicked.
 */
public final class InfoPanel extends StyledPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel coordinates;
    private final JLabel numberOfPeople;
    private final JLabel zoneNJLabel;
    private final JLabel numberOfBusiness;
    private final JLabel numberOfAvaregePay;
    private final JLabel numberDirectLines;
    private static final int COORDINATES_LABEL_GRID_Y = 1;
    private static final int NUMBER_OF_PEOPLE_LABEL_GRID_Y = 2;
    private static final int NUMBER_OF_BUSINESS_LABEL_GRID_Y = 3;
    private static final int NUMBER_OF_AVAREGE_PAY_LABEL_GRID_Y = 4;
    private static final int NUMBER_OF_DIRECT_LINES_LABEL_GRID_Y = 5;
    /**
     * Constructs an InfoPanel with the specified background color.
     *
     * @param bgColor The background color of the panel.
     */
    public InfoPanel(final Color bgColor) {
        super(bgColor);
        // Set the layout manager to GridBagLayout
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        // Create a JLabel with the desired text
        zoneNJLabel = new JLabel("Zone:", SwingConstants.CENTER); // Align the text to the center
        zoneNJLabel.setFont(new Font("Arial", Font.BOLD, ConstantAndResourceLoader.INFO_PANEL_FONT_SIZE));
        //set color of the text
        zoneNJLabel.setForeground(Color.RED);
        // Add the JLabel to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(zoneNJLabel, gbc);
        coordinates = new JLabel("Coordinates: ");
        gbc.gridy = COORDINATES_LABEL_GRID_Y;
        add(coordinates, gbc);
        numberOfPeople = new JLabel("Number of People: ");
        gbc.gridy = NUMBER_OF_PEOPLE_LABEL_GRID_Y;
        add(numberOfPeople, gbc);
        numberOfBusiness = new JLabel("Number of Business: ");
        gbc.gridy = NUMBER_OF_BUSINESS_LABEL_GRID_Y;
        add(numberOfBusiness, gbc);
        numberOfAvaregePay = new JLabel("Avarage Pay: ");
        gbc.gridy = NUMBER_OF_AVAREGE_PAY_LABEL_GRID_Y;
        add(numberOfAvaregePay, gbc);
        numberDirectLines = new JLabel("Number of Direct Lines: ");
        gbc.gridy = NUMBER_OF_DIRECT_LINES_LABEL_GRID_Y;
        add(numberDirectLines, gbc);
    }
    /**
     * Updates the position information displayed on the panel.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public void updatePositionInfo(final int x, final int y) {
        coordinates.setText("Coordinates: (" + x + ", " + y + ")");
    }

    /**
     * Updates the number of people displayed on the panel.
     *
     * @param peopleNumber The number of people.
     */
    public void updateNumberOfPeople(final int peopleNumber) {
        numberOfPeople.setText("Number of People: " + peopleNumber);
    }
    /**
     * Updates the zone name displayed on the panel.
     *
     * @param zoneName The name of the zone.
     */
    public void updateZoneName(final String zoneName) {
        zoneNJLabel.setText("Zone: " + zoneName);
    }
    /**
     * Updates the number of businesses displayed on the panel.
     *
     * @param businessNumber The number of businesses.
     */
    public void updateNumberOfBusiness(final int businessNumber) {
        numberOfBusiness.setText("Number of Business: " + businessNumber);
    }
    /**
     * Updates the avarage pay displayed on the panel.
     * @param avaragePay The avarage pay.
     */
    public void updateAvaragePay(final double avaragePay) {
        numberOfAvaregePay.setText(String.format("Average Pay: %.2f", avaragePay));
    }
    /**
     * Updates the number of direct lines displayed on the panel.
     *
     * @param directLines The number of direct lines.
     */
    public void updateNumberOfDirectLines(final int directLines) {
        numberDirectLines.setText("Number of Direct Lines: " + directLines);
    }
}

