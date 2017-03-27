package com.floorcorn.tickettoride.model;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;


/**
 * Created by Michael on 3/2/2017.
 *
 */
public class MapFactoryTest {
    private MapFactory mapFactory;
    private final String FILE_PATH = "."
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "java"
            + File.separator + "com"
            + File.separator + "floorcorn"
            + File.separator + "tickettoride"
            + File.separator + "model"
            + File.separator + "maps"
            + File.separator;

    @Before
    public void setup(){
        mapFactory = new MapFactory();
        mapFactory.setFilePath(FILE_PATH);
    }

    /**
     * Test routes loaded from .csv
     *
     * NOTE: Make sure to change file names in MapFactory to get
     * the test data instead of the real .csv. Otherwise these test
     * cases will fail.
     * @throws Exception
     */
    @Test
    public void testGetMarsRoutes() throws Exception {
        ArrayList<Route> res = new ArrayList<>();

        //Check if any routes were loaded
        try{
            res = new ArrayList<Route>(mapFactory.getMarsRoutes());
            assertNotEquals(res.size(), 0);
        } catch (Exception e){
            System.err.println(e.getMessage());
            assertTrue(false);
        }

        //Check if correct routes were loaded
        assertEquals(res.get(0).getFirstCity().getName().toLowerCase(), "alpine");
        assertEquals(res.get(0).getSecondCity().getName().toLowerCase(), "american fork");
        assertEquals(res.get(0).getLength(), 1);

        assertEquals(res.get(1).getFirstCity().getName().toLowerCase(), "alpine");
        assertEquals(res.get(1).getSecondCity().getName().toLowerCase(), "provo");
        assertEquals(res.get(1).getColor(), TrainCardColor.RED);
        assertEquals(res.get(1).getLength(), 3);

        assertEquals(res.get(2).getFirstCity().getName().toLowerCase(), "american fork");
        assertEquals(res.get(2).getSecondCity().getName().toLowerCase(), "saratoga springs");
        assertEquals(res.get(2).getColor(), TrainCardColor.WHITE);
        assertEquals(res.get(2).getLength(), 4);

        assertEquals(res.get(3).getFirstCity().getName().toLowerCase(), "orem");
        assertEquals(res.get(3).getSecondCity().getName().toLowerCase(), "provo");
        assertEquals(res.get(3).getColor(), TrainCardColor.BLUE);
        assertEquals(res.get(3).getLength(), 1);

        assertEquals(res.get(4).getFirstCity().getName().toLowerCase(), "provo");
        assertEquals(res.get(4).getSecondCity().getName().toLowerCase(), "salt lake city");
        assertEquals(res.get(4).getColor(), TrainCardColor.YELLOW);
        assertEquals(res.get(4).getLength(), 5);

        assertEquals(res.get(5).getFirstCity().getName().toLowerCase(), "salt lake city");
        assertEquals(res.get(5).getSecondCity().getName().toLowerCase(), "farmington");
        assertEquals(res.get(5).getColor(), TrainCardColor.GREEN);
        assertEquals(res.get(5).getLength(), 3);

        assertEquals(res.get(6).getFirstCity().getName().toLowerCase(), "farmington");
        assertEquals(res.get(6).getSecondCity().getName().toLowerCase(), "kaysville");
        assertEquals(res.get(6).getColor(), TrainCardColor.PURPLE);
        assertEquals(res.get(6).getLength(), 2);

    }

    @Test
    public void getMarsDestinationCards() throws Exception {
        ArrayList<DestinationCard> res = new ArrayList<>();

        //Check if any destination cards were loaded
        try{
            res = new ArrayList<DestinationCard>(mapFactory.getMarsDestinationCards());
            assertNotSame(res.size(), 0);
        } catch (Exception e){
            System.err.println(e.getMessage());
            assertTrue(false);
        }

        //Check if correct destination cards were loaded
        DestinationCard testCard = res.get(0);
        assertEquals(testCard.getFirstCity().getName().toLowerCase(), "alpine");
        assertEquals(testCard.getSecondCity().getName().toLowerCase(), "salt lake city");
        assertEquals(testCard.getValue(), 10);

        testCard = res.get(1);
        assertEquals(testCard.getFirstCity().getName().toLowerCase(), "provo");
        assertEquals(testCard.getSecondCity().getName().toLowerCase(), "american fork");
        assertEquals(testCard.getValue(), 7);

        testCard = res.get(2);
        assertEquals(testCard.getFirstCity().getName().toLowerCase(), "saratoga springs");
        assertEquals(testCard.getSecondCity().getName().toLowerCase(), "kaysville");
        assertEquals(testCard.getValue(), 15);

        testCard = res.get(3);
        assertEquals(testCard.getFirstCity().getName().toLowerCase(), "american fork");
        assertEquals(testCard.getSecondCity().getName().toLowerCase(), "orem");
        assertEquals(testCard.getValue(), 6);

        testCard = res.get(4);
        assertEquals(testCard.getFirstCity().getName().toLowerCase(), "farmington");
        assertEquals(testCard.getSecondCity().getName().toLowerCase(), "provo");
        assertEquals(testCard.getValue(), 9);

    }




}