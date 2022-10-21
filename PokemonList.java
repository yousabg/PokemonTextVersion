import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class PokemonList {
    private static final ArrayList<Pokemon> pokemons = new ArrayList<>();
    movesList ML = new movesList();

    public PokemonList() throws FileNotFoundException {
        addPokemon();
        addTypes();
        addMoveSets();
        for (Pokemon pokemon : pokemons)
        {
            pokemon.addMoves();
        }
    }

    static int v1, v2, v3, v4, v5;
    static String name;

    static void addPokemon() throws FileNotFoundException {
        Scanner fileScan = new Scanner(new File("src/genOnePokemon.txt"));
        Scanner fileScan2 = new Scanner(new File("src/genOnePokemonNames.txt"));
        while (fileScan.hasNext() || fileScan2.hasNextLine()) {

            v1 = fileScan.nextInt();
            v2 = fileScan.nextInt();
            v3 = fileScan.nextInt();
            v4 = fileScan.nextInt();
            v5 = fileScan.nextInt();
            name = fileScan2.nextLine();

            pokemons.add(new Pokemon(v1, v2, v3, v4, v5, 2, name));
        }


    }

    Pokemon returnPokemon(String name) {
        for (Pokemon pokemon : pokemons) {
            if (pokemon.returnName().equals(name)) {
                return pokemon;
            }
        }
        return null;
    }

    void addTypes() throws FileNotFoundException {
        Scanner typeScan = new Scanner(new File("src/pokemonTypes.txt"));
        Scanner secondaryTypeScan = new Scanner(new File("src/pokemonSecondaryTypes.txt"));
        int i = 0;
        while (typeScan.hasNextLine() && secondaryTypeScan.hasNextLine()) {
            pokemons.get(i).addType(typeScan.nextLine(), secondaryTypeScan.nextLine());
            i++;
        }
    }

    void addMoveSets() throws FileNotFoundException {
        Scanner movePoolScanner = new Scanner(new File("src/movePools.txt"));
        boolean samePokemon = false;
        Pokemon tempPokemon = null;
        Move tempMove = null;
        int tempValue = 0;
        String line = "";

        while (movePoolScanner.hasNextLine()) {
            line = movePoolScanner.nextLine();
            if (samePokemon && !line.contains("NEXT")) {
                String[] levelAndMoveSplit = line.split(" ", 2);
                Integer level = Integer.parseInt(levelAndMoveSplit[0]);
                tempMove = ML.findMoveByName(levelAndMoveSplit[1]);
                if (tempMove != null) {
                pokemons.get(tempValue).addToMovePool(tempMove, level); }
            } else if (line.contains("NEXT")) {
                samePokemon = false;
            } else if (!samePokemon && !line.contains("Mewtwo")) {
                for (int i = 0; i < pokemons.size(); i++) {
                    if (line.contains(pokemons.get(i).returnName())) {
                        samePokemon = true;
                        tempValue = i;
                    }
                }
            } else if (!samePokemon && line.contains("Mewtwo")) {
                samePokemon = true;
                tempValue = 149;
            }
        }

    }
}
