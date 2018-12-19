package UI;

import Client.AsyncClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ConnectionSetup extends JFrame {

    JLabel status;
    JTextField host;
    JTextField port;

    public ConnectionSetup() {

        setLayout(null);

        status = new JLabel();
        status.setText(
                "<html>" +
                        "Note: Write server host and port<br>" +
                        "Status: disconnected" +
                "</html>"
        );
        status.setBounds(10, 10, 230, 30);

        host = new JTextField("localhost");
        host.setBounds(50, 50, 190, 20);

        port = new JTextField("8813");
        port.setBounds(50, 80, 190, 20);

        JLabel labelHost = new JLabel("Host");
        labelHost.setBounds(10, 50, 30, 20);

        JLabel labelPort = new JLabel("Port");
        labelPort.setBounds(10, 80, 30, 20);

        JButton connect = new JButton("Connect");
        connect.setBounds(10, 110, 110, 20);
        connect.addActionListener(new ActionConnect());

        JButton cancel = new JButton("Cancel");
        cancel.setBounds(130, 110, 110, 20);
        cancel.addActionListener(new ActionCancel());

        add(status);
        add(host);
        add(port);
        add(labelHost);
        add(labelPort);
        add(connect);
        add(cancel);

        setTitle("Setup connection");
        setResizable(false);
        setSize(250, 170);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private class ActionConnect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                final String h = host.getText();
                final int p = Integer.valueOf(port.getText());

                AsyncClient client = new AsyncClient(h, p);
                new Menu(client);
                dispose();
            }
            catch (IOException exception) {
                //exception.printStackTrace();
                status.setText(
                        "<html>" +
                                "Note: Write server host and port<br>" +
                                "Status: CANNOT CONNECT" +
                        "</html>"
                );
            }
        }
    }

    private class ActionCancel implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }

    }

    public static void main(String ... args) {

        new ConnectionSetup();

    }

}
