package com.floorcorn.tickettoride.model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by Michael on 3/2/2017.
 */
public class MapFactoryTest {
    private MapFactory mapFactory;

    @Before
    public void setup(){
        mapFactory = new MapFactory();
    }

    @Test
    public void testGetMarsRoutes() throws Exception {
        try{
            List<Route> res = mapFactory.getMarsRoutes();
            assertNotSame(res.size(), 0);
        } catch (Exception e){
            System.err.println("****ERR***MESSAGE****");
            System.err.println(e.getMessage());
            //e.printStackTrace();
            assertTrue(false);
        }
    }

}