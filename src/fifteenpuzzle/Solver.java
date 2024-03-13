package fifteenpuzzle;

import java.util.List;
import java.io.*;
public class Solver {
	
	static int size;

	public static int[][] buildBoard(String board) throws IOException  {
		BufferedReader reader = new BufferedReader(new FileReader(board));
		String sizeStr = reader.readLine(); // read size as string from the first line of the file
		size = Integer.parseInt(sizeStr); // convert size string to int
		int [][]boardArr = new int[size][size];
		int c1, c2, s;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				c1 = reader.read();
				c2 = reader.read();
				s = reader.read(); // skip the space
				if (c1 == ' ')
					c1 = '0';
				if (c2 == ' ')
					c2 = '0';
				boardArr[i][j] = 10 * (c1 - '0') + (c2 - '0');
			}
		}
		reader.close();
		return boardArr;
	}

	public static int[][] createGoal(int size){
		int[][] goalState = new int[size][size];
		int curr=1;
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				if (i==size-1 && j==size-1){
					goalState[i][j] = 0;
				}
				else {
					goalState[i][j] = curr;
					curr++;
				}
			}
		}
		return goalState;
	}

	public static void writer(List<String> moves, String fileName){
		if (moves != null){
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
				for (String move : moves){
					writer.write(move);
					writer.newLine();
				}
			} catch (IOException e) {
				System.err.println("error writing to file");
			}
		}else {
			System.out.println("No solution found");
		}
	}

	public static void main(String[] args) throws IOException {

		int[][] board = buildBoard(args[0]);
		Node boardNode = new Node(board);
		// boardGraph BG = new boardGraph(boardNode);
		int[][] goal = createGoal(size);
			
		Node goalNode = new Node(goal);
		// boardGraph gN = new boardGraph(goalNode);

		List<String> solution=boardGraph.aStar(boardNode, goalNode);
		writer(solution, args[1]);
	}
}