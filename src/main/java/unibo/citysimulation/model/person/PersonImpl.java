package unibo.citysimulation.model.person;

import unibo.citysimulation.model.ClockModel;
import unibo.citysimulation.model.business.Business;
import unibo.citysimulation.model.transport.Zone;
import unibo.citysimulation.model.transport.ZoneTable;
import java.util.Optional;


public class PersonImpl implements Person {
    private String name;
    private PersonState state;
    private int money;
    private Business business;
    private Zone residenceZone;
    private ClockModel clock;
    private ZoneTable zoneTable;
    private int lastArrivingTime = 0;
    private PersonState lastDestination;



    public PersonImpl(int money, Business business, Zone residenceZone, ClockModel clock) {
        this.state = PersonState.AT_HOME;
        this.lastDestination = PersonState.WORKING;
        this.money = money;
        this.business = business;
        this.residenceZone = residenceZone;
        this.clock = clock;
        zoneTable = new ZoneTable();
    }

    public String getName() {
        return name;
    }

    @Override
    public PersonState getState() {
        return state;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void setState(PersonState state) {
        this.state = state;
    }

    @Override
    public void setMoney(int amount) {
        this.money = this.money + amount;
    }

    public Business getBusiness() {
        return business;
    }

    public Zone getResidenceZone() {
        return residenceZone;
    }

    public Optional<Zone> getCurrentZone() {
        switch (this.state) {
            case WORKING:
                return Optional.of(business.getZone());
            case AT_HOME:
                return Optional.of(residenceZone);
            default:
                return Optional.empty();
        }
    }

    public Zone getBusinessZone() {
        return business.getZone();
    }

    public boolean checkTimeToMove(int currentTime, int timeToMove, int lineDuration) {
        boolean moveBool = currentTime == timeToMove;
        if (moveBool) {
            this.setState(PersonState.MOVING);
            this.lastArrivingTime = currentTime + lineDuration;
        }
        return moveBool;
    }

    public boolean checkTimeToGoToWork() {
        int lineDuration = zoneTable.getMinutesForPair(residenceZone, business.getZone()) * 60;
        if (this.checkTimeToMove(clock.getCurrentTime().toSecondOfDay(),
            business.getOpeningTime().toSecondOfDay() - lineDuration,
            lineDuration)) {
            this.lastDestination = PersonState.WORKING;
            return true;
            }
        return false;
    }

    public boolean checkTimeToGoHome() {
        if (this.checkTimeToMove(clock.getCurrentTime().toSecondOfDay(),
            business.getClosingTime().toSecondOfDay(),
            zoneTable.getMinutesForPair(business.getZone(), residenceZone) * 60)) {
            this.lastDestination = PersonState.AT_HOME;
            return true;
            }
            return false;
    }

    public void incrementLastArrivingTime(int duration) {
        this.lastArrivingTime += duration;
    }

    public boolean checkArrivingTime() {
        if (clock.getCurrentTime().toSecondOfDay() == this.lastArrivingTime) {
            this.setState(this.lastDestination);
            return true;
        }
        return false;
    }

    public void checkState() {
        switch (this.state) {
            case MOVING:
                this.checkArrivingTime();
                break;
            case WORKING:
                this.checkTimeToGoHome();
                break;
            case AT_HOME:
                this.checkTimeToGoToWork();
                break;
        }
    }
    
}
