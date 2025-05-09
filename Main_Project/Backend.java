//////////////// FILE HEADER ///////////////////////////////////////////////////
//
// Title:    P209.RoleCode - Backend
// Course:   CS 400 Fall 2024
//
// Author:   Zaid Arkhagha
// Email:    zarkhagha@wisc.edu
// Lecturer: Florian Heimerl
//
///////////////////////////////////////////////////////////////////////////////
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Backend<T> implements BackendInterface {
    // Backend(GraphADT<String,Double> graph);
    // "Sconnie Bar" -> "Hotel Red" [seconds=180.7]; -- Example nodes + edge and time

    // GraphADT private field
    private final GraphADT<T, Double> graph;

    // Constructor
    public Backend(GraphADT<T, Double> graph) {
        this.graph = graph;
        // simply to store new graph info
    }

    // NOW THEN, METHODS!

    /**
     * Loads graph data from a dot file. If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     * 
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    @Override
    public void loadGraphData(String filename) throws IOException {
        if (!filename.endsWith(".dot")) {
            throw new IOException("File cannot end in an extension other than .dot");
            // ^Must be .dot file extension
        }
        // try to read the file
        File file = new File(filename);
        if (!file.exists()) {
            throw new IOException("File cannot be found");
            // ^File not found
        }
        // lets clear the graph before reading another in. Iterate through the nodes and delete'em
        for (T node : graph.getAllNodes()) {
        	graph.removeNode(node);
        }

        // Not a huge fan of using BufferedReader compared to other ways, but
        // functionally works fine to process the .dot file and create the graph
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Removing semicolon and trimming the line
                line = line.replace(";", "").trim();
                String[] parts = line.split(" -> ");
                if (parts.length >= 2) {
                    String[] edgeAndWeight = parts[1].split("\\[seconds=");
                    T node1 = (T) parts[0].replace("\"", "").trim();
                    T node2 = (T) edgeAndWeight[0].replace("\"", "").trim();
                    double weight = Double.parseDouble(edgeAndWeight[1].replace("]", "").trim());
                    // now node and edge inserting, using all the data we gather
                    // above from the .dot file
                    graph.insertNode(node1);
                    graph.insertNode(node2);
                    graph.insertEdge(node1, node2, weight);
                    graph.insertEdge(node2, node1, weight);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of all locations (node data) available in the graph.
     * 
     * @return list of all location names
     */
    public List<String> getListOfAllLocations() {

        // initialize the string list of locations
        List<String> allLocations = new ArrayList<>();
        for (T node : graph.getAllNodes()) {
            allLocations.add(node.toString());
        }
        return allLocations;
    }

    /**
     * Return the sequence of locations along the shortest path from startLocation
     * to endLocation, or an empty list if no such path exists.
     * 
     * @param startLocation the start location of the path
     * @param endLocation   the end location of the path
     * @return a list with the nodes along the shortest path from startLocation to
     *         endLocation, or an empty list if no such path exists
     */
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {

        // This part may be redundant, i'll add it in case, check for null inputs, but no exception toss
        // since its unspecified...
        if (!(graph.containsNode((T) startLocation) && graph.containsNode((T) endLocation))) {
            return new ArrayList<>();
        }

        List<T> NodeDataOnPath = graph.shortestPathData((T) startLocation, (T) endLocation);
        if (NodeDataOnPath == null) {
            return new ArrayList<>();
            // path DNE
        }

        List<String> NodeDataString = new ArrayList<>();
        for (T node : NodeDataOnPath) {
            NodeDataString.add(node.toString());
        }
        return NodeDataString;
        // Unless I'm missing something majorly important,
        // than this method just needs use shortestPathData function alongside some
        // if statements and check for any null inputs, then return the gathered list of location
    }

    /**
     * Return the walking times in seconds between each two nodes on the shortest
     * path from startLocation to endLocation, or an empty list if no such path
     * exists.
     * 
     * @param startLocation the start location of the path
     * @param endLocation   the end location of the path
     * @return a list with the walking times in seconds between two nodes along the
     *         shortest path from startLocation to endLocation, or an empty list if
     *         no such path exists
     */
    public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {

        // This part may be redundant, i'll add it in case, check for null inputs, but no exception toss
        // since its unspecified...
        if (!(graph.containsNode((T) startLocation) && graph.containsNode((T) endLocation))) {
            return new ArrayList<>();
        }

        // now store the path
        List<String> path = findLocationsOnShortestPath(startLocation, endLocation);

        // check if that path is empty
        if (path.isEmpty()) {
            return new ArrayList<>();
        }

        // Now we are going to want to make a list of the times in the path
        // index through every edge size and find the largest, we use a for loop to do so
        List<Double> travelTimes = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            T from = (T) path.get(i);
            T to = (T) path.get(i + 1);
            travelTimes.add(graph.getEdge(from, to));
        }
        return travelTimes;// This should be all the times for travel times between nodes
    }

    /**
     * Returns the list of locations that can be reached when starting from the
     * provided startLocation, and travelling a maximum of travelTime seconds.
     * 
     * @param startLocation the location to find the reachable locations from
     * @param travelTime    is the maximum number of seconds away the start location
     *                      that a destination must be in order to be returned
     * @return the list of destinations that can be reached from startLocation in
     *         travelTime seconds or less
     * @throws NoSuchElementException if startLocation does not exist
     */
    public List<String> getReachableFromWithin(String startLocation, double travelTime) throws NoSuchElementException {

        // gotta check that the start node even exists, if it dont, toss the exception!
        if (!graph.containsNode((T) startLocation)) {
            throw new NoSuchElementException("Start location DNE!");
        }

        // New string array to store the reachable destinations
        List<String> withinReachLocations = new ArrayList<>();

        // For loop, go through all nodes
        for (T location : graph.getAllNodes()) {
            // whats the cost/weight of the edge to the node? Store and compare if it's less
            // than the travelTime variable!
            double cost = graph.shortestPathCost((T) startLocation, location);
            if (cost <= travelTime) {
                withinReachLocations.add(location.toString());
                // add it to the list if it has passed so far
            }
        }
        // assuming all went well return this list
        return withinReachLocations;

    }
}
