public class Node {
    int x, y, distance, path_cost; //Cost from traversal (so far)
    Node prev, next;
    public Node(int x, int y){
        this.x = x;
        this.y = y;
        distance = 0;
        path_cost = 0;
        prev = null;
        next = null;
    }
    public Node(int x, int y, int distance, int path_cost, Node prev){
        this.x = x;
        this.y = y;
        this.distance = distance;
        this.path_cost = path_cost;
        this.prev = prev;
    }
}

