package UI;

import Filter.Image;

import javax.swing.*;

public class ImageViewer extends JFrame {

    public ImageViewer(Image toView) {

        add(new JLabel(new ImageIcon(toView.getData())));

        setTitle("View");
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(toView.getWidth(), toView.getHeight());
        setLocationRelativeTo(null);
        setVisible(true);

    }

}
