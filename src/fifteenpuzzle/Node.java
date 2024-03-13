package fifteenpuzzle;
import java.util.*;

public class Node implements Comparable<Node>{
	
    static int size;
    int[][] board;
	List<Node> neighbours;
	public int heuristic;
	public static int blankD;
	public int blank;
	// public static boolean isBig = false;
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;
	Map<Node, Integer> edges;
	public static int rowSize, colSize;
	private int G;
	public static int[][] goal;
	public Node(int[][] board){
		Node.size=board.length;
		Node.rowSize = 0;
		Node.colSize = 0;
		this.board=board;
		this.neighbours = new ArrayList<Node>();
		this.edges = new HashMap<Node , Integer>();
		this.heuristic = calcHeur(board);
		Node.blankD = 0;
	}

	public Node(int[][] board, boolean invert) {
		if(invert == true) {
			Node.size=board.length;
			this.board=board;
			this.neighbours = new ArrayList<Node>();
			this.edges = new HashMap<Node , Integer>();
		}
	}
	
	public int[][] getBoard() {
        return board;
    }

    public void addNeighbor(Node neighbor, int cost) {
        this.neighbours.add(neighbor);
		this.edges.put(neighbor, cost);
    }

    public void removeNeighbor(Node neighbor) {
        this.neighbours.remove(neighbor);
		this.edges.remove(neighbor);
    }

    public List<Node> getAllNeighbors() {
        return neighbours;
    }

	public Node getNeighbour(int direction) {
		return neighbours.get(direction);
	}

	public Map<Node, Integer> getEdges() {
        return edges;
    }
	
	public int getSize(){
		return Node.size;
	}

	public static int calcHeur(int[][] board){
		int curr_size = size;
		int ret = 0;
		while (checkOutside(board, size, size-curr_size)==0 && curr_size>4){
			curr_size--;
		}
		// if (curr_size>4){
		// 	int[][] goalBoard = makeGoal();
		// 	int r = checkRow(board, goalBoard);
		// 	int c = checkCol(board, goalBoard);
		// 	if(r <= c) {
		// 		ret = rowHeur(board, r);
		// 	}
		// 	else {
		// 		ret = colHeur(board, c);
		// 	}
		// }
		if (curr_size<=4){
			ret+=manhattan(board);
			ret+=calcHamming(board);
		}
		/* Comment out this else statement and the code at bottom to test the row solver. The row solver will infinitely loop after solving n-1/n of the row.
			To check if the code actually sovles n-1/n tiles in the row, press ctrl+c to break the loop. */
		else {
			ret+=manhattan(board);
		}
		
		return ret;
	}

	public static int calcHamming(int[][] board){
		int ret=0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// Skip the blank tile (represented by 0)
				if (board[i][j] == 0) {
					continue;
				}
				// Calculate the goal position for the current tile
				int goalRow = (board[i][j] - 1) / board.length;
				int goalCol = (board[i][j] - 1) % board.length;
				// Calculate the Hamming distance for the current tile
				if (i == goalRow || j == goalCol) {
					ret--;
				}
			}
		}
		return ret;
	}
	public static int manhattan(int[][] board) {
		int ret=0;
		int track=1;
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				int curr=board[i][j];
		
				if (curr!=0){
					int row = (curr-1) / size;
					int col = (curr-1) % size;
					ret += Math.abs(i-row) + Math.abs(j-col);
				}
			}
		}
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				// System.out.println("curr = " + board[i][j]);
				// System.out.println("track = " + track);
				if (i==size-1 && j==size-1){
					if (board[i][j]==0){
						ret--;
					}
				}
				if (board[i][j]==0){
					continue;
				}
				else if (board[i][j]!=track){
					ret+=Math.abs(board[i][j]-track);
				}
				track++;
			}
		}
		return ret;
	}
	public static int checkOutside(int[][]board, int size, int level) {
		int ret = 0;
		int track =1;
		for (int i = track; i < size-level; i++){
			if (board[level][i]!=track){
				ret++;
			}
			track++;
		}
		track=level;
		int val = 1+level+(level*size);
		for (int i = track ; i<size-level; i++){
			if (board[i][level]!=val){
				ret++;
			}
			val=val+size;
		}
		return ret;

	}

	public int getHeur(){
		return heuristic;
	}
	public void setG(int x){
		this.G = x;
	}

	public int getGScore(){
		return G;
	}

	public /*static*/ int[] getBlankPosition(int[][] board) {
		int[] position = new int[2];
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				if (board[i][j]==0){
					position[0] = i;
					position[1] = j;
					break;
				}
				else {
					continue;
				}
			}
		}
		return position;
	}

	public static int[][] makeGoal() {
		int[][] goalBoard = new int[size][size];
		int index = 1;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				goalBoard[i][j] = index;
				index++;
			}
		}
		goalBoard[size-1][size-1] = 0;
		return goalBoard;
	}

	public int getBlank() {
		this.blank = blankD;
		return blank;
	}

	@Override
	public int compareTo(Node other) {
		// if(this.heuristic!=other.heuristic)
		// {
		// 	return this.heuristic-other.heuristic;
		// }
		// else {
		// 	return this.getBlank()-other.getBlank();
		// }
		return this.heuristic-other.heuristic;
	} 
	@Override
	public int hashCode() {
		return Arrays.deepHashCode(board);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Node)) {
			return false;
		}
		Node other = (Node) obj;
		return Arrays.deepEquals(this.board, other.board);
	}
	

	/* Comment this out to test the row solver */
	// private static int checkRow(int[][]board, int[][] goalBoard) {
	// 	for(int i = 0; i < size; i++) {
	// 		for(int j = 0; j < size; j++) {
	// 			if(goalBoard[i][j] != board[i][j]) {
	// 				return i;
	// 			}
	// 		}
	// 	}
	// 	return -1;
	// }

	// private static int checkCol(int[][]board, int[][] goalBoard) {
	// 	for(int i = 0; i < size; i++) {
	// 		for(int j = 0; j < size; j++) {
	// 			if(goalBoard[j][i] != board[j][i]) {
	// 				return i;
	// 			}
	// 		}
	// 	}
	// 	return -1;
	// }

	// public static int rowHeur(int[][]board, int level) {
	// 	Map<Integer, int[]> goalRow = new HashMap<>();
	// 	int ret = 0;
	// 	for(int i = 0; i < size; i++) {
	// 		int[] temp = new int[2];
	// 		temp[0] = level;
	// 		temp[1] = i;
	// 		goalRow.put(level*size+i+1, temp);
	// 	}

	// 	int B[] = new int[2];
	// 	B = getBlankPosition(board);

	// 	for(int i = 0; i < size; i++) {
	// 		for(int j = 0; j < size; j++) {
	// 			if(goalRow.containsKey(board[i][j])) {
	// 				int x = Math.abs(goalRow.get(board[i][j])[0] - i);
	// 				int y = Math.abs(goalRow.get(board[i][j])[1] - j);
	// 				ret += x+y;
	// 				if(x!=0 && y!=0) {
	// 					int x2 = Math.abs(B[0]-i);
	// 					int y2 = Math.abs(B[1]-j);
	// 					blankD += x2+y2;
	// 				}
	// 			}
	// 		}
	// 	}

	// 	return ret;
	// }

	// public static int colHeur(int[][]board, int level) {
	// 	Map<Integer, int[]> goalCol = new HashMap<>();
	// 	int ret = 0;
	// 	for(int i = 0; i < size; i++) {
	// 		int[] temp = new int[2];
	// 		temp[0] = level;
	// 		temp[1] = i;
	// 		goalCol.put((level*i)+1, temp);
	// 	}

	// 	int B[] = new int[2];
	// 	B = getBlankPosition(board);

	// 	for(int i = 0; i < size; i++) {
	// 		for(int j = 0; j < size; j++) {
	// 			if(goalCol.containsKey(board[i][j])) {
	// 				int x = Math.abs(goalCol.get(board[i][j])[0] - i);
	// 				int y = Math.abs(goalCol.get(board[i][j])[1] - j);
	// 				ret += x+y;

	// 				int x2 = Math.abs(B[0]-i);
	// 				int y2 = Math.abs(B[1]-j);
	// 				blankD += x2+y2;
	// 			}
	// 		}
	// 	}

	// 	return ret;
	// }

}