import java.io.*;
import java.util.*;
public class homework {
    private static int [][] map;
    private static int [] goal;
    public static int [] start;
    public static int memory = 0;
    private static int expansion = 0;
    public static void main(String [] args)throws Exception{
        Reading("test_3.txt");
        implementation(map);
        System.out.println(" ");
        System.out.println("Program has ended.");
    }

    //Method Reading will read the file test and map it//
    private static void Reading(String file) throws IOException {
        //int [][] map;
        String current;
        int row = 0;
        FileReader reader = new FileReader(file);
        BufferedReader b_reader = new BufferedReader(reader);

        current = b_reader.readLine();
        String [] splitter = current.split(" ");
        map = new int [Integer.parseInt(splitter[0])] [Integer.parseInt(splitter[1])];

        current = b_reader.readLine();
        splitter = current.split(" ");
        start = new int[] {Integer.parseInt(splitter[0]), Integer.parseInt(splitter[1])};

        current = b_reader.readLine();
        splitter = current.split(" ");
        goal = new int[] {Integer.parseInt(splitter[0]), Integer.parseInt(splitter[1])};

        //GET THE REST OF THE MAP into map 2d array//
        while((current = b_reader.readLine()) != null){
            splitter = current.split(" ");
            for(int i = 0; i < splitter.length; i++){
                map [row][i] = Integer.parseInt(splitter[i]);
            }
            row++;
        }
    }
    private static int Manhattan(int x, int y, Node g_node){
        return(Math.abs(g_node.x - x) + Math.abs(g_node.y - y));
    }
    private static Node[] Succesor(Node current, Node g_node, boolean[][] storage, int[][] map){
        LinkedList<Node> children = new LinkedList<Node>();
        Node [] child = new Node[children.size()];
        //MOVE UP
        if((current.x -1 >= 0) && (map[current.x-1][current.y] != 0) && (!storage[current.x-1][current.y])){
            children.add(new Node(current.x-1, current.y,
                    Manhattan(current.x-1, current.y, g_node),
                    current.path_cost + map[current.x-1][current.y],current));
            storage[current.x-1][current.y] = true;
        }
        //MOVE DOWN
        if((current.x + 1 < map.length) && (map[current.x + 1][current.y] != 0) && (!storage[current.x + 1][current.y])){
            children.add(new Node(current.x + 1, current.y, Manhattan(current.x + 1, current.y, g_node),
                    current.path_cost + map[current.x + 1][current.y], current));
            storage[current.x + 1][current.y] = true;
        }
        //MOVE RIGHT
        if((current.y + 1 < map[0].length) && (map[current.x][current.y + 1] != 0) && (!storage[current.x][current.y + 1])){
            children.add(new Node(current.x, current.y + 1, Manhattan(current.x, current.y + 1, g_node),
                    current.path_cost+ map[current.x][current.y + 1], current));
            storage[current.x][current.y + 1] = true;
        }
        //MOVE LEFT
        if((current.y - 1 >= 0) && (map[current.x][current.y - 1] != 0) && (!storage[current.x][current.y - 1])){
            children.add(new Node(current.x, current.y - 1, Manhattan(current.x, current.y - 1, g_node),
                    current.path_cost + map[current.x][current.y - 1], current));
            storage[current.x][current.y - 1] = true;
        }
        return children.toArray(child);
    }
    private static boolean goal_state(Node current){
        if(current.distance == 0)
            return true;
        else
            return false;
    }
    public static void Container(boolean [][] storage){
        for(int i =0; i < storage.length; i++){
            for(int j=0; j< storage[i].length; j++){
                storage[i][j] = false;
            }
        }
    }
    // Algorithms //
    public static void implementation(int [][] map){
        bfs(map);
        System.out.println(" ");
        iterative(map);
        //A_Star(map);
    }
    private static void bfs(int [][] map){
        boolean [] [] storage = new boolean[map.length][map[0].length];
        int cost;
        long s_timer = System.currentTimeMillis();
        long e_timer = 180000;
        boolean buzzer = false;
        Node g_node = new Node(goal[0]-1, goal[1]-1);
        Node s_node = new Node(start[0]-1, start[1]-1,
                Manhattan(start[0]-1, start[1]-1,g_node),
                map[start[0]-1][start[1]-1], null);
        Queue<Node> q = new LinkedList<>();
        q.add(s_node);
        System.out.println("DFS: ");
        while(!(q.isEmpty())){
            if((System.currentTimeMillis() - s_timer) > e_timer) {
                System.out.println("Expired time");
                buzzer = true;
                break;
            }
            //Expanson//
            Node current = q.poll();
            storage[current.x][current.y] = true;
            memory ++;
            if (goal_state(current)){
                cost = current.path_cost;
                System.out.println("Cost"+cost);
                break;
            }
            Node [] children = Succesor(current, g_node, storage, map);
            for(int i =0; i<children.length; i++){ q.add(children[i]);}
            // Succesor to memory
            if(expansion < q.size()){
                expansion = q.size();
            }
        }
        results(buzzer, s_timer);
    }
    private static void iterative(int [][] map){
        long s_timer = System.currentTimeMillis();
        long e_timer = 180000;
        int total = 0, bound = 0, counter = 1, path_cost;
        boolean buzzer = false;
        Stack<Node> fringe = new Stack<Node>();
        Node [] children;
        boolean[][] storage = new boolean[map.length][map[0].length];
        Node g_node = new Node(goal[0]-1, goal[1]-1);
        Node s_node = new Node(start[0]-1, start[1]-1,
                Manhattan(start[0]-1, start[1]-1,g_node),
                map[start[0]-1][start[1]-1], null);
        System.out.println("IDS: ");
        for(bound = 0; bound < counter; bound++){
            fringe.push(s_node);
            Container(storage);
            expansion = 0;
            memory = 0;
            while(!fringe.isEmpty()){
                if((System.currentTimeMillis() - s_timer) > e_timer){
                    System.out.println("times up");
                    buzzer = true;
                    break;
                }
                Node current = fringe.pop();
                expansion ++;
                if(goal_state(current)){
                    path_cost = current.path_cost;
                    bound = counter + 1;
                    System.out.println("Cost: "+path_cost);
                    break;
                }
                if(total == bound){
                    total++;
                    fringe.removeAllElements();
                    break;
                }
                children = Succesor(current, g_node, storage,map);
                for(int j = 0 ; j < children.length;j++){fringe.push(children[j]);}
                if(memory < fringe.size()){memory = fringe.size();}
            }
            total ++;
            counter ++;
        }
        results(buzzer, s_timer);
    }

    //method results is what prints out into the terminal//
    public static void results(boolean buzzer, long s_timer){
        if(buzzer){
            System.out.println("Null cost");
            System.out.println("Memory: " +memory);
            System.out.println("Timer"+ (System.currentTimeMillis() - s_timer));
        }
        else{
            System.out.println("Expansion: " +expansion);
            System.out.println("Memory: " +memory);
            System.out.println("Timer: "+ (System.currentTimeMillis() - s_timer));
        }

    }
}
