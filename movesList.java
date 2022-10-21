import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class movesList {
    static final ArrayList<Move> moves = new ArrayList<>();

    public movesList() throws FileNotFoundException {
        addMoves();
    }

    public void addMoves() throws FileNotFoundException {
        Scanner moveNames = new Scanner(new File("src/moveNames.txt"));
        Scanner moveTypes = new Scanner(new File("src/moveTypes.txt"));
        Scanner moveValues = new Scanner(new File("src/moveValues.txt"));
        Scanner movePP = new Scanner(new File("src/movePP.txt"));
        Scanner movePower = new Scanner(new File("src/movePower.txt"));
        Scanner moveAccuracy = new Scanner(new File("src/moveAccuracy.txt"));

        while (moveNames.hasNext())
        {
            moves.add(new Move(moveNames.nextLine(), moveTypes.nextLine(), moveValues.nextInt(), movePP.nextInt(), movePower.nextInt(), moveAccuracy.nextDouble()));
        }
    }

    public String printMove()
    {
        return moves.get(164).name;
    }

    public static Move findMoveByName(String name)
    {
        for (Move move : moves)
        {
            if (name.contains(move.returnName()))
            {
                return move;
            }
        }
        return null;
    }

}
