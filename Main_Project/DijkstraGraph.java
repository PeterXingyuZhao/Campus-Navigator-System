// === CS400 File Header Information ===
// Name: Xingyu Zhao
// Email: xzhao468@wisc.edu
// Group and Team: P2.3915
// Lecturer: Florian
// Notes to Grader: <optional extra notes>

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        @Override
        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashtableMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // implement in step 5.3
        // if either start or end node is not in the graph, throw an exception
        if(!nodes.containsKey(start)) {
            throw new NoSuchElementException("key " + start.toString() + " not in map");
        }
        if(!nodes.containsKey(end)) {
            throw new NoSuchElementException("key " + end.toString() + " not in map");
        }
        //initialize the min heap and put the initial "empty" edge in it
        PriorityQueue<SearchNode> edgeHeap = new PriorityQueue<>();
        SearchNode startSearchNode = new SearchNode(nodes.get(start), 0, null);
        edgeHeap.add(startSearchNode);

        HashtableMap<NodeType, SearchNode> visitedMap = new HashtableMap<>();

        while(!edgeHeap.isEmpty()) {
            SearchNode topNode = edgeHeap.remove();
            if(visitedMap.containsKey(topNode.node.data)) {
                continue;
            }
            visitedMap.put(topNode.node.data, topNode);
            // if the edge leads to an unvisited node, put all the outgoing edges of that node that again lead to unvisited nodes into the heap
            for(Edge edge: topNode.node.edgesLeaving) {
                if(!visitedMap.containsKey(edge.successor.data)) {
                    SearchNode newEdge = new SearchNode(edge.successor, topNode.cost + edge.data.doubleValue(), topNode);
                    edgeHeap.add(newEdge);
                }
            }
        }
        // if the start node has no path that leads to the end node, throw an exception
        if(!visitedMap.containsKey(end)) {
            throw new NoSuchElementException("There is no directed path from " + start + " to " + end);
        }
        return visitedMap.get(end);
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    @Override
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // implement in step 5.4
        List<NodeType> path = new ArrayList<>();
        SearchNode endSearchNode = computeShortestPath(start, end);
        while(endSearchNode.predecessor != null) {
            path.add(endSearchNode.node.data);
            endSearchNode = endSearchNode.predecessor;
        }
        path.add(start);
        // the current list is in reverse order, we have to reverse it
        Collections.reverse(path);
        return path;
	}

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    @Override
    public double shortestPathCost(NodeType start, NodeType end) {
        // implement in step 5.4
        return computeShortestPath(start, end).cost;
    }

    // TODO: implement 3+ tests in step 4.1
    /**
     * Test1: Verify that the shortest path and cost from node "A" and node "H" are computed correctly using Dijkstra's algorithm.
     */

    // public static void main(String[] args) {
    //     DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    //     graph.insertNode("A");
    //     graph.insertNode("B");
    //     graph.insertNode("C");
    //     graph.insertNode("D");
    //     graph.insertNode("E");
    //     graph.insertNode("F");
    //     graph.insertNode("G");
    //     graph.insertNode("H");

    //     graph.insertEdge("A", "B", 4);
    //     graph.insertEdge("A", "E", 15);
    //     graph.insertEdge("B", "E", 10);
    //     graph.insertEdge("A", "C", 2);
    //     graph.insertEdge("B", "D", 1);
    //     graph.insertEdge("C", "D", 5);
    //     graph.insertEdge("D", "E", 3);
    //     graph.insertEdge("D", "F", 0);
    //     graph.insertEdge("F", "D", 2);
    //     graph.insertEdge("F", "H", 4);
    //     graph.insertEdge("G", "H", 4);

    //     System.out.println(graph.computeShortestPath("B", "F").predecessor.node.data);
    // }
    // @Test
    // public void Test1() {
    //     DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    //     graph.insertNode("A");
    //     graph.insertNode("B");
    //     graph.insertNode("C");
    //     graph.insertNode("D");
    //     graph.insertNode("E");
    //     graph.insertNode("F");
    //     graph.insertNode("G");
    //     graph.insertNode("H");

    //     graph.insertEdge("A", "B", 4);
    //     graph.insertEdge("A", "E", 15);
    //     graph.insertEdge("B", "E", 10);
    //     graph.insertEdge("A", "C", 2);
    //     graph.insertEdge("B", "D", 1);
    //     graph.insertEdge("C", "D", 5);
    //     graph.insertEdge("D", "E", 3);
    //     graph.insertEdge("D", "F", 0);
    //     graph.insertEdge("F", "D", 2);
    //     graph.insertEdge("F", "H", 4);
    //     graph.insertEdge("G", "H", 4);

    //     List<String> returnedData = graph.shortestPathData("A", "H");
    //     Double returnedCost = graph.shortestPathCost("A", "H");

    //     List<String> expectedData = List.of("A", "B", "D", "F", "H");
    //     Double expectedCost = 9.0;
    //     Assertions.assertEquals(expectedData, returnedData);
    //     Assertions.assertEquals(expectedCost, returnedCost);
    // }

    // /**
    //  * Test2: Verifies the correctness of the shortest path and cost from "A" to "E".
    //  */
    // @Test
    // public void Test2() {
    //     DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    //     graph.insertNode("A");
    //     graph.insertNode("B");
    //     graph.insertNode("C");
    //     graph.insertNode("D");
    //     graph.insertNode("E");
    //     graph.insertNode("F");
    //     graph.insertNode("G");
    //     graph.insertNode("H");

    //     graph.insertEdge("A", "B", 4);
    //     graph.insertEdge("A", "E", 15);
    //     graph.insertEdge("B", "E", 10);
    //     graph.insertEdge("A", "C", 2);
    //     graph.insertEdge("B", "D", 1);
    //     graph.insertEdge("C", "D", 5);
    //     graph.insertEdge("D", "E", 3);
    //     graph.insertEdge("D", "F", 0);
    //     graph.insertEdge("F", "D", 2);
    //     graph.insertEdge("F", "H", 4);
    //     graph.insertEdge("G", "H", 4);

    //     List<String> returnedData = graph.shortestPathData("A", "E");
    //     Double returnedCost = graph.shortestPathCost("A", "E");

    //     List<String> expectedData = List.of("A", "B", "D", "E");
    //     Double expectedCost = 8.0;
    //     Assertions.assertEquals(expectedData, returnedData);
    //     Assertions.assertEquals(expectedCost, returnedCost);
    // }

    // /**
    //  * Test3: Verifies attempting to find a path between two unconnected nodes correctly throws a NoSuchElementException.
    //  */
    // @Test
    // public void Test3() {
    //     DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    //     graph.insertNode("A");
    //     graph.insertNode("B");
    //     graph.insertNode("C");
    //     graph.insertNode("D");
    //     graph.insertNode("E");
    //     graph.insertNode("F");
    //     graph.insertNode("G");
    //     graph.insertNode("H");

    //     graph.insertEdge("A", "B", 4);
    //     graph.insertEdge("A", "E", 15);
    //     graph.insertEdge("B", "E", 10);
    //     graph.insertEdge("A", "C", 2);
    //     graph.insertEdge("B", "D", 1);
    //     graph.insertEdge("C", "D", 5);
    //     graph.insertEdge("D", "E", 3);
    //     graph.insertEdge("D", "F", 0);
    //     graph.insertEdge("F", "D", 2);
    //     graph.insertEdge("F", "H", 4);
    //     graph.insertEdge("G", "H", 4);

    //     Assertions.assertThrows(NoSuchElementException.class, () -> {graph.shortestPathData("C", "B");});
    // }
}
