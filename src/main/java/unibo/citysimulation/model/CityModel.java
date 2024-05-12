package unibo.citysimulation.model;

import unibo.citysimulation.model.business.Business;
import unibo.citysimulation.model.business.BusinessFactory;
import unibo.citysimulation.model.clock.ClockModel;
import unibo.citysimulation.model.clock.ClockObserverPerson;
import unibo.citysimulation.model.person.DynamicPerson;
import unibo.citysimulation.model.person.PersonFactory;
import unibo.citysimulation.model.transport.TransportFactory;
import unibo.citysimulation.model.transport.TransportLine;
import unibo.citysimulation.model.zone.Zone;
import unibo.citysimulation.model.zone.ZoneFactory;
import unibo.citysimulation.model.zone.ZoneTable;
import unibo.citysimulation.utilities.ConstantAndResourceLoader;
import unibo.citysimulation.utilities.Pair;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the model of the city simulation, containing zones, transports, businesses, and people.
 */
public class CityModel {
    private List<Zone> zones;
    private List<TransportLine> transports;
    private ZoneTable zoneTable;
    private List<Business> businesses;
    private List<List<DynamicPerson>> people;
    private MapModel mapModel;
    private ClockModel clockModel;

    /**
     * Constructs a CityModel object with default settings.
     */
    public CityModel() {
        this.mapModel = new MapModel();
        this.clockModel = new ClockModel(365);
        this.zones = ZoneFactory.createZonesFromFile();
    }

    /**
     * Creates entities such as zones, transports, businesses, and people.
     * @param numberOfPeople The number of people to create in the simulation.
     */
    public void createEntities(int numberOfPeople) {
        // Create zones
        //this.zones = ZoneFactory.createZonesFromFile();
        System.out.println("Zones created. " + zones.size());

        // Create transports
        this.transports = TransportFactory.createTransportsFromFile(zones);
        System.out.println("Transports created. " + transports.size());

        // Create zone table
        ZoneTable.getInstance().addPair(zones.get(0), zones.get(1), transports.get(0));
        ZoneTable.getInstance().addPair(zones.get(1), zones.get(2), transports.get(1));
        ZoneTable.getInstance().addPair(zones.get(0), zones.get(2),transports.get(2));

        // Create businesses
        this.businesses = BusinessFactory.createBusinesses(zones);
        System.out.println("Businesses created. " + businesses.size());

        // Create people
        this.people = new ArrayList<>();
        for (var zone : zones) {
            this.people.add(PersonFactory.createGroupOfPeople((int) (numberOfPeople * (zone.businessPercents()/100)),
            zone.wellfareMinMax(), businesses, zone, zoneTable));
        }

        // Add people as observers to clock model
        clockModel.addObserver(new ClockObserverPerson(people));

        System.out.println("People groups created. " + people.size());
        for (var group : people) {
            System.out.println("Group size: " + group.size());
        }

        // Print details of each person
        for (var group : people) {
            for (var person : group) {
                System.out.println(person.getPersonData().name() + ", " + person.getPersonData().age() + ", " + person.getMoney() + ", " +
                person.getPersonData().business().getName() + ", " + person.getPersonData().business().getZone().name() + ", " + person.getPersonData().residenceZone().name()
                + ", " + person.getTripDuration());
            }
        }
    }

    public Pair<Integer,Integer> getFrameSize(){
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calculate the maximum dimensions based on the screen size and a constant percentage
        int maxWidth = (int) (screenSize.getWidth() * ConstantAndResourceLoader.SCREEN_SIZE_PERCENTAGE);
        int maxHeight = (int) (screenSize.getHeight() * ConstantAndResourceLoader.SCREEN_SIZE_PERCENTAGE);

        // Calculate the frame dimensions based on the maximum dimensions
        int frameHeight = maxHeight > maxWidth / 2 ? maxWidth / 2 : maxHeight;
        int frameWidth = frameHeight * 2;

        // Create and return the window model with the calculated dimensions
        return new Pair<>(frameWidth, frameHeight);
    }

    /**
     * Gets the map model associated with this city model.
     * @return The map model.
     */
    public MapModel getMapModel() {
        return this.mapModel;
    }

    /**
     * Gets the clock model associated with this city model.
     * @return The clock model.
     */
    public ClockModel getClockModel() {
        return this.clockModel;
    }

    /**
     * Gets the list of zones in the city model.
     * @return The list of zones.
     */
    public List<Zone> getZones() {
        return this.zones;
    }

    /**
     * Gets the list of transport lines in the city model.
     * @return The list of transport lines.
     */
    public List<TransportLine> getTransportLines() {
        return this.transports;
    }
    public List<DynamicPerson> getAllPeople() {
        List<DynamicPerson> allPeople = new ArrayList<>();
        for (var group : this.people) {
            allPeople.addAll(group);
        }
        return allPeople;
    }
}
