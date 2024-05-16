package unibo.citysimulation.model.person;

import java.util.Optional;
import unibo.citysimulation.model.transport.TransportLine;
import unibo.citysimulation.model.zone.ZoneTable;
import unibo.citysimulation.utilities.Pair;

public class StaticPersonImpl implements StaticPerson {
    private int money;
    private Optional<Pair<Integer, Integer>> position;
    protected final PersonData personData;
    protected PersonState state;
    protected Pair<Integer, Integer> homePosition;
    protected TransportLine transportLine;
    protected int tripDuration;

    public StaticPersonImpl(PersonData personData, int money) {
        this.personData = personData;
        this.money = money;
        this.state = PersonState.AT_HOME;
        this.homePosition = personData.residenceZone().getRandomPosition();
        this.position = Optional.of(homePosition);
        this.getTrip();
    }

    public PersonData getPersonData() {
        return personData;
    }

    public Optional<Pair<Integer, Integer>> getPosition() {
        return position;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public PersonState getState() {
        return state;
    }

    protected void setState(PersonState state) {
        this.state = state;
    }

    public TransportLine getTransportLine() {
        return transportLine;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    protected void updatePosition() {
        switch (this.state) {
            case MOVING:
                this.position = Optional.empty();
                break;
            case WORKING:
                Pair<Integer, Integer> businessPosition = personData.business().getPosition();
                int newX = businessPosition.getFirst() + getRandomDeviation();
                int newY = businessPosition.getSecond() + getRandomDeviation();
                this.position = Optional.of(new Pair<>(newX, newY));
                break;
            case AT_HOME:
                this.position = Optional.of(homePosition);
                break;
        }
    }
    
    private int getRandomDeviation() {
        // Genera un numero casuale compreso tra -20 e 20
        return (int) (Math.random() * 41) - 20;
    }

    private void getTrip() {
        System.out.println("dentro getTrip");
        if (personData.residenceZone() == personData.business().getZone()) {
            this.tripDuration = 0;
        } else {
            this.transportLine = ZoneTable.getInstance().getTransportLine(personData.residenceZone(), personData.business().getZone());
            tripDuration = ZoneTable.getInstance().getTripDuration(transportLine);
        }
        System.out.println("in getTrip: " + tripDuration);
    }
}
