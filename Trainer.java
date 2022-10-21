import java.util.ArrayList;
import java.io.Serializable;
public class Trainer implements Serializable {

    ArrayList<Pokemon> team = new ArrayList<>();
    String name;

    public Trainer(String name)
    {
        this.name = name;
    }

    public void addPokemonToParty(Pokemon pokemon)
    {
        if (team.size() != 6) {
            team.add(pokemon);
        }
    }
}

class Rival extends Trainer{

    public Rival(String name) {
        super(name);
    }
}
