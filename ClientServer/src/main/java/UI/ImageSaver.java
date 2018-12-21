package UI;

import Filter.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ImageSaver extends JFrame {

    private Image result;
    private JTextField path;

    public ImageSaver(Image toSave) {
        this.result = toSave;

        JButton save;
        JButton clean;
        JButton cancel;
        JLabel image;
        JLabel note;

        note = new JLabel();
        note.setText(
                "<html>" +
                        "Note: Write FULL path to file with<br>" +
                        "preferred extension for image format" +
                "</html>"
        );
        note.setBounds(10, 10, 320, 30);

        path = new JTextField("Enter file name...", 1024);
        path.setBounds(10, 50, 320, 20);

        save = new JButton("Save");
        save.addActionListener(new ActionSave());
        save.setBounds(10, 80, 100, 20);

        clean = new JButton("Clean");
        clean.addActionListener(new ActionClean());
        clean.setBounds(120, 80, 100, 20);

        cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionCancel());
        cancel.setBounds(230, 80, 100, 20);

        image = new JLabel(new ImageIcon(toSave.getData()));
        image.setBounds(10, 110, 320, 200);

        setLayout(null);

        add(note);
        add(path);
        add(save);
        add(clean);
        add(cancel);
        add(image);

        setTitle("Save result");
        setSize(340, 340);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private class ActionSave implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String fileName = path.getText();

            try {
                result.saveImage(fileName);
                dispose();
            }
            catch (IOException exception) {
                path.setText("Wrong format of file...");
                exception.printStackTrace();
            }
        }

    }

    private class ActionClean implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            path.setText("");
        }
    }

    private class ActionCancel implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }

    }

}
