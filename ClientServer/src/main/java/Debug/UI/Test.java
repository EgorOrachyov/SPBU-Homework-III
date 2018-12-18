package Debug.UI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import Filter.Image;
import UI.ImageSaver;
import UI.ImageViewer;

public class Test extends JFrame {

    public Test() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("help");
        JMenuItem item1 = new JMenuItem("how to");
        JMenuItem item2 = new JMenuItem("about");
        item1.addActionListener((event) -> {System.out.println(event.getActionCommand());});
        item2.addActionListener((event) -> {System.out.println(event.getActionCommand());});
        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JButton button1 = new JButton("action 1");
        JButton button2 = new JButton("action 2");
        button1.addActionListener((event) -> {System.out.println(event.getActionCommand());});
        button2.addActionListener((event) -> {System.out.println(event.getActionCommand());});
        panel1.add(button1);
        panel1.add(button2);

        String[] items = {"item 1","item 2","item 3"};
        JComboBox box = new JComboBox(items);
        box.addActionListener((event) -> {System.out.println(event.getActionCommand());});
        box.addItemListener((event) -> {System.out.println(event.paramString());});
        panel2.add(box);

        setTitle("Test");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(BorderLayout.NORTH, menuBar);
        getContentPane().add(BorderLayout.SOUTH, panel1);
        getContentPane().add(BorderLayout.CENTER,  panel2);
        setVisible(true);

        try {
            Image image = new Image("src/main/java/Debug/Images/test2.jpg");
            ///new ImageViewer(image);
            new ImageSaver(image);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String ... args) {
        Test test = new Test();
    }

}
