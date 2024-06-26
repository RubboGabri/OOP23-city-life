package unibo.citylife.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unibo.citysimulation.model.person.api.TransportStrategy;
import unibo.citysimulation.model.person.impl.TransportStrategyImpl;
import unibo.citysimulation.model.transport.api.TransportFactory;
import unibo.citysimulation.model.transport.api.TransportLine;
import unibo.citysimulation.model.transport.impl.TransportFactoryImpl;
import unibo.citysimulation.model.zone.Zone;
import unibo.citysimulation.model.zone.ZoneCreation;
import unibo.citysimulation.model.zone.ZoneTableCreation;

class TransportStrategyImplTest {

    private TransportStrategy transportStrategy;
    private List<TransportLine> allLines;

    @BeforeEach
    public void setUp() {
        final TransportFactory transportFactory = new TransportFactoryImpl();
        final List<Zone> zones = ZoneCreation.createZonesFromFile();
        allLines = transportFactory.createTransportsFromFile(zones);
        ZoneTableCreation.createAndAddPairs(zones, allLines);

        transportStrategy = new TransportStrategyImpl();
    }

    @Test
    void testIsCongested() {
        final List<TransportLine> lines = List.of(allLines.get(0), allLines.get(1));

        assertFalse(transportStrategy.isCongested(lines));

        lines.forEach(l -> IntStream.range(0, l.getCapacity()).forEach(n -> l.incrementPersonInLine()));

        assertTrue(transportStrategy.isCongested(lines));
    }

        @Test
    void testCalculateArrivalTime() {
        final int exampleTime = 1800;

        final int arrivalTime = transportStrategy.calculateArrivalTime(exampleTime, exampleTime);
        assertEquals(exampleTime * 2, arrivalTime);
    }

    @Test
    void testIncrementAndDecrementPersonsInLine() {
        final TransportLine line = allLines.get(0);

        assertEquals(0, line.getPersonInLine());

        transportStrategy.incrementPersonsInLine(List.of(line));

        assertEquals(1, line.getPersonInLine());

        transportStrategy.decrementPersonsInLine(List.of(line));

        assertEquals(0, line.getPersonInLine());
    }
}
