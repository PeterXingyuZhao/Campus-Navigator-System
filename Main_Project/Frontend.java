import java.util.*;

public class Frontend implements FrontendInterface {

    private BackendInterface backend;

    public Frontend (BackendInterface backend) {
        this.backend = backend;
    }

    private Double totalCost(List<Double> timesList) {
        Double sum = 0.0;
        for(int i = 0; i < timesList.size(); i++) {
            sum += timesList.get(i);
        }
        return sum;
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page. 
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a shortest path computation
     */
    @Override
    public String generateShortestPathPromptHTML() {
        StringBuilder promptHTML = new StringBuilder();
        promptHTML.append("<div>");
        promptHTML.append("<label for=\"start\">Start Location:</label>");
        promptHTML.append("<input type=\"text\" id=\"start\" name=\"start\"><br>"); // a text input field with the id="start", for the start location
        promptHTML.append("<label for=\"end\">End Location:</label>"); // a text input field with the id="end", for the destination
        promptHTML.append("<input type=\"text\" id=\"end\" name=\"end\"><br>");
        promptHTML.append("<button>Find Shortest Path</button><br>"); // a button labelled "Find Shortest Path" to request this computation
        promptHTML.append("</div>");
        return promptHTML.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  
     * @param start is the starting location to find a shortest path from
     * @param end is the destination that this shortest path should end at
     * @return an HTML string that describes the shortest path between these
     *         two locations
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        StringBuilder responseHTML = new StringBuilder();
        try {
            List<String> locationsOnPath = backend.findLocationsOnShortestPath(start, end);
            if(locationsOnPath.isEmpty()) {
                return "<p>No path found between " + start + " and " + end + ".</p>";
            }

            List<Double> timesOnPath = backend.findTimesOnShortestPath(start, end);

            responseHTML.append("<p>Shortest path from " + start + " to " + end + ":</p>");// a paragraph (p) that describes the path's start and end locations
            responseHTML.append("<ol>"); // an ordered list (ol) of locations along that shortest path
            for(int i = 0; i < locationsOnPath.size(); i++) {
                responseHTML.append("<li>" + locationsOnPath.get(i) + "</li>");
            }
            responseHTML.append("</ol>");

            responseHTML.append("<p>Total travel time: " + totalCost(timesOnPath) + " seconds.</p>");// a paragraph (p) that includes the total travel time along this path
            return responseHTML.toString();
        } catch (NoSuchElementException e) {
            return "<p>No path found between " + start + " and " + end + ".</p>"; // if there is no such path, the HTML returned indicates the kind of problem encountered.
        }
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page. 
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a ten closest destinations calculation
     */
    @Override
    public String generateReachableFromWithinPromptHTML() {
        StringBuilder promptHTML = new StringBuilder();
        promptHTML.append("<div>");
        promptHTML.append("<label for=\"from\">Start Location:</label>");// a text input field with the id="from", for the start locations
        promptHTML.append("<input type=\"text\" id=\"from\" name=\"from\"><br>");
        promptHTML.append("<label for=\"time\">Max Travel Time (in seconds):</label>");
        promptHTML.append("<input type=\"text\" id=\"time\" name=\"time\"><br>");// a text input field with the id="time", for the max time limit
        promptHTML.append("<button>Find Reachable Destinations</button><br>"); // a button labelled "Reachable From Within" to submit this request
        promptHTML.append("</div>");
        return promptHTML.toString();
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.
     * @param start is the starting location to search from
     * @param travelTime is the maximum number of seconds away from the start
     *        that will allow a destination to be reported
     * @return an HTML string that describes the closest destinations from the
     *         specified start location.
     */ 
    @Override
    public String generateReachableFromWithinResponseHTML(String start, double travelTime) {
        try {
            StringBuilder responseHTML = new StringBuilder();
            List<String> reachableDestinations = backend.getReachableFromWithin(start, travelTime);
            if(reachableDestinations.isEmpty()) {
                return "<p>No destinations reachable from " + start + " within " + travelTime + " minutes.</p>";
            }
            responseHTML.append("<p>Destinations reachable from " + start + " within " + travelTime + " minutes: </p>");
            responseHTML.append("<ul>");// an unordered list (ul) of destinations that can be reached within that allowed travel time
            for(int i = 0; i < reachableDestinations.size(); i++) {
                responseHTML.append("<li>" + reachableDestinations.get(i) + "</li>");
            }
            responseHTML.append("</ul>");
            return responseHTML.toString();
        } catch (NoSuchElementException e) {
            return "<p>Start location does not exist.</p>";
        }
    }
}
