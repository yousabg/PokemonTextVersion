import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class ItemsList {
    private static final ArrayList<Item> items = new ArrayList<>();
    public ItemsList() throws FileNotFoundException {
        addItems();
    }

    static void addItems() throws FileNotFoundException {
        Scanner types = new Scanner(new File("src/listOfItemTypes.txt"));
        Scanner names = new Scanner(new File("src/listOfItemNames.txt"));
        Scanner descriptions = new Scanner(new File("src/listOfItemDescription.txt"));
        Scanner prices = new Scanner(new File("src/listOfItemPrice.txt"));
        while (types.hasNext() && names.hasNext() && descriptions.hasNext() && prices.hasNext())
        {
            int type = types.nextInt();
            String name = names.nextLine();
            String description = descriptions.nextLine();
            int price = prices.nextInt();
            items.add(new Item(type,name,description,price));
        }
    }

    static String returnITEMS(ArrayList<Item> bag)
    {
        int count = 1;
        String ITEMS = "";
        for (Item item: bag)
        {
            if (item.returnType() == 1)
            {
                ITEMS += count + ": " + item.returnName() + "\n";
                count++;
            }
        }
        return ITEMS;
    }

    static String returnKEYITEMS(ArrayList<Item> bag)
    {
        int count = 1;
        String KEYITEMS = "";
        for (Item item: bag)
        {
            if (item.returnType() == 2)
            {
                KEYITEMS += count + ": " + item.returnName() + "\n";
                count++;
            }
        }
        return KEYITEMS;
    }

    static String returnPOKEBALLS(ArrayList<Item> bag)
    {
        int count = 1;
        String POKEBALLS = "";
        for (Item item: bag)
        {
            if (item.returnType() == 3)
            {
                POKEBALLS += count + ": " + item.returnName() + "\n";
                count++;
            }
        }
        return POKEBALLS;
    }
}
