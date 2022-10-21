import java.io.Serializable;

public class Move implements Serializable{
    String name;
    String type;
    int moveValue;
    int pp;
    int currentpp;
    int power;
    double accuracy;

    public Move(String name, String type, int moveValue, int pp, int power, double accuracy) {
        this.name = name;
        this.type = type;
        this.moveValue = moveValue;
        this.pp = pp;
        currentpp = pp;
        this.power = power;
        this. accuracy = accuracy;
    }

    public String returnName()
    {
        return name;
    }
}