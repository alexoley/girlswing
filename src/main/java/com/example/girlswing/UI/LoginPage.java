package com.example.girlswing.UI;

import com.example.girlswing.exceptions.GirlswingContextInitializeException;
import com.example.girlswing.exceptions.LoginException;
import com.example.girlswing.services.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;


@Component
@Slf4j
public class LoginPage extends JFrame{

    @Autowired
    MainPage mainPage;

    @Autowired
    LoginService loginService;

    @Autowired
    MainForm mainForm;


    public LoginPage() {
        initUI();
    }

    private void initUI(){

        JButton quitButton = new JButton("Quit");
        JButton loginButton = new JButton("Login");
        JTextField emailField = new JTextField("Email");
        JPasswordField passwordField = new JPasswordField("Password");
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
            try {
                String pass = new String(passwordField.getPassword());
                loginService.applicationLogin(emailField.getText(), pass);
                this.setVisible(false);
                mainPage.setVisible(true);
                mainPage.setSize(this.getSize());
                mainPage.setLocation(this.getLocation());

                mainForm.setVisible(true);

                this.dispose();
            }
            catch(LoginException e){
                JOptionPane.showMessageDialog(this,"Incorrect login or password",
                        "Error",JOptionPane.ERROR_MESSAGE);
                log.error(e.getMessage());
            }
            catch(GirlswingContextInitializeException e){
                log.error(e.getMessage());
            }
        });

        createLayout(emailField,passwordField,loginButton,quitButton, menIds);

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
