package org.example;

import lombok.Data;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Data
public class BottomPanel extends JPanel {

    private JButton clientBtn;
    private JButton serverBtn;
    private JTextField ipField;
    private JTextField portField;
    private JButton readyBtn;
    private JTextArea chatArea;
    private JTextField msgField;
    private JButton sendBtn;
    private JTextField usernameField;
    private JButton applyBtn;
    Font font = new Font("Helvetica", Font.PLAIN, 22);

    public BottomPanel(Color color) {
        setBackground(color);
        setLayout(new BorderLayout());

        // Left side panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(color);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add client and server buttons
        clientBtn = new JButton("Client");
        serverBtn = new JButton("Server");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        leftPanel.add(clientBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        leftPanel.add(serverBtn, gbc);

        // Add IP field with label
        JLabel ipLabel = new JLabel("IP Address:");
        ipLabel.setFont(font);
        ipField = new JTextField("127.0.0.1");
        ipField.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        leftPanel.add(ipLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        leftPanel.add(ipField, gbc);

        // Add port field with label
        JLabel portLabel = new JLabel("Port:");
        portLabel.setFont(font);
        portField = new JTextField("8080");
        portField.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        //leftPanel.add(portLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        //leftPanel.add(portField, gbc);

        // Add ready button
        readyBtn = new JButton("Ready");
        readyBtn.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        leftPanel.add(readyBtn, gbc);

        // Increase font size for left panel components
        leftPanel.setFont(font);
        for (Component comp : leftPanel.getComponents()) {
            comp.setFont(font);
        }

        add(leftPanel, BorderLayout.WEST);

        // Right side panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(color);

        // Add username field with label and apply button on the left side
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.setBackground(color);
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField("Enter username");
        applyBtn = new JButton("Apply");
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.add(applyBtn);
        rightPanel.add(usernamePanel, BorderLayout.NORTH);

        // Add chat area with scroll pane
        chatArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel msgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        msgPanel.setBackground(color);
        JLabel msgLabel = new JLabel("Message:");
        msgField = new JTextField(20);
        sendBtn = new JButton("Send");
        msgPanel.add(msgLabel);
        msgPanel.add(msgField);
        msgPanel.add(sendBtn);
        rightPanel.add(msgPanel, BorderLayout.SOUTH);

        // Increase font size for right panel components
        rightPanel.setFont(font);
        for (Component comp : rightPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component innerComp : panel.getComponents()) {
                    innerComp.setFont(font);
                }
            } else {
                comp.setFont(font);
            }
        }

        add(rightPanel, BorderLayout.CENTER);
    }
}
