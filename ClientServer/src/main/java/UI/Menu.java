package UI;

import Client.AsyncClient;
import Client.FilterTask;
import Filter.FilterInfo;
import Filter.Image;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Menu extends JFrame {

    JProgressBar progress;
    JTextField path;
    JLabel processing;

    JComboBox filters;
    JComboBox results;

    JButton send;
    JButton preview;
    JButton clean;
    JButton cancel;

    JButton view;
    JButton save;
    JButton exit;

    AsyncClient client;
    ArrayList<Image> images;
    ConcurrentLinkedQueue<FilterTask> filterTasks;

    public Menu(AsyncClient client) {

        this.client = client;
        this.images = new ArrayList<>(4);
        this.filterTasks = client.getCompletedTasks();

        JLabel loadNote = new JLabel();
        loadNote.setText(
                "<html>" +
                    "Note: Write image full name <br>" +
                    "and choose desired filter from list" +
                "</html>"
        );
        loadNote.setBounds(10, 10, 430, 30);

        JLabel pathNote = new JLabel("Path");
        pathNote.setBounds(10, 50, 50, 20);

        path = new JTextField("Enter path to the image...");
        path.setBounds(70, 50, 370, 20);

        JLabel filtersNote = new JLabel("Filters");
        filtersNote.setBounds(10, 80, 50, 20);

        filters = new JComboBox();
        filters.setBounds(70, 80, 370, 20);
        filters.setEnabled(false);

        {
            ArrayList<FilterInfo> info = client.getFilters();
            if (info != null) {
                for (int i = 0; i < info.size(); i++) {
                    filters.addItem(info.get(i).getName());
                }
            }
            filters.setEnabled(true);
        }

        send = new JButton("Send");
        send.setBounds(10, 110, 100, 20);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filters.getItemCount() <= 0) {
                    processing.setText(
                            "<html>" +
                                "Progress: <br>" +
                                "No filter is available" +
                            "</html>"
                    );
                    return;
                }

                try {
                    Image image = new Image(path.getText());
                    client.submitTask(new FilterTask(image, filters.getSelectedIndex()));

                    send.setEnabled(false);
                    preview.setEnabled(false);
                    clean.setEnabled(false);
                    progress.setEnabled(true);

                    processing.setText(
                            "<html>" +
                                "Progress: <br>" +
                                "Load image and send to server" +
                            "</html>"
                    );
                }
                catch (IOException exception) {
                    processing.setText(
                            "<html>" +
                                "Progress: <br>" +
                                "Could not load image" +
                            "</html>"
                    );
                }
            }
        });

        preview = new JButton("Preview");
        preview.setBounds(120, 110, 100, 20);
        preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Image image = new Image(path.getText());
                    new ImageViewer(image);
                }
                catch (IOException exception) {
                    processing.setText(
                            "<html>" +
                                "Progress: <br>" +
                                "Could not load image" +
                            "</html>"
                    );
                }
            }
        });

        clean = new JButton("Clean");
        clean.setBounds(230, 110, 100, 20);
        clean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                path.setText("");
                processing.setText(
                        "<html>" +
                            "Progress: <br>" +
                            "Image is not loaded" +
                        "</html>"
                );
            }
        });

        cancel = new JButton("Cancel");
        cancel.setBounds(340, 110, 100, 20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.cancelTask();

                send.setEnabled(true);
                preview.setEnabled(true);
                clean.setEnabled(true);
                progress.setEnabled(false);

                path.setText("");
                processing.setText(
                        "<html>" +
                            "Progress: <br>" +
                            "Image is not loaded" +
                        "</html>"
                );
            }
        });

        processing = new JLabel();
        processing.setText(
                "<html>" +
                    "Progress: <br>" +
                    "Image is not loaded" +
                "</html>"
        );
        processing.setBounds(10, 140, 430, 30);

        progress = new JProgressBar();
        progress.setMinimum(0);
        progress.setMaximum(100);
        progress.setStringPainted(true);
        progress.setValue(0);
        progress.setEnabled(false);
        progress.setBounds(10, 180, 430, 20);

        JLabel resultNote = new JLabel();
        resultNote.setText(
                "<html>" +
                    "Note: Here stored all filtered images for session <br>" +
                    "Choose the one to save it or preview" +
                "</html>"
        );
        resultNote.setBounds(10, 210, 430, 30);

        JLabel resultsNote = new JLabel("Results");
        resultsNote.setBounds(10, 250, 50, 20);

        results = new JComboBox();
        results.setEnabled(false);
        results.setBounds(70, 250, 370, 20);

        view = new JButton("View");
        view.setBounds(10, 280, 155, 20);
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (images.size() > 0) {
                    new ImageViewer(images.get(results.getSelectedIndex()));
                }
            }
        });

        save = new JButton("Save");
        save.setBounds(175, 280, 155, 20);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (images.size() > 0) {
                    new ImageSaver(images.get(results.getSelectedIndex()));
                }
            }
        });

        exit = new JButton("Exit");
        exit.setBounds(340, 280, 100, 20);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.done(true);
                dispose();
            }
        });

        JLabel about = new JLabel("2018, Egor Orachyov (c)");
        about.setBounds(10, 310, 200, 20);

        setLayout(null);

        add(loadNote);
        add(path);
        add(pathNote);
        add(filters);
        add(filtersNote);
        add(send);
        add(preview);
        add(clean);
        add(cancel);
        add(processing);
        add(progress);
        add(resultNote);
        add(resultsNote);
        add(results);
        add(view);
        add(save);
        add(exit);
        add(about);

        setTitle("Filters");
        setSize(450, 360);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.done(true);
                e.getWindow().dispose();
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (progress.isEnabled()) {
                        progress.setValue(client.getCurrentTaskProgress());
                    }

                    FilterTask task = filterTasks.poll();
                    if (task != null) {
                        images.add(task.getSource());
                        results.setEnabled(true);
                        results.addItem("Filtered image " + String.valueOf(results.getItemCount()));

                        send.setEnabled(true);
                        preview.setEnabled(true);
                        clean.setEnabled(true);
                        progress.setEnabled(false);

                        processing.setText(
                                "<html>" +
                                    "Progress: <br>" +
                                    "Image is not loaded" +
                                "</html>"
                        );
                    }
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    // src/main/java/Debug/Images/test1.png
    // src/main/java/Debug/Images/test2.jpg

}
