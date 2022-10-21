import java.io.IOException;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

class PokemonYousab {
    public static void main(String[] args) throws IOException {
        PMap currentMap;
        Maps maps = new Maps();
        Scanner input = new Scanner(System.in);
        boolean playing = true;
        int cash = 0;
        HashMap<Integer, String> badges = new HashMap<>();
        ArrayList<Pokemon> party = new ArrayList<>();
        String playerName, rivalName = "";
        ArrayList<Item> bag = new ArrayList<>();
        ItemsList IL = new ItemsList();
        movesList ML = new movesList();
        PokemonList PL = new PokemonList();
        Rival rival;

        System.out.println("-------------");
        System.out.println("POKEMON YOUSAB");
        System.out.println("-------------");
        System.out.println("Type 'load' to load an existing game, and 'new game' to start a new game.");
        String loadQuestion = input.nextLine();
        if (loadQuestion.contains("load")) {
            try {
                File theSaveFile = new File("src/SaveObj.sav");
                FileInputStream saveFile = new FileInputStream(theSaveFile);
                ObjectInputStream restore = new ObjectInputStream(saveFile);
                playerName = (String) restore.readObject();
                cash = (int) restore.readObject();
                badges = (HashMap) restore.readObject();
                party = (ArrayList) restore.readObject();
                currentMap = (PMap) restore.readObject();
                bag = (ArrayList) restore.readObject();
                rival = (Rival) restore.readObject();

            } catch (Exception e) {
                System.out.println("There is no save file currently. Starting a new game.");
                String start = "";
                while (!start.equals("start")) {
                    System.out.println("Type 'Start' to begin.");
                    start = input.nextLine();
                    start = start.toLowerCase();
                }
                playerName = IntroUntilPlayerName();
                rivalName = RivalNameUntilEndIntro(playerName);
                rival = new Rival(rivalName);
                currentMap = maps.getMap("er room in your mom's");
            }

        } else {
            String start = "";
            while (!start.equals("start")) {
                System.out.println("Type 'Start' to begin.");
                start = input.nextLine();
                start = start.toLowerCase();
            }

            playerName = IntroUntilPlayerName();
            rivalName = RivalNameUntilEndIntro(playerName);
            rival = new Rival(rivalName);
            currentMap = maps.getMap("er room in your mom's");
        }
        while (playing) {
            printCurrentMap(currentMap);
            printMenu(party.size());
            String response = input.nextLine();
            response = response.toLowerCase();
            boolean battled = false;

            if (response.contains("move")) {
                printMOVEMenu(currentMap);
                PMap previousMap = currentMap;
                String response2 = input.nextLine();
                currentMap = goRoom(response2, currentMap);
                if (currentMap == null) {
                    currentMap = previousMap;
                    System.out.println("That's not a direction. You didn't move.");
                }
                else if (response2.contains("north") && party.size() == 0) {
                    currentMap = maps.getMap("yousab's la");
                    Pokemon starter = triggerStartingPokemonScene(rival.name, playerName);
                    starter.changeLevel(5);
                    party.add(starter);
                    if (starter.returnName().equals("Bulbasaur")) rival.addPokemonToParty(PL.returnPokemon("Charmander"));
                    else if (starter.returnName().equals("Charmander")) rival.addPokemonToParty(PL.returnPokemon("Squirtle"));
                    else if (starter.returnName().equals("Squirtle")) rival.addPokemonToParty(PL.returnPokemon("Bulbasaur"));
                    rival.team.get(0).changeLevel(5);
                    System.out.println("YOUSAB: This POKEMON is really energetic!");
                    System.out.println(rival.name + ": I'll take this one, then!");
                }
                else if (!battled && party.size() == 1) {
                    triggerFirstRivalBattle(rival.name, party, rival.team, playerName);
                    battled = true;
                }
            } else if (response.contains("info")) {
                printINFOMenu(playerName, cash, badges);
            } else if (response.contains("pokemon") && party.size() != 0) {
                printParty(party);
            } else if (response.contains("save")) {
                System.out.println("Saving will override the current file. Are you sure you want to do this? Type 'yes' if so.");
                String saveQuestion = input.nextLine();
                saveQuestion = saveQuestion.toLowerCase();
                if (saveQuestion.contains("yes")) {
                    File theFile = new File("src/SaveObj.sav");
                    FileOutputStream saveFile = new FileOutputStream(theFile);
                    ObjectOutputStream save = new ObjectOutputStream(saveFile);
                    save.writeObject(playerName);
                    save.writeObject(cash);
                    save.writeObject(badges);
                    save.writeObject(party);
                    save.writeObject(currentMap);
                    save.writeObject(bag);
                    save.writeObject(rival);
                    save.flush();
                    save.close();
                    System.out.println("Save successful.");
                }
            } else if (response.contains("bag")) {
                printBag(bag);
            } else if (response.contains("log off")) {
                System.out.println("Do you want to save first?");
                String response3 = input.nextLine();
                response3 = response3.toLowerCase();
                if (response3.contains("yes")) {
                    System.out.println("Saving will override the current file. Are you sure you want to do this? Type 'yes' if so.");
                    String saveQuestion = input.nextLine();
                    saveQuestion = saveQuestion.toLowerCase();
                    if (saveQuestion.contains("yes")) {
                        File theFile = new File("src/SaveObj.sav");
                        FileOutputStream saveFile = new FileOutputStream(theFile);
                        ObjectOutputStream save = new ObjectOutputStream(saveFile);
                        save.writeObject(playerName);
                        save.writeObject(cash);
                        save.writeObject(badges);
                        save.writeObject(party);
                        save.writeObject(currentMap);
                        save.writeObject(bag);
                        save.writeObject(rival);
                        save.flush();
                        save.close();
                        System.out.println("Save successful.");
                        System.out.println("Logging off now.");
                        playing = false;
                    }
                } else {
                    System.out.println("Logging off now.");
                    playing = false;
                }
            }

        }
    }

    static String IntroUntilPlayerName() {
        Scanner input = new Scanner(System.in);


        System.out.println("Hello there! Welcome to the world of POKEMON! My name is Yousab!");
        System.out.println("People call me the POKEMON PROF!");
        System.out.println("This world is inhabited by creatures called POKEMON!");
        System.out.println("For some people, POKEMON are pets. Others use them for fights.");
        System.out.println("Myself... I study POKEMON as a profession.");
        String continu = "";
        while (!continu.equals("c")) {
            System.out.println("Type 'C' to continue.");
            continu = input.nextLine();
            continu = continu.toLowerCase();
        }
        System.out.println("First, what is your name?");
        String player = input.nextLine();
        System.out.println("Right! So your name is " + player + "!");
        return player;
    }

    static String RivalNameUntilEndIntro(String playerName) {
        Scanner input = new Scanner(System.in);
        new ImageLoader("garyy.png");
        System.out.println("This is my grandson. He's been your rival since you were a baby.");
        System.out.println("...Erm, what is his name again?");
        String rivalName = input.nextLine();
        System.out.println("That's right! I remember now! His name is " + rivalName + "!");
        System.out.println(playerName + "! Your very own POKEMON legend is about to unfold!");
        System.out.println("A world of dreams and adventures with POKEMON awaits! Lets go!");
        return rivalName;
    }

    static void printCurrentMap(PMap map) {
        System.out.println(map.getDescription());
    }

    static void printMOVEMenu(PMap map) {
        System.out.println("You can currently do the following:");
        System.out.print(map.printExits());
        System.out.println("Type 'go' followed by where you want to go to move.");
    }

    static PMap goRoom(String input, PMap currentMap) {
        ArrayList<String> tempDirections = currentMap.returnDirections();
        for (String direction : tempDirections) {
            if (input.contains(direction)) {
                return currentMap.getMap(direction);
            }
        }
        return null;
    }

    static void printMenu(int numberOfPokemonInParty) {
        if (numberOfPokemonInParty == 0) {
            System.out.println("MOVE      INFO      BAG      SAVE      LOG OFF");
        } else {
            System.out.println("MOVE      INFO      BAG      POKEMON      SAVE      LOG OFF");
        }
    }

    static void printINFOMenu(String player, int cash, HashMap<Integer, String> badges) {
        System.out.println("************************************");
        System.out.println("PLAYER " + player + "");
        System.out.println("MONEY " + cash + "");
        System.out.println("TIME " + java.time.LocalTime.now());
        System.out.println("***********************************");
        System.out.println("*              BADGES             *");
        if (badges.size() != 0) {
            for (int i = 0; i < badges.size(); i++) {
                System.out.println(badges.get(i));
            }
        } else {
            System.out.println("NONE YET!");
        }
    }

    static Pokemon triggerStartingPokemonScene(String rivalName, String playerName) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        PokemonList pk = new PokemonList();

        System.out.println("YOUSAB: Hey! Wait! Don't go out!");
        System.out.println("It's unsafe! Wild POKEMON live in tall grass!");
        System.out.println("YOUSAB: You need your own POKEMON for your protection. I know! Here, come with me!");
        System.out.println(rivalName + ": Gramps! I'm fed up with waiting!");
        System.out.println("YOUSAB: " + rivalName + "? Let me think... Oh, that's right, I told you to come! Just wait!");
        new ImageLoader("starters.png");
        System.out.println("YOUSAB: I only have 3 pokemon left here. They are inside poke balls.");
        System.out.println("YOUSAB: You can have one " + playerName + "!");
        System.out.println(rivalName + ": Hey! Gramps! What about me?");
        System.out.println("Be patient! " + rivalName + ", you can have one too!");
        System.out.println("YOUSAB: Now, " + playerName + " which POKEMON do you want?");

        System.out.println(rivalName + ": Heh, I don't need to be greedy like you! Go ahead and choose, RED!");
        System.out.println("Type '1' for Bulbasaur, '2' for Charmander, '3' for Squirtle.");
        boolean chosen = false;
        while (!chosen) {
            System.out.println("Go ahead! Choose one!");
            String response = input.nextLine();
            if (response.equals("1")) {
                System.out.println("So! You want the plant Pokemon Bulbasaur?");
                String response2 = input.nextLine();
                response2 = response2.toLowerCase();
                if (response2.equals("yes")) {
                    return pk.returnPokemon("Bulbasaur");
                }
            } else if (response.equals("2")) {
                System.out.println("So! You want the fire Pokemon Charmander?");
                String response3 = input.nextLine();
                response3 = response3.toLowerCase();
                if (response3.equals("yes")) {
                    return pk.returnPokemon("Charmander");
                }
            } else if (response.equals("3")) {
                System.out.println("So! You want the water Pokemon Squirtle?");
                String response4 = input.nextLine();
                response4 = response4.toLowerCase();
                if (response4.equals("yes")) {
                    return pk.returnPokemon("Squirtle");
                }
            }
        }
        return null;
    }

    static void printParty(ArrayList<Pokemon> party) {
        Scanner input = new Scanner(System.in);
        String partyString = "";
        for (int i = 0; i < party.size(); i++) {
            partyString = (i + 1) + ": " + party.get(i).returnName() + "\n";
        }
        System.out.println("Type the number of the pokemon to view stats. Exit to leave.");
        System.out.println(partyString);
        String response = input.nextLine();
        if (response.matches("[0-6]+")) {
            for (int k = 1; k <= 6; k++) {
                String s = Integer.toString(k);
                if (response.contains(s)) {
                    try {
                        System.out.println(party.get(k - 1).returnStats());
                    } catch (Exception e) {
                        System.out.println("No pokemon found!");
                    }
                }
            }
        }
    }

    static void printBag(ArrayList<Item> bag) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        ItemsList IL = new ItemsList();
        int screen = 0;
        System.out.println("Type '1' to move right, '-1' to move left, and 'EXIT' to escape.");
        String response = "1";
        while ((response.matches("[-1-1]+"))) {
            if (response.equals("1"))
            {
                screen++;
                if (screen == 4)
                {
                    screen = 1;
                }
            } else if (response.equals("-1"))
            {
                screen--;
                if (screen == 0)
                {
                    screen = 3;
                }
            }
            if (screen == 1) {
                System.out.println("ITEMS:");
                String ITEMS = IL.returnITEMS(bag);
                if (ITEMS.equals("")) {
                    System.out.println("NONE");
                } else {
                    System.out.println(ITEMS);
                }
            } else if (screen == 2) {
                System.out.println("KEYITEMS:");
                String ITEMS = IL.returnKEYITEMS(bag);
                if (ITEMS.equals("")) {
                    System.out.println("NONE");
                } else {
                    System.out.println(ITEMS);
                }
            } else if (screen == 3) {
                System.out.println("POKEBALLS:");
                String POKEBALLS = IL.returnPOKEBALLS(bag);
                if (POKEBALLS.equals("")) {
                    System.out.println("NONE");
                } else {
                    System.out.println(POKEBALLS);
                }
            }
            response = input.nextLine();
        }
    }

    static void triggerFirstRivalBattle(String rivalName, ArrayList<Pokemon> party, ArrayList<Pokemon> rivalParty, String playerName) throws FileNotFoundException {
        System.out.println(rivalName + ": Wait " + playerName + "! Let's check out our POKEMON! Come on, I'll take you on!");
        Battle firstBattle = new personBattle(party, rivalParty, rivalName, 5);
    }
}
