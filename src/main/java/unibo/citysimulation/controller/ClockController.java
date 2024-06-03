package unibo.citysimulation.controller;

import java.time.LocalTime;

import unibo.citysimulation.model.clock.api.ClockModel;
import unibo.citysimulation.model.clock.api.ClockObserver;
import unibo.citysimulation.utilities.ConstantAndResourceLoader;
import unibo.citysimulation.view.sidepanels.clock.ClockPanel;

/**
 * Controller class responsible for managing the clock and its interactions with the user interface.
 */
public class ClockController implements ClockObserver {
    private final ClockPanel clockPanel;
    private final ClockModel clockModel;

    /**
     * Constructs a ClockController object.
     *
     * @param clockModel The ClockModel object representing the clock.
     * @param clockPanel The ClockPanel object representing the clock user interface.
     */
    public ClockController(final ClockModel clockModel, final ClockPanel clockPanel) {
        this.clockPanel = clockPanel;
        this.clockModel = clockModel;

        // Add action listener for the pause button
        clockPanel.addPauseButtonActionListener(e -> pauseSimulation());

        // Add action listener for the speed button
        clockPanel.addSpeedButtonActionListener(e -> changeClockSpeed());
    }

    /**
     * Updates the clock panel with the current time and day.
     *
     * @param currentTime The current time.
     * @param currentDay  The current day.
     */
    @Override
    public void onTimeUpdate(final LocalTime currentTime, final int currentDay) {
        // Update the clock panel text with current time and day
        clockPanel.setClockText(Integer.toString(currentDay), currentTime.toString());
    }

    /**
     * Sets the simulation speed based on the given speed value.
     */
    private void changeClockSpeed() {
        final int speed = clockPanel.changeSpeed();
        // Start the simulation with the new speed
        if (clockModel.getTimer() != null) {
            clockModel.startSimulation(ConstantAndResourceLoader.TIME_UPDATE_RATE / speed);
        } else {
            clockModel.setUpdateRate(ConstantAndResourceLoader.TIME_UPDATE_RATE / speed);
        }
    }

    /**
     * Pauses the simulation.
     */
    private void pauseSimulation() {
        // Pause the simulation
        clockModel.pauseSimulation();
        clockPanel.updatePauseButton(clockModel.isPaused());
    }
}
