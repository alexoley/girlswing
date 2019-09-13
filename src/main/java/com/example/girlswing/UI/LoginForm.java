package com.example.girlswing.UI;

import com.example.girlswing.exceptions.GirlswingContextInitializeException;
import com.example.girlswing.exceptions.LoginException;
import com.example.girlswing.services.LoginService;
import lombok.extern.slf4j.Slf4j;
import mdlaf.utils.MaterialBorders;
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialFonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import mdlaf.animation.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class LoginForm extends JFrame{

    @Autowired
    MainPage mainPage;

    @Autowired
    MainForm mainForm;

    @Autowired
    LoginService loginService;

    JLabel l1, l2, l3;
    JTextField emailField;
    JButton btn1;
    JPasswordField passwordField;

    LoginForm(@Value("${test.programm.email:}") String testMail,
              @Value("${test.programm.password:}") String testPassword,
              @Value("${application.icon:}") String appIcon) {
        try {
            InputStream icon = getClass().getClassLoader().getResourceAsStream(appIcon);
            setIconImage(ImageIO.read(icon));
        }
        catch(IOException e){
            e.printStackTrace();
            log.error(getClass().getClassLoader().getResource(appIcon).getPath());
        }
        l1 = new JLabel("Login Form");
        l1.setForeground(MaterialColors.BLACK);
        l1.setFont(new Font("Serif", Font.BOLD, 20));
        l1.setFont(MaterialFonts.BOLD);

        l2 = new JLabel("Username");
        l3 = new JLabel("Password");
        emailField = new JTextField(testMail);
        passwordField = new JPasswordField(testPassword);
        btn1 = new JButton("Login");
        MaterialUIMovement.add (btn1, MaterialColors.GRAY_700);
        btn1.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);

        l1.setBounds(100, 30, 400, 30);
        l2.setBounds(80, 70, 200, 30);
        l3.setBounds(80, 110, 200, 30);
        log.error(getBackground().toString());
        l1.setBackground(MaterialColors.WHITE);
        l2.setBackground(MaterialColors.WHITE);
        l3.setBackground(MaterialColors.WHITE);
        emailField.setBounds(300, 70, 250, 30);
        passwordField.setBounds(300, 110, 250, 30);
        btn1.setBounds(150, 160, 100, 30);

        add(l1);
        add(l2);
        add(emailField);
        add(l3);
        add(passwordField);
        add(btn1);
        setLayout(null);

        btn1.addActionListener((ActionEvent event)  -> {
            String password = new String(passwordField.getPassword());
            try {
                loginService.applicationLogin(emailField.getText(), password);
                this.setVisible(false);
                /*mainPage.setVisible(true);
                mainPage.setSize(this.getSize());
                mainPage.setLocation(this.getLocation());*/

                mainForm.setVisible(true);

                this.dispose();
            }
            catch(LoginException e){
                JOptionPane.showMessageDialog(this,"Incorrect login or password",
                        "Error",JOptionPane.ERROR_MESSAGE);
                log.error(e.getMessage());
                log.trace(e.getMessage());
            }
            catch(GirlswingContextInitializeException e){
                log.error(e.getMessage());
            }
        });
    }
}
