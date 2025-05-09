import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class FrontendTests {

    // test the generateShortestPathPromptHTML() and the generateShortestPathResponseHTML() methods of the frontend class
    @Test
    public void roleTest1() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
        String output1 = frontend.generateShortestPathPromptHTML();
        Assertions.assertTrue(output1.contains("<div><label for=\"start\">Start Location:</label><input type=\"text\" id=\"start\" name=\"start\"><br>"));
        String output2 = frontend.generateShortestPathResponseHTML("Union South", "Atmospheric, Oceanic and Space Sciences");
        System.out.println(output2);
        Assertions.assertTrue(output2.contains("<ol><li>Union South</li><li>Computer Sciences and Statistics</li><li>Atmospheric, Oceanic and Space Sciences</li></ol>")); // <ol><li>Union South</li><li>Computer Sciences and Statistics</li><li>Atmospheric, Oceanic and Space Sciences</li></ol>
        Assertions.assertTrue(output2.contains("<p>Total travel time: 6.0 seconds.</p>"));
    }

    // test the generateReachableFromWithinPromptHTML() method of the frontend class
    @Test
    public void roleTest2() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
        String output = frontend.generateReachableFromWithinPromptHTML();
        Assertions.assertTrue(output.contains("<label for=\"time\">Max Travel Time (in minutes):</label><input type=\"text\" id=\"time\" name=\"time\">")); // <label for="time">Max Travel Time (in minutes):</label><input type="text" id="time" name="time">
    }

    // test the generateReachableFromWithinResponseHTML() method of the frontend class
    @Test
    public void roleTest3() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
        String output = frontend.generateReachableFromWithinResponseHTML("Union South", 8);
        Assertions.assertTrue(output.contains("<ul><li>Union South</li><li>Computer Sciences and Statistics</li><li>Atmospheric, Oceanic and Space Sciences</li></ul>")); // <ul><li>Union South</li><li>Computer Sciences and Statistics</li><li>Atmospheric, Oceanic and Space Sciences</li></ul> 
    }

    /**
     * Test both the fronend and backend through finding out the shortest path between "Bascom Hall" and "Van Hise Hall".
     */
    @Test
    public void IntegrationTest1() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        Backend<String> backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            String output = frontend.generateShortestPathResponseHTML("Bascom Hall", "Van Hise Hall");
            Assertions.assertTrue(output.contains("<p>Total travel time: 274.9 seconds.</p>")); // test the total travel time
        } catch (IOException e) {
            e.printStackTrace(); // or handle it as appropriate
            Assertions.fail("IOException was thrown: " + e.getMessage());
        }
    }

    /**
     * Test both the fronend and backend through finding out the shortest path between "Noland Hall" and "Van Hise Hall".
     */
    @Test
    public void IntegrationTest2() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        Backend<String> backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            String output = frontend.generateShortestPathResponseHTML("Noland Hall", "Van Hise Hall");
            // test all the locations on the shortest path
            Assertions.assertTrue(output.contains("<ol><li>Noland Hall</li><li>Brogden Psychology</li><li>The Crossing</li><li>Service Memorial Institute</li><li>Bardeen Medical Laboratories</li><li>Van Hise Hall</li></ol>"));
        } catch (IOException e) {
            e.printStackTrace(); // or handle it as appropriate
            Assertions.fail("IOException was thrown: " + e.getMessage());
        }
    }

    /**
     * Test both the fronend and backend through finding out all the locations within 500s from X01.
     */
    @Test
    public void IntegrationTest3() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        Backend<String> backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            String output = frontend.generateReachableFromWithinResponseHTML("X01", 500);
            // test some of the reachable locations
            Assertions.assertTrue(output.contains("<li>Ogg Residence Hall</li><li>Van Vleck Hall</li><li>Sterling Hall</li><li>Chadbourne Residence Hall</li>"));
        } catch (IOException e) {
            e.printStackTrace(); // or handle it as appropriate
            Assertions.fail("IOException was thrown: " + e.getMessage());
        }
    }

    /**
     * Test both the fronend and backend through finding out all the locations within 300s from Van Hise Hall.
     */
    @Test
    public void IntegrationTest4() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        Backend<String> backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            String output = frontend.generateReachableFromWithinResponseHTML("Van Hise Hall", 300);
            // test some of the reachable locations
            Assertions.assertTrue(output.contains("<li>Medical Sciences</li><li>Bascom Hall</li><li>Nutritional Sciences</li><li>Van Vleck Hall</li><li>Bardeen Medical Laboratories</li>"));
        } catch (IOException e) {
            e.printStackTrace(); // or handle it as appropriate
            Assertions.fail("IOException was thrown: " + e.getMessage());
        }
    }
}