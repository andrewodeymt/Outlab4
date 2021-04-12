import java.util.Iterator;
import java.util.NoSuchElementException;

public class DiGraph {
    private int V = 0;
    private int E = 0;
    private int[][] adjacencyMatrix;

    /**
     * constructor that creates an appropriately sized adjacency matrix
     *
     * @param size
     */
    DiGraph(int size) {
        V = size;
        adjacencyMatrix = new int[size + 1][size + 1];
    }

    /**
     * method to add edge to a graph
     *
     * @param tail
     * @param head
     * @param weight
     */
    public void addEdge(int tail, int head, int cost) {
        adjacencyMatrix[tail][head] = cost;
        E++;
    }

    /**
     * method to return a list of adjacent vertices to v
     *
     * @param v
     * @return
     */
    public Iterable<Integer> adj(int v) {
        return new AdjIterator(v);
    }

    private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {
        private int v;
        private int w = 0;

        AdjIterator(int v) {
            this.v = v;
        }

        @Override
        public Iterator<Integer> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            while (w < V + 1) {
                if (adjacencyMatrix[v][w] != 0) return true;
                w++;
            }
            return false;
        }

        /**
         * method to return the index of the head of an edge whose tail is at v
         */
        @Override
        public Integer next() {
            if (hasNext()) return w++;
            else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * method to return the cost of a specific edge
     *
     * @param tail
     * @param head
     * @return
     */
    public int getEdgeCost(int tail, int head) {
        return adjacencyMatrix[tail][head];
    }

    /**
     * method to return number of vertices in the graph
     */
    public int V() {
        return this.V;
    }

    /**
     * method to return the number of edges in the graph
     */
    public int E() {
        return this.E;
    }
}
