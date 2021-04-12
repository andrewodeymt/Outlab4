import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.*;

public class GPSGraph {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filePath = args[0];
        DiGraph graph = createGraph(filePath);
        int[] shortestPath;
        Scanner scan = new Scanner(System.in);
        int menuChoice = 0;

        while (menuChoice != 2) {
            printMenu(graph.V());
            //if user chooses 1, get source and destination info
            menuChoice = scan.nextInt();
            if (menuChoice == 1) {
                int[] pathInfo = getPathInfo(graph.V());
                int source = pathInfo[0];
                int destination = pathInfo[1];

                //if pathInfo[0] is 0, the user entered an invalid value
                if (pathInfo[0] == 0) {
                    out.println("Invalid entry.\n");
                } else {
                    //find shortest path
                    long startTime = currentTimeMillis();
                    shortestPath = dijkstrasAlg(graph, source, destination);
                    long endTime = currentTimeMillis();
                    double time = (double) (endTime - startTime) / 1000;

                    output(shortestPath, time);
                }
            } else if (menuChoice != 2) {
                out.println("Invalid entry.\n");
            }
        }
        out.println("\nExited.");
    }

    /**
     * method to return the reverse of the path as an array
     *
     * @param graph
     * @param source
     * @param destination
     * @return
     */
    private static int[] dijkstrasAlg(DiGraph graph, int source, int destination) {
        //stores distance from src for each vertex
        int[] distanceTo = new int[graph.V() + 1];
        //stores the tail of the edge that connects to the index
        int[] edgeTo = new int[graph.V() + 1];

        //populate distanceTo with values outside scope
        for (int i = 0; i < distanceTo.length; i++) {
            distanceTo[i] = -1;
        }
        distanceTo[source] = 0;
        edgeTo[source] = -1;

        MinPriorityQueue<Vertex> pq = new MinPriorityQueue<>();
        pq.insert(new Vertex(source, 0));

        //find the shortest path
        while (!pq.isEmpty()) {
            //store current shortest path form source
            Vertex current = pq.remove();
            //store list of vertices the terminal vertex of current is connected to
            Iterable<Integer> heads = graph.adj(current.getVertex());

            //adds all possible paths branching from the terminal vertex to a min PQ
            for (int head : heads) {
                int tail = current.getVertex();
                //store the additional cost of adding an edge from tail to head
                int edgeDistance = graph.getEdgeCost(tail, head);

                //branch off of current vertex
                Vertex branch = relax(distanceTo, tail, head, edgeDistance);
                if (branch != null) {
                    edgeTo[head] = tail;
                    pq.decreaseKey(branch);
                }

            }
        }
        return returnShortestPath(distanceTo, edgeTo, destination);
    }

    /**
     * method to relax edges
     *
     * @param distanceTo
     * @param tail
     * @param head
     * @param edgeDistance
     * @return
     */
    private static Vertex relax(int[] distanceTo, int tail, int head, int edgeDistance) {
        //if there is no path to head, then this is the current shortest path to head
        if (distanceTo[head] == -1) {
            distanceTo[head] = distanceTo[tail] + edgeDistance;
            return new Vertex(head, distanceTo[head]);
        }
        //if path cost is less than current cost to reach head becomes the current shortest path
        else if (distanceTo[head] > distanceTo[tail] + edgeDistance) {
            distanceTo[head] = distanceTo[tail] + edgeDistance;
            return new Vertex(head, distanceTo[head]);
        }
        //the existing path to head is the shortest path
        else {
            return null;
        }
    }

    private static int[] returnShortestPath(int[] distanceTo, int[] edgeTo, int destination) {
        //stores the path in reverse
        int[] path = new int[3];
        path[1] = distanceTo[destination];
        //iterator follows the shortest path, starting at destination i is the first empty index of the path array
        int iterator = destination, i = 2;
        //insert vertices into path array in reverse order
        while (iterator != -1) {
            if (i == path.length) {
                path = doubleArraySize(path);
            }
            path[i] = iterator;
            iterator = edgeTo[iterator];
            i++;
        }
        //path[0] is the last filled index in the array
        path[0] = i;
        return path;
    }

    /**
     * Method to read in file and create graph
     *
     * @param filePath
     * @return
     */
    private static DiGraph createGraph(String filePath) {
        DiGraph graph = null;

        try {
            Scanner fin = new Scanner(new File(filePath));

            String[] line;
            //finds the line that defines the number of nodes and edges
            do {
                String input = fin.nextLine();
                line = input.split(" ");
            } while (!line[0].equals("p"));

            //create correctly sized graph
            int v = Integer.parseInt(line[2]);
            graph = new DiGraph(v);

            //populates graph with edges
            while (fin.hasNextLine()) {
                String input = fin.nextLine();
                line = input.split(" ");
                //all inputs can be converted to integer values
                if (line[0].equals("a")) {
                    int tail = Integer.parseInt(line[1]);
                    int head = Integer.parseInt(line[2]);
                    int cost = Integer.parseInt(line[3]);
                    graph.addEdge(tail, head, cost);
                }
            }
            fin.close();
        } catch (FileNotFoundException e) {
            err.println("Opening file error");
        }
        return graph;
    }

    /**
     * method to get path information from user
     * returns the array {0, 0} if user enters invalid entry
     *
     * @param v
     * @return
     */
    private static int[] getPathInfo(int v) {
        Scanner scan = new Scanner(in);

        out.print("Source: ");
        int source = scan.nextInt();

        // ensures src is a valid entry
        if (source < 1 || source > v) {
            return new int[]{0, 0};
        }

        out.print("Destination: ");
        int destination = scan.nextInt();

        //destination is a valid entry
        if (destination < 1 || destination > v) {
            return new int[]{0, 0};

        }
        return new int[]{source, destination};
    }

    private static int[] doubleArraySize(int[] arr) {
        int[] temp = new int[2 * arr.length];
        for (int i = 0; i < arr.length; i++) {
            temp[i] = arr[i];
        }
        return temp;
    }

    /**
     * Method to print menu
     *
     * @param max
     */
    private static void printMenu(int max) {
        out.println(String.format("The current graph has vertices from 1 to %d.\n"
                + "Would you like to:\n1. Find a new route\n2. Exit", max));
    }

    /**
     * method to output path information
     *
     * @param path
     * @param start
     * @param end
     */
    private static void output(int[] path, double time) {
        int length = path[0];
        int cost = path[1];
        int destination = path[2];
        int source = path[length - 1];

        out.println(String.format("\nResults -- Shortest path from %d to %d", source, destination));
        String output = "Path: ";
        for (int i = length - 1; i > 1; i--) {
            output += Integer.toString(path[i]);
            if (i != 2) {
                output += "->";
            }
        }
        out.println(output);
        out.println(String.format("\nTotal cost: %d", cost));
        out.println(String.format("\nTotal time: %.3f sec\n", time));
    }
}
