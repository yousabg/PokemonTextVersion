import javax.swing.*;
public class ImageLoader extends JFrame {
    JFrame frame;
    JLabel displayField;
    ImageIcon image;

    public ImageLoader(String imageName)
    {
        frame = new JFrame("Image Display Text");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            image = new ImageIcon(getClass().getResource(imageName));
            displayField = new JLabel(image);
            frame.add(displayField);
        }
        catch(Exception e){
            System.out.println("Image cannot be found!");
        }
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}