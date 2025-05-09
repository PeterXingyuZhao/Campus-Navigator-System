import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Frontend implements FrontendInterface{

    private BackendInterface backend;

    /**
     * Implementing classes should support the constructor below.
     * @param backend is used for shortest path computations
     */
    public Frontend(BackendInterface backend) {
        this.backend = backend;
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="start", for the start location
     * - a text input field with the id="end", for the destination
     * - a button labelled "Find Shortest Path" to request this computation
     * Ensure that these text fields are clearly labelled, so that the user
     * can understand how to use them.
     *
     * @return an HTML string that contains input controls that the user can
     * make use of to request a shortest path computation
     */
    @Override
    public String generateShortestPathPromptHTML() {
        String html = """
                <div>
                    <!-- Label and input for the start location -->
                    <label for="start">Start Location:</label>
                    <input type="text" id="start" name="start" placeholder="Enter start location">
                    <br>
                    
                    <!-- Label and input for the destination location -->
                    <label for="end">Destination:</label>
                    <input type="text" id="end" name="end" placeholder="Enter destination">
                    <br>
                    
                    <!-- Button to request shortest path computation -->
                   <button>Find Shortest Path</button>
                </div>
                """;
        return html;
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the path's start and end locations
     * - an ordered list (ol) of locations along that shortest path
     * - a paragraph (p) that includes the total travel time along this path
     * Or if there is no such path, the HTML returned should instead indicate
     * the kind of problem encountered.
     *
     * @param start is the starting location to find a shortest path from
     * @param end   is the destination that this shortest path should end at
     * @return an HTML string that describes the shortest path between these
     * two locations
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        // Get the shortest path and travel times between start and end
        List<String> path = backend.findLocationsOnShortestPath(start, end);
        List<Double> times = backend.findTimesOnShortestPath(start, end);

        // If no path is found, return a message
        if (path.isEmpty()) {
            return "<p>No path found between " + start + " and " + end + ".</p>\n";
        }

        // Build HTML response with path description and ordered list of locations
        StringBuilder html = new StringBuilder();
        html.append("<p>Shortest path from ").append(start).append(" to ").append(end).append(":</p>\n");

        html.append("<ol>\n");
        for (String location : path) {
            html.append("\t<li>").append(location).append("</li>\n");
        }
        html.append("</ol>\n");

        // Calculate total travel time
        double totalTravelTime = 0;
        for (Double time : times) {
            totalTravelTime += time;
        }

        // Add total travel time to HTML
        html.append("<p>Total travel time: ").append(totalTravelTime).append(" seconds</p>\n");

        return html.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="from", for the start locations
     * - a button labelled "Closest From All" to submit this request
     * Ensure that this text field is clearly labelled, so that the user
     * can understand that they should enter a comma separated list of as
     * many locations as they would like into this field
     *
     * @return an HTML string that contains input controls that the user can
     * make use of to request a ten closest destinations calculation
     */
    @Override
    public String generateClosestDestinationsFromAllPromptHTML() {
        String html = """
            <div>
                <!-- Label and input for start locations -->
                <label for="from">Enter Start Locations (comma-separated):</label>
                <input type="text" id="from" name="from" placeholder="e.g., Location1, Location2, Location3">
                <br>

                <!-- Button to request closest destinations calculation -->
                <button>Closest From All</button>
            </div>
            """;
        return html;
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - an unordered list (ul) of the start Locations
     * - a paragraph (p) describing the destination that is reached most
     * quickly from all of those start locations (summing travel times)
     * - a paragraph that displays the total/summed travel time that it take
     * to reach this destination from all specified start locations
     * Or if no such destinations can be found, the HTML returned should
     * instead indicate the kind of problem encountered.
     *
     * @param starts is the comma separated list of starting locations to
     *               search from
     * @return an HTML string that describes the closest destinations from the
     * specified start location.
     */
    @Override
    public String generateClosestDestinationsFromAllResponseHTML(String starts) {
        // Split the comma-separated starts into a list of locations
        String[] startArray = starts.split(",");
        List<String> startLocations = new ArrayList<>();
        for (String location : startArray) {
            startLocations.add(location.trim());
        }

        // Try to find the closest destination from all start locations
        String closestDestination;
        double totalTravelTime = 0;

        try {
            // Get the closest destination
            closestDestination = backend.getClosestDestinationFromAll(startLocations);

            // Calculate total travel time by summing up travel times from each start location
            for (String startLocation : startLocations) {
                List<Double> times = backend.findTimesOnShortestPath(startLocation, closestDestination);
                // Sum the times
                for (Double time : times) {
                    totalTravelTime += time;
                }
            }
        } catch (NoSuchElementException e) {
            // Return an error message indicating that no valid destination was found
            return "<p>No destination could be reached from all specified start locations, or some start locations do not exist in the graph.</p>\n";
        }

        // Build HTML response
        StringBuilder html = new StringBuilder();

        // Add unordered list of start locations
        html.append("<p>Start Locations:</p>\n<ul>\n");
        for (String location : startLocations) {
            html.append("\t<li>").append(location).append("</li>\n");
        }
        html.append("</ul>\n");

        // Add the closest destination and total travel time to HTML
        html.append("<p>Closest destination: ").append(closestDestination).append("</p>\n");
        html.append("<p>Total travel time: ").append(totalTravelTime).append(" seconds</p>\n");

        return html.toString();
    }
}
