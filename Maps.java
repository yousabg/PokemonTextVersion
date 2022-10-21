import java.util.ArrayList;
import java.util.List;
public class Maps {
    private ArrayList<PMap> maps;
    public Maps()
    {
         maps = new ArrayList<PMap>();
        addMaps();
    }

    public void addMaps()
    {
        PMap URIMH = new PMap("You are in your room.","the upper room in your mom's house");
        maps.add(URIMH);
        PMap MRIMH = new PMap("You are in the living room of your house.", "the main room in your mom's house");
        maps.add(MRIMH);
        URIMH.addExit("down stairs", MRIMH);
        MRIMH.addExit("up stairs", URIMH);
        PMap PT = new PMap("You are in Pallet Town", "Pallet Town");
        maps.add(PT);
        MRIMH.addExit("outside", PT);
        PT.addExit("inside mom's house", MRIMH);
        PMap yousabsLab = new PMap("You are in Prof. Yousab's lab.", "prof yousab's lab");
        maps.add(yousabsLab);
        PT.addExit("inside prof yousab's lab", yousabsLab);
        yousabsLab.addExit("outside", PT);
        PMap route1 = new PMap("You are in route 1.","route 1");
        maps.add(route1);
        PT.addExit("north", route1);
    }

    public ArrayList<PMap> getAllMaps()
    {
        return maps;
    }

    public PMap getMap(String mapName)
    {
        for (int i = 0; i< maps.size(); i++)
        {
            if (maps.get(i).getName().contains(mapName))
            {
                return maps.get(i);
            }
        }
        return null;
    }

}
