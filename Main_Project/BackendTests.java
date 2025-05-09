//////////////// FILE HEADER ///////////////////////////////////////////////////
//
// Title:    P209.RoleCode - BackendTests
// Course:   CS 400 Fall 2024
//
// Author:   Zaid Arkhagha
// Email:    zarkhagha@wisc.edu
// Lecturer: Florian Heimerl
//
///////////////////////////////////////////////////////////////////////////////
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is for testing my Backend.java functionality. The Graph_Placeholder will be utilized
 * to do so.
 */
public class BackendTests {
    
    /**
     * Loading graph data from a .dot file.
     * Check if an IOException is thrown for invalid file and if
     * the method processes valid files without issues.
     */
    @Test
    public void roleTest1() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        //Invalid file
        assertThrows(IOException.class, () -> backend.loadGraphData("invalid_file.txt"));

	//Valid file
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            fail("IOException should not have been thrown for a valid file.");
        }
    }

    /**
     * Checks the list of all locations and shortest path between two nodes.
     * Verifiy that the Backend.java retrieves all nodes and the shortest path correctly.
     */
    @Test
    public void roleTest2() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        //getListOfAllLocations method
        List<String> locations = backend.getListOfAllLocations();
        assertEquals(Arrays.asList("Union South", "Computer Sciences and Statistics", 
                "Atmospheric, Oceanic and Space Sciences"), locations);

        //findLocationsOnShortestPath method
        List<String> path = backend.findLocationsOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        assertEquals(Arrays.asList("Union South", "Computer Sciences and Statistics", 
                "Atmospheric, Oceanic and Space Sciences"), path);
    }

    /**
     * Test reachable locations within a certain travel time.
     * Verifies that the Backend.java throws the right exception when the start location
     * DNE, check reachable locations within a set travel time for known start.
     */
    @Test
    public void roleTest3() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        //getReachableFromWithin method, invalid start location
        assertThrows(NoSuchElementException.class, 
            () -> backend.getReachableFromWithin("Unknown Location", 300.0));

        //Reachable locations from "Union South" within a given travel time
        List<String> reachable = backend.getReachableFromWithin("Union South", 500.0);
        assertEquals(Arrays.asList("Union South", "Computer Sciences and Statistics", 
                "Atmospheric, Oceanic and Space Sciences"), reachable);
    }

}

