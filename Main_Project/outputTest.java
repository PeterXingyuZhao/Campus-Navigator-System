import java.io.IOException;

public class outputTest {
    public static void main(String[] args) {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        Backend<String> backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
            Frontend frontend = new Frontend(backend);
            String output = frontend.generateReachableFromWithinResponseHTML("Van Hise Hall", 300);
            System.out.println(output);
        } catch(IOException e) {

        }
    }
}
