package fifteenpuzzle;

import java.util.*;

public class boardGraph {
    public Node current;
    public Map<int[][], Node> visited;
    public PriorityQueue<Node> pq;
    public int blankRow, blankCol;


    public boardGraph(Node board){
        this.current=board;
        this.visited=new HashMap<>();
        this.pq= new PriorityQueue<>();
        // this.blankRow = board.getBlankPosition(board.getBoard())[0];
        // this.blankCol = board.getBlankPosition(board.getBoard())[1];
    }

    public void buildGraph() {
        pq.add(current);
        visited.put(current.getBoard(), current);
        while (!pq.isEmpty()){
            Node node = pq.poll();
            List<Node> neighbours = generateNeighbours(node);
            for (Node neighbour : neighbours){
                //int tentativeG = current.getGScore() + 1;
                //if (tentativeG < neighbour.getGScore()){

                //}
                if (!visited.containsKey(neighbour.getBoard())){
                    pq.add(neighbour);
                    visited.put(neighbour.getBoard(), neighbour);
                }
                node.addNeighbor(neighbour,1);
            }
        }
    }

    public static List<Node> generateNeighbours(Node node){
        List<Node> neighbours= new ArrayList<>();
        int[][] board = node.getBoard();
        int emptyRow=0;
        int emptyCol=0;
        for (int i = 0; i < node.getSize(); i++) {
            for (int j = 0; j < node.getSize(); j++) {
                if (board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break;
                }
            }
        }
        if (emptyRow>0){
            int [][] otherState = applyMove(board, emptyRow, emptyCol, emptyRow-1, emptyCol);
            Node other = new Node(otherState);
            neighbours.add(other);
        }
        if (emptyRow < node.getSize() -1) {
            int [][] otherState = applyMove(board, emptyRow, emptyCol, emptyRow+1, emptyCol);
            Node other = new Node(otherState);
            neighbours.add(other);
        }
        if (emptyCol>0){
            int [][] otherState = applyMove(board, emptyRow, emptyCol, emptyRow, emptyCol-1);
            Node other = new Node(otherState);
            neighbours.add(other);
        }
        if (emptyCol < node.getSize()-1){
            int [][] otherState = applyMove(board, emptyRow, emptyCol, emptyRow, emptyCol+1);
            Node other = new Node(otherState);
            neighbours.add(other);
        }
        return neighbours;
    }

    public static int[][] applyMove(int[][] board, int row1, int col1, int row2, int col2){
        int [][] newBoard = new int[board.length][board.length];
        for (int i = 0; i<board.length; i++){
            for (int j = 0; j<board.length; j++){
                newBoard[i][j] = board[i][j];
            }
        }
        newBoard[row1][col1] = board[row2][col2];
        newBoard[row2][col2] = board[row1][col1];
        return newBoard;
    }

    public Map<int[][] , Node> getVisited() {
        return visited;
    }

    public void printNeighbors() {
        List<Node> neighbors = generateNeighbours(current);
        System.out.println("Neighbors of current node:");
        for (Node neighbor : neighbors) {
            System.out.println(Arrays.deepToString(neighbor.getBoard()));
        
        }
    }
    

    public static List<String> aStar (Node start, Node end){
        Map<Node, Integer> g = new HashMap<>();
        g.put(start, 0);
        Map<Node, Integer> f = new HashMap<>();
        f.put(start, start.getHeur());
    
        PriorityQueue<Node> pQ = new PriorityQueue<>();
        pQ.add(start);
        Set<Node> closed = new HashSet<>();
    
        Map<Node, Node> pastNodes = new HashMap<>();
    
        while (!pQ.isEmpty()){
            Node current = pQ.poll();
            /* The commented code below prints each board state that the priority queue calls */
            // System.out.println("New board:");
            // for (int i =0; i<Node.size; i++){
            //     System.out.print('\n');
            //     for (int j = 0; j<Node.size; j++){
            //         System.out.print(current.getBoard()[i][j]);
            //         System.out.print(' ');
            //     }
            // }
            // System.out.println();
            if (current.equals(end)){
                System.out.println("Found goal");
                return createPath(pastNodes, current);
            }
            closed.add(current);   
    
            for (Node neighbour : generateNeighbours(current)){
                if (closed.contains(neighbour)){
                    continue;
                }
                int tentativeG = g.get(current)+1;
                if (!pQ.contains(neighbour) || tentativeG < g.get(neighbour)){ 
                    pastNodes.put(neighbour, current);
                    g.put(neighbour, tentativeG);
                    f.put(neighbour, tentativeG + neighbour.getHeur());
                    if (!pQ.contains(neighbour)){
                        pQ.add(neighbour);
                    } else {
                        pQ.remove(neighbour);
                        pQ.add(neighbour);
                    }
                }
            }
            //Ensure only neighbouring nodes are being accessed next
            List<Node> neighbours = generateNeighbours(current);
            pQ.removeIf(n -> !neighbours.contains(n));
            if (pQ.isEmpty()){
                for (Node n : neighbours){
                    pQ.add(n);
                }
            }
        }
        return null;
    }

  
    
    
    

    public static List<String> createPath(Map <Node, Node> pastNodes, Node current){
        List<String> path = new ArrayList<String>();
        Node node = current;
        while (pastNodes.containsKey(node)){
            Node prev = pastNodes.get(node);
            int[] prevEmpty = prev.getBlankPosition(prev.getBoard());
            int[] currEmpty = node.getBlankPosition(node.getBoard());
            int[] diff = new int[] { currEmpty[0] - prevEmpty[0], currEmpty[1] - prevEmpty[1] };
            String direction;
            if (diff[0] == 1 && diff[1] == 0){
                direction = "U";
            }
            else if (diff[0] == -1 && diff[1]==0){
                direction = "D";
            }
            else if (diff[0] == 0 && diff[1]==1){
                direction = "L";
            }
            else {
                direction = "R";
            }
            int movingTile = prev.getBoard()[currEmpty[0]][currEmpty[1]];
            path.add(movingTile-32 + ' ' + direction);
            node = prev;
        }
        Collections.reverse(path);
        return path;
    }
}

