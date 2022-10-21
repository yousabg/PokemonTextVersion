import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;
import java.util.Random;

public class Battle {
    ArrayList<Pokemon> playerParty;
    Pokemon currentPokemon;
    Pokemon opposingPokemon;
    int cAttack, cDefense, cSpecial, cSpeed;
    int oAttack, oDefense, oSpecial, oSpeed;
    int cAttackStatMod, cDefenseStatMod, cSpecialStatMod, cSpeedStatMod, cEvasionStatMod, cAccuracyStatMod;
    int oAttackStatMod, oDefenseStatMod, oSpecialStatMod, oSpeedStatMod, oEvasionStatMod, oAccuracyStatMod;

    int damageLastMoveDid;
    Move lastMoveUsed;
    Move MMlastMoveUsed;

    int cToxicCounter = 1;
    int oToxicCounter = 1;
    Move previousMovePicked = null;
    Move previousOppMove = null;

    int pBideCounter;
    int oBideCounter;
    private static final String[][] genITypeChart =
            {
                    // "norm" fght fly pois grnd rock bug ghst fire wter grss elec psyc ice drag
                    /* "norm" */ {"norm", "str", "norm", "norm", "norm", "norm", "norm", "inef", "norm", "norm", "norm", "norm", "norm", "norm", "norm"},
                    /* fght */ {"norm", "norm", "str", "norm", "norm", "norm", "norm", "inef", "norm", "norm", "norm", "norm", "norm", "norm", "norm"},
                    /* fly */ {"norm", "weak", "norm", "norm", "inef", "str", "weak", "norm", "norm", "norm", "weak", "str", "norm", "str", "norm"},
                    /* pois */ {"norm", "weak", "norm", "weak", "str", "norm", "str", "norm", "norm", "norm", "weak", "norm", "str", "norm", "norm"},
                    /* grnd */ {"norm", "norm", "norm", "weak", "norm", "weak", "norm", "norm", "norm", "str", "str", "inef", "norm", "str", "norm"},
                    /* rock */ {"weak", "str", "weak", "weak", "str", "norm", "norm", "norm", "norm", "str", "str", "norm", "norm", "norm", "norm"},
                    /* bug */ {"norm", "weak", "str", "norm", "weak", "str", "norm", "norm", "str", "norm", "weak", "norm", "norm", "norm", "norm"},
                    /* ghst */ {"inef", "inef", "norm", "weak", "norm", "norm", "weak", "norm", "norm", "norm", "norm", "norm", "norm", "norm", "norm"},
                    /* fire */ {"norm", "norm", "norm", "norm", "str", "str", "weak", "norm", "weak", "str", "weak", "norm", "norm", "norm", "norm"},
                    /* wter */ {"norm", "norm", "norm", "norm", "norm", "norm", "norm", "norm", "weak", "weak", "str", "str", "norm", "weak", "norm"},
                    /* grss */ {"norm", "norm", "str", "str", "weak", "norm", "str", "norm", "str", "weak", "weak", "weak", "norm", "str", "norm"},
                    /* elec */ {"norm", "norm", "weak", "norm", "str", "norm", "norm", "norm", "norm", "norm", "norm", "weak", "norm", "norm", "norm"},
                    /* psyc */ {"norm", "weak", "norm", "norm", "norm", "norm", "str", "inef", "norm", "norm", "norm", "norm", "weak", "norm", "norm"},
                    /* ice */ {"norm", "str", "norm", "norm", "norm", "str", "norm", "norm", "str", "norm", "norm", "norm", "norm", "weak", "norm"},
                    /* drag */ {"norm", "norm", "norm", "norm", "norm", "norm", "norm", "norm", "weak", "weak", "weak", "weak", "norm", "str", "str"},
                    // "norm" fght fly pois grnd rock bug ghst fire wter grss elec psyc ice drag
            };

    private static final String[] types = {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Fire", "Water", "Grass", "Electric", "Psychic", "Ice", "Dragon"};

    private static final int[][] statRatios = {{25, 100}, //0 (-6)
            {28, 100}, //1 (-5)
            {33, 100}, //2 (-4)
            {40, 100}, //3 (-3)
            {50, 100}, //4 (-2)
            {66, 100}, //5 (-1)
            {1, 1},    //6 (0)
            {15, 10},  //7 (+1)
            {2, 1},    //8 (+2)
            {25, 10},  //9 (+3)
            {3, 1},    //10 (+4)
            {35, 10},  //11 (+5)
            {4, 1}     //12 (+6)
    };

    public Battle() {
    }

    public Battle(ArrayList<Pokemon> playerParty, Pokemon opposingPokemon) {
        this.playerParty = playerParty;
        currentPokemon = playerParty.get(0);
        this.opposingPokemon = opposingPokemon;

        changeStats();
    }

    public void calculateStatChanges(int value, int statChanging, int playerAffected) {
        boolean affectingPlayer = false;
        boolean affectingEnemy = false;

        //1-6 for cAttackStatMod, cDefenseStatMod, cSpecialStatMod, cSpeedStatMod, cEvasionStatMod, cAccuracyStatMod respectively
        //1 for player, 2 for enemy

        if (playerAffected == 1) affectingPlayer = true;
        if (playerAffected == 2) affectingEnemy = true;

        if (affectingPlayer) {
            if (statChanging == 1) {
                if (cAttackStatMod != 12 && cAttackStatMod != 0) {
                    cAttackStatMod += value;
                    if (cAttackStatMod > 12) {
                        cAttackStatMod = 12;
                    } else if (cAttackStatMod < 0) {
                        cAttackStatMod = 0;
                    }
                }
            } else if (statChanging == 2) {
                if (cDefenseStatMod != 12 && cDefenseStatMod != 0) {
                    cDefenseStatMod += value;
                    if (cDefenseStatMod > 12) {
                        cDefenseStatMod = 12;
                    } else if (cDefenseStatMod < 0) {
                        cDefenseStatMod = 0;
                    }
                }
            } else if (statChanging == 3) {
                if (cSpecialStatMod != 12 && cSpecialStatMod != 0) {
                    cSpecialStatMod += value;
                    if (cSpecialStatMod > 12) {
                        cSpecialStatMod = 12;
                    } else if (cSpecialStatMod < 0) {
                        cSpecialStatMod = 0;
                    }
                }
            } else if (statChanging == 4) {
                if (cSpeedStatMod != 12 && cSpeedStatMod != 0) {
                    cSpeedStatMod += value;
                    if (cSpeedStatMod > 12) {
                        cSpeedStatMod = 12;
                    } else if (cSpeedStatMod < 0) {
                        cSpeedStatMod = 0;
                    }
                }
            } else if (statChanging == 5) {
                if (cEvasionStatMod != 12 && cEvasionStatMod != 0) {
                    cEvasionStatMod += value;
                    if (cEvasionStatMod > 12) {
                        cEvasionStatMod = 12;
                    } else if (cEvasionStatMod < 0) {
                        cEvasionStatMod = 0;
                    }
                }
            } else if (statChanging == 6) {
                if (cAccuracyStatMod != 12 && cAccuracyStatMod != 0) {
                    cAccuracyStatMod += value;
                    if (cAccuracyStatMod > 12) {
                        cAccuracyStatMod = 12;
                    } else if (cAccuracyStatMod < 0) {
                        cAccuracyStatMod = 0;
                    }
                }
            }
        } else if (affectingEnemy) {
            if (statChanging == 1) {
                if (oAttackStatMod != 12 && oAttackStatMod != 0) {
                    oAttackStatMod += value;
                    if (oAttackStatMod > 12) {
                        oAttackStatMod = 12;
                    } else if (oAttackStatMod < 0) {
                        oAttackStatMod = 0;
                    }
                }
            } else if (statChanging == 2) {
                if (oDefenseStatMod != 12 && oDefenseStatMod != 0) {
                    oDefenseStatMod += value;
                    if (oDefenseStatMod > 12) {
                        oDefenseStatMod = 12;
                    } else if (oDefenseStatMod < 0) {
                        oDefenseStatMod = 0;
                    }
                }
            } else if (statChanging == 3) {
                if (oSpecialStatMod != 12 && oSpecialStatMod != 0) {
                    oSpecialStatMod += value;
                    if (oSpecialStatMod > 12) {
                        oSpecialStatMod = 12;
                    } else if (oSpecialStatMod < 0) {
                        oSpecialStatMod = 0;
                    }
                }
            } else if (statChanging == 4) {
                if (oSpeedStatMod != 12 && oSpeedStatMod != 0) {
                    oSpeedStatMod += value;
                    if (oSpeedStatMod > 12) {
                        oSpeedStatMod = 12;
                    } else if (oSpeedStatMod < 0) {
                        oSpeedStatMod = 0;
                    }
                }
            } else if (statChanging == 5) {
                if (oEvasionStatMod != 12 && oEvasionStatMod != 0) {
                    oEvasionStatMod += value;
                    if (oEvasionStatMod > 12) {
                        oEvasionStatMod = 12;
                    } else if (oEvasionStatMod < 0) {
                        oEvasionStatMod = 0;
                    }
                }
            } else if (statChanging == 6) {
                if (oAccuracyStatMod != 12 && oAccuracyStatMod != 0) {
                    oAccuracyStatMod += value;
                    if (oAccuracyStatMod > 12) {
                        oAccuracyStatMod = 12;
                    } else if (oAccuracyStatMod < 0) {
                        oAccuracyStatMod = 0;
                    }
                }
            }
        }

        cAttack = (int) (currentPokemon.attack * ((double) (statRatios[cAttackStatMod][0] / statRatios[cAttackStatMod][1])));
        cDefense = (int) (currentPokemon.defense * ((double) (statRatios[cDefenseStatMod][0] / statRatios[cDefenseStatMod][1])));
        cSpecial = (int) (currentPokemon.special * ((double) (statRatios[cSpecialStatMod][0] / statRatios[cSpecialStatMod][1])));
        cSpeed = (int) (currentPokemon.speed * ((double) (statRatios[cSpecialStatMod][0] / statRatios[cSpecialStatMod][1])));

        oAttack = (int) (opposingPokemon.attack * ((double) (statRatios[oAttackStatMod][0] / statRatios[oAttackStatMod][1])));
        oDefense = (int) (opposingPokemon.defense * ((double) (statRatios[cDefenseStatMod][0] / statRatios[oDefenseStatMod][1])));
        oSpecial = (int) (opposingPokemon.special * ((double) (statRatios[oSpecialStatMod][0] / statRatios[oSpecialStatMod][1])));
        oSpeed = (int) (opposingPokemon.speed * ((double) (statRatios[oSpeedStatMod][0] / statRatios[oSpeedStatMod][1])));

        if (cAttack > 999) cAttack = 999;
        else if (cAttack < 1) cAttack = 1;
        if (cDefense > 999) cDefense = 999;
        else if (cDefense < 1) cDefense = 1;
        if (cSpecial > 999) cSpecial = 999;
        else if (cSpecial < 1) cSpecial = 1;
        if (cSpeed > 999) cSpeed = 999;
        else if (cSpeed < 1) cSpeed = 1;

        if (oAttack > 999) oAttack = 999;
        else if (oAttack < 1) oAttack = 1;
        if (oDefense > 999) oDefense = 999;
        else if (oDefense < 1) oDefense = 1;
        if (oSpecial > 999) oSpecial = 999;
        else if (oSpecial < 1) oSpecial = 1;
        if (oSpeed > 999) oSpeed = 999;
        else if (oSpeed < 1) oSpeed = 1;
    }

    public void changeStats() {
        cAttack = currentPokemon.attack;
        cDefense = currentPokemon.defense;
        cSpecial = currentPokemon.special;
        cSpeed = currentPokemon.speed;

        oAttack = opposingPokemon.attack;
        oDefense = opposingPokemon.defense;
        oSpecial = opposingPokemon.special;
        oSpeed = opposingPokemon.speed;

        cAttackStatMod = 6;
        cDefenseStatMod = 6;
        cSpecialStatMod = 6;
        cSpeedStatMod = 6;
        cEvasionStatMod = 6;
        cAccuracyStatMod = 6;

        oAttackStatMod = 6;
        oDefenseStatMod = 6;
        oSpecialStatMod = 6;
        oSpeedStatMod = 6;
        oEvasionStatMod = 6;
        oAccuracyStatMod = 6;

        currentPokemon.volatileStatus = "";
        opposingPokemon.volatileStatus = "";

        currentPokemon.protection = "";
        currentPokemon.invulnerable = false;
        currentPokemon.mist = false;

        if (currentPokemon.status.equals("Paralyzed")) {
            cSpeed = currentPokemon.speed / 4;
            if (cSpeed == 0) cSpeed = 1;
        } else if (currentPokemon.status.equals("Burned")) {
            cAttack = currentPokemon.attack / 2;
            if (cAttack == 0) cAttack = 1;
        }
    }

    public void printMenu() {
        System.out.println("********************************");
        System.out.println(opposingPokemon.name + "    " + "Lv: " + opposingPokemon.level);
        String enemyBar = "||||||||||";
        int gone = (int) (((double) opposingPokemon.currenthp / opposingPokemon.hp) * 10);
        for (int i = 0; i < 10 - gone; i++) {
            enemyBar = enemyBar.substring(0, enemyBar.length() - 1);
        }
        if (opposingPokemon.currenthp == 0) {
            enemyBar = "";
        }
        System.out.println("HP: " + enemyBar);
        System.out.println("               " + currentPokemon.name + "    " + "Lv: " + currentPokemon.level);
        String playerBar = "||||||||||";
        gone = (int) (((double) currentPokemon.currenthp / currentPokemon.hp) * 10);
        for (int i = 0; i < 10 - gone; i++) {
            playerBar = playerBar.substring(0, playerBar.length() - 1);
        }
        if (currentPokemon.currenthp == 0) {
            playerBar = "";
        }
        System.out.println("               HP: " + playerBar);
        System.out.println("                   " + currentPokemon.currenthp + "/" + currentPokemon.hp);
        System.out.println("What will " + currentPokemon.returnName() + " do?");
        System.out.println("FIGHT      BAG      POKEMON      RUN");
        System.out.println("********************************");
    }

    public void cPoisonOrBurn() {
        int damage = 0;
        if (currentPokemon.status.equals("bPoisoned")) {
            System.out.println(currentPokemon.name + " is hurt by poison!");
            int sixteenthOfHealth = currentPokemon.hp/16;
            if (sixteenthOfHealth == 0) {
                sixteenthOfHealth = 1;
            }
            damage = cToxicCounter * sixteenthOfHealth;
            cToxicCounter++;
        } else if (currentPokemon.status.equals("Poisoned")) {
            System.out.println(currentPokemon.name + " is hurt by poison!");
            int sixteenthOfHealth = currentPokemon.hp/16;
            if (sixteenthOfHealth == 0) {
                sixteenthOfHealth = 1;
            }
            damage = sixteenthOfHealth;
        } else if (currentPokemon.status.equals("Burned")) {
            System.out.println(currentPokemon.name + " is hurt by its burn!");
            int sixteenthOfHealth = currentPokemon.hp/16;
            if (sixteenthOfHealth == 0) {
                sixteenthOfHealth = 1;
            }
            damage = sixteenthOfHealth;
        }
    }

    public void oPoisonOrBurn() {

    }
    public boolean pUse(Move move, boolean recharged) throws FileNotFoundException {
        boolean attackSelf = false;
        Random random = new Random();
        move.currentpp--;
        MMlastMoveUsed = move;
        movesList ML = new movesList();
        //skipped bide, thrash, and petal dance for now
        //skipped trapped
        //apply charging up moves (Fly, dig, etc.)

        if (currentPokemon.status.equals("Asleep")) {
            currentPokemon.sleepCounter--;
            if (currentPokemon.sleepCounter != 0) {
                System.out.println(currentPokemon.name + " is fast asleep.");
                MMlastMoveUsed = null;
                return false;
            } else {
                System.out.println(currentPokemon.name + " woke up!");
                currentPokemon.status = "";
            }
        } else if (currentPokemon.status.equals("Frozen")) {
            System.out.println(currentPokemon.name + " is frozen.");
            MMlastMoveUsed = null;
            return false;
        } else if (currentPokemon.status.equals("Paralyzed")) {
            if (random.nextInt(4) + 1 == 3) {
                System.out.println(currentPokemon + "is paralyzed!");
                System.out.println("It can't move!");
                currentPokemon.volatileStatus = "";
                return false;
            }
        }

        if (currentPokemon.volatileStatus.equals("Flinched")) {
            System.out.println(currentPokemon.name + " flinched.");
            currentPokemon.volatileStatus = "";
            return false;

        } else if (!recharged) {
            System.out.println(currentPokemon.name + " must recharge.");
            //change it in main code
            return false;
        } else if (currentPokemon.volatileStatus.equals("Confused")) {
            System.out.println(currentPokemon + "is confused!");
            if (random.nextInt(2) + 1 == 2) {
                attackSelf = true;
            }
        }

        if (move.name.equals("Metronome")) {
            System.out.println(currentPokemon.name + " used " + move.name + "!");
            while (move.name.equals("Metronome") || move.name.equals("Struggle")) {
                int movesSize = movesList.moves.size();
                move = movesList.moves.get(random.nextInt(movesSize));
            }
        }

        int tempSpeed = cSpeed / 2;
        boolean critical = false;
        int defStat = 0;
        int attStat = 0;
        int attLevel = currentPokemon.level;
        if (tempSpeed > random.nextInt(255)) {
            critical = true;
        }
        if (move.moveValue == 1) {

            defStat = oDefense;
            attStat = cAttack;
            if (opposingPokemon.protection.equals("Reflect")) {
                defStat = defStat * 2;
            }
            if (attackSelf) {
                attStat = 40;
            }
            if (currentPokemon.status.equals("Burned")) {
                attStat = attStat/2;
            }

            if (defStat >= 256 || attStat >= 256) {
                defStat = defStat / 4;
                defStat = defStat % 256;
                attStat = attStat / 4;
                if (attStat == 0) {
                    attStat = 1;
                }
            }
            if (critical && !attackSelf) {
                defStat = opposingPokemon.defense;
                attLevel = attLevel * 2;
                if (defStat >= 256 || attStat >= 256) {
                    defStat = defStat / 4;
                    defStat = defStat % 256;
                    attStat = attStat / 4;
                    if (attStat == 0) {
                        attStat = 1;
                    }
                }
            }
        } else if (move.moveValue == 2) {
            defStat = oSpecial;
            if (opposingPokemon.protection.equals("Light Screen")) {
                defStat = defStat * 2;
            }
            attStat = cSpecial;
            if (attackSelf) {
                attStat = 40;
            }
            if (critical && !attackSelf) {
                defStat = opposingPokemon.special;
                attLevel = attLevel * 2;
            }
            if (attStat >= 256 || defStat >= 256) {
                attStat = attStat / 4;
                defStat = defStat / 4;
                if (attStat == 0) {
                    attStat = 1;
                }
            }
        }

        if (move.moveValue != 3) {
            attLevel = (((((((attLevel * 2) / 5) + 2) * move.power) * attStat) / defStat) / 50);
            if (attLevel > 997) {
                attLevel = 997;
            }
            attLevel = attLevel + 2;
            if (move.type.equals(currentPokemon.type) || move.type.equals(currentPokemon.secondaryType)) {
                attLevel = attLevel + (attLevel / 2);
            }

            String temp;
            int tempIndex = 100;
            String temp2;
            int temp2Index = 100;
            String temp3;
            int temp3Index = 100;
            for (int i = 0; i < types.length; i++) {
                if (move.type.equals(types[i])) {
                    temp = types[i];
                    tempIndex = i;
                }
                if (opposingPokemon.type.equals(types[i])) {
                    temp2 = types[i];
                    temp2Index = i;
                }

                if (opposingPokemon.secondaryType.equals(types[i])) {
                    temp3 = types[i];
                    temp3Index = i;
                }

            }

            if (!attackSelf) {
                System.out.println(currentPokemon.name + " used " + move.name + "!");
            }
            if (!attackSelf && !move.name.equals("Super Fang") && !move.name.equals("Seismic Toss") && !move.name.equals("Night Shade")) {
                if (genITypeChart[tempIndex][temp2Index].equals("str")) {
                    attLevel = attLevel * 20;
                    attLevel = attLevel / 10;
                    System.out.println("It's super effective!");
                } else if (genITypeChart[tempIndex][temp2Index].equals("weak")) {
                    attLevel = attLevel * 5;
                    attLevel = attLevel / 10;
                    System.out.println("It's not very effective...");
                } else if (genITypeChart[tempIndex][temp2Index].equals("inef")) {
                    System.out.println("It doesn't affect Foe " + opposingPokemon.name);
                    return false;
                } else if (!opposingPokemon.secondaryType.equals(("None")) && genITypeChart[tempIndex][temp3Index].equals("str")) {
                    attLevel = attLevel * 20;
                    attLevel = attLevel / 10;
                    System.out.println("It's super effective!");
                } else if (!opposingPokemon.secondaryType.equals(("None")) && genITypeChart[tempIndex][temp3Index].equals("weak")) {
                    attLevel = attLevel * 5;
                    attLevel = attLevel / 10;
                    System.out.println("It's not very effective...");
                } else if (!opposingPokemon.secondaryType.equals(("None")) && genITypeChart[tempIndex][temp3Index].equals("inef")) {
                    System.out.println("It doesn't affect Foe " + opposingPokemon.name);
                    return false;
                }
            }

            if (attLevel == 0) {
                System.out.println(currentPokemon.name + "'s attack missed!");
                //clear trap
                return false;
            }

            if (attLevel != 1 && !attackSelf) {
                int rando = random.nextInt(255) + 217;
                attLevel = attLevel * rando;
                attLevel = attLevel / 255;
            }
        }

        if (!attackSelf) {
            if (move.name.equals("Counter")) {
                if (lastMoveUsed.power != 0 && !lastMoveUsed.type.equals("Normal") && !lastMoveUsed.type.equals("Fighting") && !lastMoveUsed.name.equals("Counter") && damageLastMoveDid != 0) {
                    attLevel = damageLastMoveDid * 2;
                } else {
                    System.out.println(currentPokemon.name + "'s attack missed!");
                    return false;
                }
            } else if (move.name.equals("Mirror Move")) {
                if (MMlastMoveUsed != null && !MMlastMoveUsed.name.equals("Mirror Move")) {
                    attLevel = damageLastMoveDid;
                } else {
                    System.out.println(currentPokemon.name + "'s attack missed!");
                }
            } else if (move.name.equals("Super Fang")) {
                attLevel = opposingPokemon.currenthp / 2;
            } else if (move.name.equals("Seismic Toss") || move.name.equals("Night Shade")) {
                attLevel = opposingPokemon.level;
            } else if (move.name.equals("Dragon Rage")) {
                attLevel = 40;
            } else if (move.name.equals("Psywave")) {
                int randomN = 0;
                while (randomN == 0 || randomN > (currentPokemon.level + (currentPokemon.level / 2))) {
                    randomN = random.nextInt(255);
                }
                attLevel = randomN;
            }
        }


        if (attLevel > opposingPokemon.currenthp) {
            attLevel = opposingPokemon.currenthp;
        }


        if (move.accuracy != 0) {
            if (move.name.equals("Dream Eater") && !opposingPokemon.status.equals("Asleep")) {
                System.out.println(currentPokemon.name + "'s attack missed!");
                //clear trap
                return false;
            } else if (opposingPokemon.invulnerable) {
                System.out.println(currentPokemon.name + "'s attack missed!");
                //clear trap
                return false;
            } else if (opposingPokemon.mist && (move.name == "Growl" || move.name == "Tail Whip" || move.name == "Leer" || move.name == "String Shot" || move.name == "Sand Attack" || move.name == "Smokescreen" || move.name == "Kinesis" || move.name == "Flash" || move.name == "Screech")) {
                System.out.println(currentPokemon.name + "'s attack missed!");
                //clear trap
                return false;
            }
            int acc = ((int) (move.accuracy * 255));
            acc = (int) (acc * ((double) statRatios[cAccuracyStatMod][0] / statRatios[cAccuracyStatMod][1]));
            int tempEvasion = 6;
            if (oEvasionStatMod > 6) {
                tempEvasion = oEvasionStatMod - 6;
                tempEvasion = oEvasionStatMod - (tempEvasion * 2);
            } else if (oEvasionStatMod < 6) {
                tempEvasion = oEvasionStatMod - 6;
                tempEvasion = oEvasionStatMod + (tempEvasion * 2);
            }

            acc = (int) (acc * ((double) statRatios[tempEvasion][0] / statRatios[tempEvasion][1]));
            if (acc > 255) {
                acc = 255;
            } else if (acc < 1) {
                acc = 1;
            }

            if (acc <= random.nextInt(255)) {
                System.out.println(currentPokemon.name + "'s attack missed!");
                //clear trap status
                return false;
            }
        }

        if (attackSelf) {
            System.out.println(currentPokemon.name + " used " + move.name + "!");
            System.out.println("It hurt itself in its confusion!");
            //calculate substitute health
            currentPokemon.currenthp = currentPokemon.currenthp - attLevel;
            return true;
        }

        if (critical) {
            System.out.println("A critical hit!");
        }

        if (move.name.equals("Hypnosis") || move.name.equals("Spore") || move.name.equals("Lovely Kiss") || move.name.equals("Sleep Powder") || move.name.equals("Sing")) {
            previousOppMove = null;
            if (!opposingPokemon.status.equals("")) {
                System.out.println("But, it failed!");
            } else {
                opposingPokemon.status = "Asleep";
                System.out.println("Foe " + opposingPokemon.name + " is fast asleep.");
                opposingPokemon.sleepCounter = random.nextInt(7)+1;
            }
        } else if (move.name.equals("Poison Gas") || move.name.equals("Poison Powder") || move.name.equals("Toxic")) {
            //cant poison substitute
            if (opposingPokemon.type.equals("Poison") || opposingPokemon.secondaryType.equals("Poison") || !opposingPokemon.status.equals("")) {
                System.out.println("But, it failed!");
            } else {
                System.out.println("Foe " + opposingPokemon.name + " is badly poisoned.");
                opposingPokemon.status = "bPoisoned";
            }
        }
        damageLastMoveDid = attLevel;
        lastMoveUsed = move;
        opposingPokemon.currenthp = opposingPokemon.currenthp - attLevel;
        return true;

    }

    public boolean oUse(Move move, boolean recharged) {
        if (opposingPokemon.status.equals("Asleep")) {
            System.out.println(opposingPokemon.name + " is fast asleep.");
            return false;
        } else if (opposingPokemon.status.equals("Frozen")) {
            System.out.println(opposingPokemon.name + " is frozen.");
            return false;
        }
        if (opposingPokemon.volatileStatus.equals("Flinched")) {
            System.out.println(opposingPokemon.name + " flinched.");
            opposingPokemon.volatileStatus = "";
            return false;
        } else if (opposingPokemon.volatileStatus.equals("Trapped")) {
            System.out.println(opposingPokemon.name + " is trapped.");
            opposingPokemon.volatileStatus = "";
            return false;
        } else if (!recharged) {
            System.out.println(opposingPokemon.name + " must recharge.");
            //change it in main code
            return false;
        }

        int tempSpeed = oSpeed / 2;
        Random random = new Random();
        boolean critical = false;
        int defStat = 0;
        int attStat = 0;
        int attLevel = opposingPokemon.level;
        if (tempSpeed > random.nextInt(255)) {
            critical = true;
        }
        if (move.moveValue == 1) {

            defStat = cDefense;
            attStat = oAttack;
            if (defStat >= 256 || attStat >= 256) {
                defStat = defStat / 4;
                defStat = defStat % 256;
                attStat = attStat / 4;
                if (attStat == 0) {
                    attStat = 1;
                }
            }
            if (critical) {
                defStat = currentPokemon.defense;
                attLevel = attLevel * 2;
                if (defStat >= 256 || attStat >= 256) {
                    defStat = defStat / 4;
                    defStat = defStat % 256;
                    attStat = attStat / 4;
                    if (attStat == 0) {
                        attStat = 1;
                    }
                }
            }
        } else if (move.moveValue == 2) {
            defStat = cSpecial;
            attStat = oSpecial;
            if (critical) {
                defStat = currentPokemon.special;
                attLevel = attLevel * 2;
            }
            if (attStat >= 256 || defStat >= 256) {
                attStat = attStat / 4;
                defStat = defStat / 4;
                if (attStat == 0) {
                    attStat = 1;
                }
            }
        }

        if (move.moveValue != 3) {
            attLevel = (((((((attLevel * 2) / 5) + 2) * move.power) * attStat) / defStat) / 50);
            if (attLevel > 997) {
                attLevel = 997;
            }
            attLevel = attLevel + 2;
            if (move.type.equals(opposingPokemon.type) || move.type.equals(opposingPokemon.secondaryType)) {
                attLevel = attLevel + (attLevel / 2);
            }

            String temp;
            int tempIndex = 100;
            String temp2;
            int temp2Index = 100;
            String temp3;
            int temp3Index = 100;
            for (int i = 0; i < types.length; i++) {
                if (move.type.equals(types[i])) {
                    temp = types[i];
                    tempIndex = i;
                }
                if (currentPokemon.type.equals(types[i])) {
                    temp2 = types[i];
                    temp2Index = i;
                }
                if (currentPokemon.secondaryType.equals(types[i])) {
                    temp3 = types[i];
                    temp3Index = i;
                }
            }

            if (genITypeChart[tempIndex][temp2Index].equals("str")) {
                attLevel = attLevel * 20;
                attLevel = attLevel / 10;
                System.out.println("It's super effective!");
            } else if (genITypeChart[tempIndex][temp2Index].equals("weak")) {
                attLevel = attLevel * 5;
                attLevel = attLevel / 10;
                System.out.println("It's not very effective...");
            } else if (genITypeChart[tempIndex][temp2Index].equals("inef")) {
                System.out.println("It doesn't affect " + currentPokemon.name);
                return false;
            } else if (!currentPokemon.secondaryType.equals(("None")) && genITypeChart[tempIndex][temp3Index].equals("str")) {
                attLevel = attLevel * 20;
                attLevel = attLevel / 10;
                System.out.println("It's super effective!");
            } else if (!currentPokemon.secondaryType.equals(("None")) && genITypeChart[tempIndex][temp3Index].equals("weak")) {
                attLevel = attLevel * 5;
                attLevel = attLevel / 10;
                System.out.println("It's not very effective...");
            } else if (!currentPokemon.secondaryType.equals(("None")) && genITypeChart[tempIndex][temp3Index].equals("inef")) {
                System.out.println("It doesn't affect " + currentPokemon.name);
                return false;
            }

            if (attLevel == 0) {
                System.out.println("Foe" + opposingPokemon.name + "'s attack missed!");
                //clear trap
                return false;
            }

            if (attLevel != 1) {
                int rando = random.nextInt(255) + 217;
                attLevel = attLevel * rando;
                attLevel = attLevel / 255;
                if (attLevel > currentPokemon.currenthp) {
                    attLevel = currentPokemon.currenthp;
                }
            }

            System.out.println("Foe " + opposingPokemon.name + " used " + move.name + "!");
            currentPokemon.currenthp = currentPokemon.currenthp - attLevel;
            return true;

        }
        return true; //REMOVE THIS LATER
    }

    public void addEffects() {

    }

    public boolean playerGoesFirst(Move pMove, Move oMove) {
        if (pMove.name.equals("Quick Attack") && !oMove.name.equals("Quick Attack")) {
            return true;
        } else if (oMove.name.equals("Quick Attack") && !pMove.name.equals("Quick Attack")) {
            return false;
        } else if (pMove.name.equals("Counter") && !oMove.name.equals("Counter")) {
            return false;
        } else if (oMove.name.equals("Counter") && !pMove.name.equals("Counter")) {
            return true;
        } else {
            if (cSpeed > oSpeed) {
                return true;
            } else {
                return false;
            }
        }
    }
}

class personBattle extends Battle {
    ArrayList<Pokemon> trainerParty;
    String trainerName;
    String trainerType;
    int trainerTypeValue;
    boolean battling = true;

    public personBattle(ArrayList<Pokemon> playerParty, ArrayList<Pokemon> trainerParty, String trinerName, int trainerTypeValue) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        this.trainerTypeValue = trainerTypeValue;
        if (trainerTypeValue == 1) trainerType = "TRAINER ";
        else if (trainerTypeValue == 2) trainerType = "TRAINER ";
        else if (trainerTypeValue == 3) trainerType = "TRAINER ";
        else if (trainerTypeValue == 4) trainerType = "TRAINER ";
        else if (trainerTypeValue == 5) trainerType = "RIVAL ";
        else if (trainerTypeValue == 6) trainerType = "RIVAL ";
        else if (trainerTypeValue == 7) trainerType = "RIVAL ";
        else if (trainerTypeValue == 8) trainerType = "RIVAL ";
        else if (trainerTypeValue == 9) trainerType = "GYM LEADER ";
        else if (trainerTypeValue == 10) trainerType = "GYM LEADER ";
        else if (trainerTypeValue == 11) trainerType = "GYM LEADER ";
        else if (trainerTypeValue == 12) trainerType = "GYM LEADER ";
        this.playerParty = playerParty;
        this.trainerParty = trainerParty;
        this.trainerName = trinerName;
        System.out.println(trainerType + trainerName + " would like to battle!");
        currentPokemon = playerParty.get(0);
        opposingPokemon = trainerParty.get(0);
        System.out.println(trainerType + trainerName + " sent out " + opposingPokemon.returnName() + "!");
        System.out.println("Go! " + currentPokemon.returnName() + "!");
        changeStats();
        Move movePicked = null;
        boolean previousHit = true;
        boolean previousOppHit = true;
        while (battling) {
            printMenu();
            String response = input.nextLine();
            response = response.toUpperCase();
            if (response.equals("FIGHT")) {
                System.out.println("Type the index of the move you would like to select.");
                System.out.println("Type B to go back.");
                System.out.println("MOVES: " + "\n" + currentPokemon.printMoves());
                String response2 = input.nextLine();
                response2 = response2.toLowerCase();
                boolean picked = false;
                int ppSum = 0;
                for (Move move : currentPokemon.moves) {
                    if (move != null) {
                        ppSum += move.currentpp;
                    }
                }
                if (ppSum == 0) {
                    picked = true;
                    System.out.println(currentPokemon.name + " has no moves left!");
                    movePicked = movesList.findMoveByName("Struggle");
                }
                while (!picked) {
                    if (response2.equals("1")) {
                        if (currentPokemon.moves[0].currentpp != 0) {
                            movePicked = currentPokemon.moves[0];
                            picked = true;
                        } else {
                            System.out.println("There's no PP left for this move!");
                        }
                    } else if (response2.equals("2")) {
                        if (currentPokemon.moves[1] != null) {
                            if (currentPokemon.moves[1].currentpp != 0) {
                                movePicked = currentPokemon.moves[1];
                                picked = true;
                            } else {
                                System.out.println("There's no PP left for this move!");
                            }
                        }
                    } else if (response2.equals("3")) {
                        if (currentPokemon.moves[2] != null) {
                            if (currentPokemon.moves[2].currentpp != 0) {
                                movePicked = currentPokemon.moves[2];
                                picked = true;
                            } else {
                                System.out.println("There's no PP left for this move!");
                            }
                        }
                    } else if (response2.equals("4")) {
                        if (currentPokemon.moves[3] != null) {
                            if (currentPokemon.moves[3].currentpp != 0) {
                                movePicked = currentPokemon.moves[3];
                                picked = true;
                            } else {
                                System.out.println("There's no PP left for this move!");
                            }
                        }
                    } else if (response2.equals("b")) {
                        picked = true;
                    } else {
                        System.out.println("Type the index of the move you would like to select.");
                        System.out.println("Type B to go back.");
                        response2 = input.nextLine();
                        response2 = response2.toLowerCase();
                    }
                }
                if (!response2.equals("b")) {
                    Move oppMove = chooseMoveForOpp();
                    if (oppMove != null) {
                        if (playerGoesFirst(movePicked, oppMove)) {
                            if (previousMovePicked != null) {
                                if (previousMovePicked.name == "Hyper Beam" && previousHit) {
                                    previousHit = pUse(movePicked, false);
                                } else {
                                    previousHit = pUse(movePicked, true);
                                }
                            }
                            previousMovePicked = movePicked;
                            if (opposingPokemon.currenthp == 0) {
                                //put kill check here
                            } else {
                                if (previousOppMove != null) {
                                    if (previousOppMove.name == "Hyper Beam" && previousOppHit) {
                                        previousOppHit = oUse(oppMove, false);
                                    } else {
                                        previousOppHit = oUse(oppMove, true);
                                    }
                                }
                            }
                            previousOppMove = oppMove;
                        } else {
                            if (previousOppMove != null) {
                                if (previousOppMove.name == "Hyper Beam" && previousOppHit) {
                                    previousOppHit = oUse(oppMove, false);
                                } else {
                                    previousOppHit = oUse(oppMove, true);
                                }
                            }
                            previousOppMove = oppMove;
                            if (currentPokemon.currenthp == 0) {
                                //put kill check here
                            } else {
                                if (previousMovePicked != null) {
                                    if (previousMovePicked.name == "Hyper Beam" && previousHit) {
                                        previousHit = pUse(movePicked, false);
                                    } else {
                                        previousHit = pUse(movePicked, true);
                                    }
                                }
                                previousMovePicked = movePicked;
                            }
                        }
                    }
                }
            }
        }
    }

    public Move chooseMoveForOpp() {
        int ppSum = 0;
        Move chosen = opposingPokemon.moves[0];
        for (Move move : opposingPokemon.moves) {
            if (move != null) {
                ppSum += move.currentpp;
            }
        }
        if (ppSum == 0) {
            System.out.println(opposingPokemon.name + " has no moves left!");
            return movesList.findMoveByName("Struggle");
        }

        if (trainerTypeValue == 1 || trainerTypeValue == 5 || trainerTypeValue == 9) {
            int statusCount = 0;
            int damageCount = 0;
            Random random = new Random();
            for (Move move : opposingPokemon.moves) {
                if (move != null) {
                    if (move.moveValue == 1 || move.moveValue == 2) {
                        damageCount++;
                    } else {
                        statusCount++;
                    }
                }
            }
            if (currentPokemon.status.equals("")) {
                statusCount = 0;
            }


            if (statusCount == 0) {
                int gen = random.nextInt(2) + 1;
                if (gen == 1) {
                    for (Move move : opposingPokemon.moves) {
                        if (move != null) {
                            if (move.power > chosen.power) {
                                chosen = move;
                            }
                        }
                    }
                } else {
                    if (statusCount > 1) {
                        int gen2 = random.nextInt(statusCount) + 1;
                        int count = 0;
                        for (Move move : opposingPokemon.moves) {
                            if (move.moveValue == 3) {
                                count++;
                                if (count == gen2) {
                                    chosen = move;
                                }
                            }
                        }
                    } else {
                        for (Move move : opposingPokemon.moves) {
                            if (move != null) {
                                if (move.moveValue == 3) {
                                    chosen = move;
                                }
                            }
                        }
                    }
                }
            }
            if (chosen.name.equals("Substitute")) {
                if (opposingPokemon.currenthp < (0.25 * opposingPokemon.hp))
                    for (Move move : opposingPokemon.moves) {
                        if (statusCount > 1) {
                            if (!move.name.equals("Substitute") && move.moveValue == 3) {
                                chosen = move;
                            }
                        } else {
                            if (move.power > chosen.power) {
                                chosen = move;
                            }
                        }
                    }
            }
        } else if (trainerTypeValue == 2 || trainerTypeValue == 6 || trainerTypeValue == 10) {

        }
        return chosen;
    }
}
