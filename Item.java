import java.io.Serializable;

public class Item implements Serializable{
    String name = "";
    String type = "";
    int typeValue;
    String description = "";
    int price = 0;
    public Item(int typeValue, String name, String description, int price)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.typeValue = typeValue;
        if (typeValue == 1)
        {
            type = "ITEMS";
        } else if (typeValue == 2)
        {
            type = "KEY ITEMS";
        } else if (typeValue == 3)
        {
            type = "POKE BALLS";
        }
    }

    public int returnType()
    {
        return typeValue;
    }

    public String returnName()
    {
        return name;
    }
}
