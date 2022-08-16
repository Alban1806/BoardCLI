import java.util.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


// Notes:   Add a timer of 30 seconds per turn for each player.

@CommandLine.Command(name = "Al's Board Game", mixinStandardHelpOptions = true)
public class BoardGame implements Callable<Integer> {

    // Run CLI
    public static void main(String[] args) {
        int exitStatus = new CommandLine(new BoardGame()).execute(args);
        System.exit(exitStatus);
    }

    // Insert 'x' or 'o' on tic-tac-toe board.
    private static void insertToBoard(char[][] currentPosition, String position, int player) {

        if(position.equals("NW")) currentPosition[0][0] = (player == 1 ? 'x' : 'o');
        else if(position.equals("N")) currentPosition[0][1] = (player == 1 ? 'x' : 'o');
        else if(position.equals("NE")) currentPosition[0][2] = (player == 1 ? 'x' : 'o');
        else if(position.equals("W")) currentPosition[1][0] = (player == 1 ? 'x' : 'o');
        else if(position.equals("C")) currentPosition[1][1] = (player == 1 ? 'x' : 'o');
        else if(position.equals("E")) currentPosition[1][2] = (player == 1 ? 'x' : 'o');
        else if(position.equals("SW")) currentPosition[2][0] = (player == 1 ? 'x' : 'o');
        else if(position.equals("S")) currentPosition[2][1] = (player == 1 ? 'x' : 'o');
        else if(position.equals("SE")) currentPosition[2][2] = (player == 1 ? 'x' : 'o');
    }

    /*  Print the current board and current positions.
    'x' mark = all positions player 1 picked
    'o' mark = all positions player 2 picked
 */
    private static void displayBoard(char[][] currentPositions) {

        for(int row = 0; row < currentPositions.length; row++) {
            for(int col = 0; col < currentPositions[row].length; col++) {
                System.out.print(currentPositions[row][col]);
            }
            System.out.println();
        }
    }

    /* Board Game Parameter Options:
        1 = Tic-Tac-Toe
        2 = Connect 4 (to be continued)
        3 = Checkers (to be continued)
     */
    @Parameters(index = "0", description = "Enter:%n1=Tic-Tac-Toe", paramLabel = "Board Game")
    int boardGameType;

    /* Opponent Options (-o):
        1 = CPU
        2 = Player 2
     */
    @Option(names = {"-o"}, description = "Enter:%n1 = CPU%n2 = Player 2%n", paramLabel = "Opponent")
    Integer opponent = 1;

    public Integer call() throws Exception{

        if (boardGameType == 1) {
            LinkedList<String> possibleMoves = new LinkedList<String>(List.of(new String[]{"NW", "N", "NE", "W", "C", "E", "SW", "S", "SE"}));
            HashMap<Integer,LinkedList<String>> winCondition = new HashMap<Integer,LinkedList<String>>();
            LinkedList<String> p1Positions = new LinkedList<String>();
            LinkedList<String> p2Positions = new LinkedList<String>();
            Scanner p1 = new Scanner(System.in);
            Scanner p2 = new Scanner(System.in);
            String p1Input, p2Input;
            char[][]  currentPositions = new char[3][3];
            int p1win = 0, p2win = 0;
            Random cpuSelect = new Random();

            // Initialize the positions of the board in array to 0.
            for(int i = 0; i < currentPositions.length; i++){
                for(int j = 0; j < currentPositions.length; j++) {
                    currentPositions[i][j] = '-';
                }
            }

            // Record all possible win conditions
            winCondition.put(1,new LinkedList<String>(List.of(new String[]{"NW","N","NE"})));
            winCondition.put(2,new LinkedList<String>(List.of(new String[]{"NW","W","SW"})));
            winCondition.put(3,new LinkedList<String>(List.of(new String[]{"NE","E","SE"})));
            winCondition.put(4,new LinkedList<String>(List.of(new String[]{"SW","S","SE"})));
            winCondition.put(5,new LinkedList<String>(List.of(new String[]{"N","C","S"})));
            winCondition.put(6,new LinkedList<String>(List.of(new String[]{"W","C","E"})));
            winCondition.put(7,new LinkedList<String>(List.of(new String[]{"NW","C","SE"})));
            winCondition.put(8,new LinkedList<String>(List.of(new String[]{"NE","C","SW"})));

            displayBoard(currentPositions);

            while(true) {

                // Player 1 move.
                System.out.println("\nPlayer 1 Turn: " + possibleMoves.toString());
                p1Input = p1.nextLine().toUpperCase();

                // Let player 1 enter until valid move.
                while(!possibleMoves.contains(p1Input)) {
                    System.out.println(p1Input + " is invalid\n");
                    System.out.println("Player 1 Turn: " + possibleMoves.toString());
                    p1Input = p1.nextLine().toUpperCase();
                }
                // Mark player 1 position on board with an x.
                insertToBoard(currentPositions,p1Input,1);
                // Add to player 1 current position on the board and remove from possible moves to choose from.
                p1Positions.add(possibleMoves.remove(possibleMoves.indexOf(p1Input)));

                displayBoard(currentPositions);

                // Search list of win conditions to see if player 1 wins.
                for(LinkedList<String> win : winCondition.values()) {
                    // If player has 3 values in a win condition set, then player1 wins.
                    p1win = 0;
                    for(String p1Position : p1Positions) {
                        if(win.contains(p1Position)) p1win += 1;
                    }
                    if (p1win == 3) {
                        System.out.println("\nPlayer 1 Wins");
                        break;
                    }
                }
                if(p1win == 3) break;

                // When no more turns left, end loop. This only happens after player 1 last turn.
                if(possibleMoves.isEmpty()) {
                    System.out.println("\nTie Game");
                    break;
                }

                // CPU/Player 2 move.
                if (opponent == 1) {
                    System.out.println("\nCPU Turn: " + possibleMoves.toString());
                    TimeUnit.SECONDS.sleep(2);
                    p2Input = possibleMoves.get(cpuSelect.nextInt(0,possibleMoves.size())).toUpperCase();
                }
                else {
                    System.out.println("\nPlayer 2 Turn: " + possibleMoves.toString());
                    p2Input = p2.nextLine().toUpperCase();
                }

                // Let player 2 enter until valid move.
                while(!possibleMoves.contains(p2Input)) {
                    System.out.println(p2Input + " is invalid");
                    System.out.println("Player 2 Turn: " + possibleMoves.toString());
                    p2Input = p2.nextLine().toUpperCase();
                }
                // Mark player 2 position on board with an x.
                insertToBoard(currentPositions,p2Input,2);
                // Add to player 2 current position on the board and remove from possible moves to choose from.
                p2Positions.add(possibleMoves.remove(possibleMoves.indexOf(p2Input)));

                displayBoard(currentPositions);

                // Search list of win conditions to see if player 2 wins.
                for(LinkedList<String> win : winCondition.values()) {
                    // If player has 3 values in a win condition set, then player2/cpu wins.
                    p2win = 0;
                    for(String p2Position : p2Positions) {
                        if(win.contains(p2Position)) p2win += 1;
                    }
                    if (p2win == 3) {
                        if(opponent == 1) System.out.println("\nCPU Wins");
                        else System.out.println("\nPlayer 2 Wins");
                        break;
                    }
                }
                if(p2win == 3) break;
            }
        }
        return 0;
    }
}