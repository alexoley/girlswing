package com.example.girlswing.UI;

import com.example.girlswing.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;


@Component
public class LoginPage extends JFrame{

    @Autowired
    MainPage mainPage;

    @Autowired
    LoginService loginService;

    public LoginPage() {
        initUI();
    }

    private void initUI(){

        JButton quitButton = new JButton("Quit");
        JButton loginButton = new JButton("Login");
        JTextField usernameField = new JTextField("Username");
        JTextField passwordField = new JTextField("Password");
        JProgressBar menIds = new JProgressBar();
        JProgressBar sendToMenIds = new JProgressBar();

        menIds.setValue(0);
        sendToMenIds.setValue(0);
        menIds.setStringPainted(true);
        sendToMenIds.setStringPainted(true);

        quitButton.addActionListener((ActionEvent event) -> {
            System.exit(0);

        });

        loginButton.addActionListener((ActionEvent event)  -> {
            loginService.login(usernameField.getText(), passwordField.getText());
            this.setVisible(false);
            mainPage.setVisible(true);
            mainPage.setSize(this.getSize());
            mainPage.setLocation(this.getLocation());
            this.dispose();
        });

        createLayout(usernameField,passwordField,loginButton,quitButton, menIds);

        setTitle("Login Page");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createLayout(JComponent... arg) {

        GroupLayout g1 = new GroupLayout(getContentPane());
        getContentPane().setLayout(g1);

        g1.setAutoCreateGaps(true);
        g1.setAutoCreateContainerGaps(true);

        g1.setHorizontalGroup(g1.createParallelGroup(CENTER)
                .addGroup(g1.createSequentialGroup()
                        .addComponent(arg[0])
                        .addComponent(arg[1]))
                .addGroup(g1.createSequentialGroup()
                        .addComponent(arg[2])
                        .addComponent(arg[3]))
        );

        g1.setVerticalGroup(g1.createSequentialGroup()
                //.addGroup(g1.createParallelGroup(CENTER)
                .addGroup(g1.createParallelGroup(BASELINE)
                        .addComponent(arg[0])
                        .addComponent(arg[1]))
                .addGroup(g1.createParallelGroup(BASELINE)
                        .addComponent(arg[2])
                        .addComponent(arg[3]))//)
        );


    }




}
