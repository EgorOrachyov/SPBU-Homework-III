package Debug.UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Test extends JFrame {

    public Test() {

        JPanel panel = new JPanel();
        JButton button = new JButton("exit");
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
                System.exit(0);
            }
        });
        panel.add(button);
        try {
            panel.add(new JLabel(new ImageIcon((new Filter.Image("src/main/java/Debug/Images/server3.png")).getData())));
        } catch (IOException e) {

        }
        add(panel);

        setTitle("Test");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String ... args) {
        Test test = new Test();
        test.setVisible(true);
    }

}
