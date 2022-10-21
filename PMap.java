import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
public class PMap implements Serializable {
    private String description;
    private String name;
    private Map<String, PMap> exits = new HashMap<String,PMap>();
    private ArrayList<String> directions = new ArrayList<>();

    public PMap(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public void addExit(String direction, PMap map) {
        exits.put(direction, map);
        directions.add(direction);
    }

    public void removeExit(String direction) {
        exits.remove(direction);
    }

    public String printExits() {
        String listOfExits = "";
        for (int k = 0; k < exits.size(); k++) {
            listOfExits += "Going " + directions.get(k) + " leads to "+ exits.get(directions.get(k)).getName() + ".\n";
        }
        return listOfExits;
    }

    public PMap getMap(String direction)
    {
        return exits.get(direction);
    }

    public ArrayList<String> returnDirections() {
        return directions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
