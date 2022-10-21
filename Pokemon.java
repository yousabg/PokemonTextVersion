import java.io.Serial;
import java.util.Random;
import java.io.Serializable;
import java.util.*;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class Pokemon implements Serializable {
    Random calc = new Random();
    //stats max = 999, min = 1
    int hp, attack, defense, speed, special, level, IV, EV, hpIV, attackIV, defenseIV, speedIV, specialIV;
    int currenthp;
    int baseHP, baseATTACK, baseDEFENSE, baseSPEED, baseSPECIAL;
    int experienceSpeed = calc.nextInt(4) + 1;
    String name;
    String type;
    String secondaryType = null;
    String nature, increaseNatureStat, decreaseNatureStat;
    Move[] moves = new Move[4];
    LinkedHashMap<Move, Integer> movePool = new LinkedHashMap<>();
    String status = "";
    String volatileStatus = "";

    String protection = "";

    int sleepCounter;


    boolean invulnerable = false;

    boolean mist = false;

    public Pokemon(int baseHP, int baseATTACK, int baseDEFENSE, int baseSPECIAL, int baseSPEED, int level, String name) throws FileNotFoundException {
        addNatures();
        this.name = name;
        Random IVcalculator = new Random();

        this.baseHP = baseHP;
        this.baseATTACK = baseATTACK;
        this.baseDEFENSE = baseDEFENSE;
        this.baseSPEED = baseSPEED;
        this.baseSPECIAL = baseSPECIAL;

        this.level = level;

        hpIV = IVcalculator.nextInt(32);
        attackIV = IVcalculator.nextInt(32);
        defenseIV = IVcalculator.nextInt(32);
        speedIV = IVcalculator.nextInt(32);
        specialIV = IVcalculator.nextInt(32);

        hp = ((((2 * baseHP + hpIV) * level) / 100) + level + 10);
        attack = (int) (((2 * baseATTACK + attackIV * 0.110) * level) / 100) + 5;
        defense = (int) (((2 * baseDEFENSE + defenseIV * 0.110) * level) / 100) + 5;
        speed = (int) (((2 * baseSPEED + speedIV * 0.110) * level) / 100) + 5;
        special = (int) (((2 * baseSPECIAL + specialIV * 0.110) * level) / 100) + 5;
        currenthp = hp;
        considerNatures();

        addMoves();

    }

    public void levelUp() {
        level++;
        hp = ((((2 * baseHP + hpIV) * level) / 100) + level + 10);
        attack = (int) (((2 * baseATTACK + attackIV * 0.110) * level) / 100) + 5;
        defense = (int) (((2 * baseDEFENSE + defenseIV * 0.110) * level) / 100) + 5;
        speed = (int) (((2 * baseSPEED + speedIV * 0.110) * level) / 100) + 5;
        special = (int) (((2 * baseSPECIAL + specialIV * 0.110) * level) / 100) + 5;
        currenthp = hp;
        considerNatures();
        addMoves();
    }

    public int getXPNeeded(int level) {
        int xpNeeded = 0;
        if (experienceSpeed == 1) {
            xpNeeded = (int) ((0.8) * ((level) * (level) * (level)));
        } else if (experienceSpeed == 2) {
            xpNeeded = (level * level * level);
        } else if (experienceSpeed == 3) {
            xpNeeded = (int) ((1.2) * ((level) * (level) * (level)));
        } else {
            xpNeeded = (int) ((1.25) * ((level) * (level) * (level)));
        }
        return xpNeeded;
    }

    public String returnName() {
        return name;
    }

    public void changeLevel(int newLevel) {
        level = newLevel;
        hp = ((((2 * baseHP + hpIV) * level) / 100) + level + 10);
        attack = (int) (((2 * baseATTACK + attackIV * 0.110) * level) / 100) + 5;
        defense = (int) (((2 * baseDEFENSE + defenseIV * 0.110) * level) / 100) + 5;
        speed = (int) (((2 * baseSPEED + speedIV * 0.110) * level) / 100) + 5;
        special = (int) (((2 * baseSPECIAL + specialIV * 0.110) * level) / 100) + 5;
        currenthp = hp;
        addMoves();
    }

    public String returnStats() {
        String stats = "Name: " + name;
        if (secondaryType == null || secondaryType.equals("None")) {
            stats += " Type: " + type + " Pokemon.";
        } else {
            stats += " Type: " + type + "/" + secondaryType + " Pokemon.";
        }
        stats += "\nNATURE: " + nature;
        stats += "\nLEVEl: " + level;
        stats += "\nCURRENT HP: " + currenthp;
        stats += "\nATTACK: " + attack;
        stats += "\nDEFENSE: " + defense;
        stats += "\nSPEED: " + speed;
        stats += "\nSPECIAL: " + special;
        stats += "\nMOVES: " + "\n" + printMoves();
        return stats;

    }

    public void addType(String firstType) {
        type = firstType;
    }

    public void addType(String firstType, String secondType) {
        type = firstType;
        secondaryType = secondType;
    }

    public void addNatures() throws FileNotFoundException {
        Random natureGenerator = new Random();
        int natureValue = natureGenerator.nextInt(17) + 1;
        try {
            nature = Files.readAllLines(Paths.get("src/NatureNames.txt")).get(natureValue - 1);
            increaseNatureStat = Files.readAllLines(Paths.get("src/NatureIncrease.txt")).get(natureValue - 1);
            decreaseNatureStat = Files.readAllLines(Paths.get("src/NatureDecrease.txt")).get(natureValue - 1);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void considerNatures() {
        if (increaseNatureStat.contains("Attack")) {
            attack = (int) (attack * 1.10);
        } else if (increaseNatureStat.contains("Defense")) {
            defense = (int) (defense * 1.10);
        } else if (increaseNatureStat.contains("Speed")) {
            speed = (int) (speed * 1.10);
        } else if (increaseNatureStat.contains("Special")) {
            special = (int) (special * 1.10);
        }

        if (decreaseNatureStat.contains("Attack")) {
            attack = (int) (attack * 0.90);
        } else if (decreaseNatureStat.contains("Defense")) {
            defense = (int) (defense * 0.90);
        } else if (decreaseNatureStat.contains("Speed")) {
            speed = (int) (speed * 0.90);
        } else if (decreaseNatureStat.contains("Special")) {
            special = (int) (special * 0.90);
        }
    }

    public void addToMovePool(Move move, Integer level) {
        movePool.put(move, level);
    }

    public String printMoves() {
        String s = "";
        int i = 1;
        for (Move move : moves) {
            if (move != null)
            {
                s += "      " + i + ": TYPE: " + move.type + "/" + "NAME: " + move.name + "/" + "PP: " + move.currentpp + "/" + move.pp + "\n";
            }
        }
        return s;
    }

    public void heal() {
        currenthp = hp;
        attack = (int) (((2 * baseATTACK + attackIV * 0.110) * level) / 100) + 5;
        defense = (int) (((2 * baseDEFENSE + defenseIV * 0.110) * level) / 100) + 5;
        speed = (int) (((2 * baseSPEED + speedIV * 0.110) * level) / 100) + 5;
        special = (int) (((2 * baseSPECIAL + specialIV * 0.110) * level) / 100) + 5;
        status = "";
        volatileStatus = "";
        sleepCounter = 0;
    }
    public void addMoves() {
        Set<Move> keySet = movePool.keySet();
        ArrayList<Move> movesP = new ArrayList<Move>(keySet);

        int limit = 0;
        //for (Move move : movesP) {
        //    System.out.println(move.name + " -- "
        //            + movePool.get(move));
        //}
        for (Move move : movesP) {
            if (movePool.get(move) < level)
            {
                if (limit < 4)
                {
                    moves[limit] = move;
                    limit++;
                } else {
                    for (int i = 0; i < moves.length; i++)
                    {
                        if (movePool.get(move) > movePool.get(moves[i]))
                        {
                            moves[i] = move;
                        }
                    }
                }
            }
        }



    }
}
