package unibo.citysimulation.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalTime;
import java.util.Optional;

import unibo.citysimulation.model.CityModel;
import unibo.citysimulation.model.clock.api.ClockObserver;
import unibo.citysimulation.model.map.api.MapModel;
import unibo.citysimulation.model.zone.Zone;
import unibo.citysimulation.utilities.Pair;
import unibo.citysimulation.view.WindowView;
import unibo.citysimulation.view.map.MapPanel;
import unibo.citysimulation.view.sidepanelsfffff.InfoPanel;

import java.util.Objects;

/**
 * Controller class responsible for handling mouse events on the map and updating the view based on the model state.
 * Implements MouseListener to handle mouse events and ClockObserver to update the map as time progresses.
 */
public final class MapController implements MouseListener, ClockObserver {
    private final InfoPanel infoPanel;
    private final MapPanel mapPanel;
    private final MapModel mapModel;
    private final CityModel cityModel;

    /**
     * Constructs a MapController object.
     *
     * @param cityModel  The CityModel interface containing methods to access and modify city data.
     * @param windowView The WindowView interface containing methods to access and modify info and map panels.
     */
    public MapController(final CityModel cityModel, final WindowView windowView) {
        // Ensure that the provided cityModel is not null
        this.cityModel = Objects.requireNonNull(cityModel, "cityModel must not be null");
        this.infoPanel = windowView.getInfoPanel();
        this.mapPanel = windowView.getMapPanel();
        this.mapModel = cityModel.getMapModel();

        initialize();

        // Add this controller as a MouseListener to the mapPanel
        mapPanel.addMouseListener(this);
    }

    /**
     * Initializes the map model and view with initial data from the city model.
     * Sets up observers and configures initial transport information and congestion levels.
     */
    private void initialize() {
        cityModel.getClockModel().addObserver(this);
        mapModel.setMaxCoordinates((int) cityModel.getFrameWidth() / 2, (int) cityModel.getFrameHeight());
        mapModel.setTransportInfo(cityModel.getTransportLines());
        mapModel.setTransportCongestion(cityModel.getTransportLines());

        mapPanel.setImage(mapModel.getImage());
        mapPanel.setLinesInfo(mapModel.getLinesPointsCoordinates(), mapModel.getTransportNames());
        mapPanel.setLinesColor(mapModel.getColorList());
    }

    /**
     * Handles mouse click events on the map panel.
     * Translates the click coordinates and updates the zone information displayed in the info panel.
     *
     * @param e the MouseEvent object containing details about the mouse click
     */
    private void handleMouseClick(final MouseEvent e) {
        final int x = (int) ((double) e.getX() / mapPanel.getWidth() * 1000);
        final int y = (int) ((double) e.getY() / mapPanel.getHeight() * 1000);

        updateZoneInfo(x, y);
        mapModel.setMaxCoordinates((int) cityModel.getFrameWidth() / 2, (int) cityModel.getFrameHeight());
    }

    /**
     * Updates the information panel with details of the zone located at the given coordinates.
     *
     * @param x the x-coordinate of the mouse click
     * @param y the y-coordinate of the mouse click
     */
    private void updateZoneInfo(final int x, final int y) {
        final Optional<Zone> selectedZone = cityModel.getZoneByPosition(new Pair<>(x, y));
        selectedZone.ifPresentOrElse(zone -> updateInfoPanelWithZone(zone, x, y), () -> clearInfoPanel(x, y));
    }

    /**
     * Updates the information panel with details of the given zone.
     *
     * @param zone the Zone object representing the selected zone
     * @param x    the x-coordinate of the mouse click
     * @param y    the y-coordinate of the mouse click
     */
    private void updateInfoPanelWithZone(final Zone zone, final int x, final int y) {
        infoPanel.updatePositionInfo(x, y);
        infoPanel.updateZoneName(zone.name());
        cityModel.getPeopleInZone(zone.name()).ifPresentOrElse(
                infoPanel::updateNumberOfPeople,
                () -> infoPanel.updateNumberOfPeople(0));
        infoPanel.updateNumberOfBusiness(cityModel.getBusinessesInZone(zone.name()));
        infoPanel.updateAvaragePay(cityModel.avaragePayZone(zone));
        infoPanel.updateNumberOfDirectLines(cityModel.getNumberOfDirectLinesFromZone(zone));
    }

    /**
     * Clears the information panel when no zone is found at the given coordinates.
     *
     * @param x the x-coordinate of the mouse click
     * @param y the y-coordinate of the mouse click
     */
    private void clearInfoPanel(final int x, final int y) {
        infoPanel.updatePositionInfo(x, y);
        infoPanel.updateZoneName("");
        infoPanel.updateNumberOfPeople(0);
        infoPanel.updateNumberOfBusiness(0);
        infoPanel.updateAvaragePay(0);
        infoPanel.updateNumberOfDirectLines(0);
    }

    /**
     * Updates the map model when the time is updated.
     *
     * @param currentTime the current time
     * @param currentDay  the current day
     */
    @Override
    public void onTimeUpdate(final LocalTime currentTime, final int currentDay) {

        mapModel.setTransportCongestion(cityModel.getTransportLines());
        mapPanel.setLinesColor(mapModel.getColorList());
        mapPanel.setEntities(mapModel.getPersonInfos(cityModel.getAllPeople()),
                mapModel.getBusinessInfos(cityModel.getBusinesses()));
    }

    /**
     * When the mouse clicks on the map in the MapPanel this method is called and
     * updates the map model thanks to method handleMouseClick. This method updates
     * also the InfoPanel.
     * 
     * @param e
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        handleMouseClick(e);
    }

    // Unused mouse events - implemented as empty methods to fulfill

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }
}
