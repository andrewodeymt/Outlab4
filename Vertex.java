public class Vertex implements Comparable<Vertex> {
    private final int vertex;
    private int distanceTo;

    /**
     * constructor to initialize global variables
     *
     * @param vertex
     * @param distanceTo
     */
    Vertex(int vertex, int distanceTo) {
        this.vertex = vertex;
        this.distanceTo = distanceTo;
    }

    /**
     * method to compare distance of vertices from a source
     *
     * @param object
     * @return
     */
    @Override
    public int compareTo(Vertex object) {
        if (this.distanceTo > object.getDistanceTo()) {
            return 1;
        } else if (this.distanceTo == object.getDistanceTo()) {
            return 0;
        } else {
            return -1;
        }
    }

    public void setDistTo(int distance) {
        distanceTo = distance;
    }

    public int getVertex() {
        return vertex;
    }

    public int getDistanceTo() {
        return distanceTo;
    }
}
