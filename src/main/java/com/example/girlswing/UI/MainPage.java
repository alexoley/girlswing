package com.example.girlswing.UI;

import com.example.girlswing.services.LoginService;
import com.example.girlswing.services.MainPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.GroupLayout.Alignment.CENTER;

@Component
public class MainPage extends JFrame {

    @Autowired
    MainPageService mainPageService;

    @Autowired
    LoginService loginService;

    JProgressBar menIds;

    JProgressBar sendToMenIds;

    MainPage(){
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we)
            {
                loginService.logout();
                System.exit(0);//to close the window
            }
        });

        JTextField message = new JTextField("How are you today?");
        JTextField toWhom = new JTextField("count message");
        JButton send = new JButton("count");
        JButton sendToAll = new JButton("sendToAll");
        message.setSize(30,10);
        toWhom.setSize(30,10);
        send.setSize(20,10);

        menIds = new JProgressBar();
        sendToMenIds = new JProgressBar();
        menIds.setValue(0);
        menIds.setMinimum(0);
        menIds.setStringPainted(true);
        sendToMenIds.setValue(0);
        sendToMenIds.setStringPainted(true);
        sendToMenIds.setMinimum(0);

        send.addActionListener((ActionEvent event)  -> {
            //mainPageService.send(toWhom.getText(), message.getText());
            mainPageService.countEntryOfMessage(toWhom.getText());
        });

        sendToAll.addActionListener((ActionEvent event) -> {
            mainPageService.sendToAll(message.getText());
        });

        createLayout(message,toWhom,sendToAll, send, menIds, sendToMenIds);
    }

    public void updateBar(int newValue) {
        menIds.setValue(newValue);
    }

    public void setBarMax(int maxValue){
        menIds.setMaximum(maxValue);
    }

    public void setBar2Max(int maxValue){
        sendToMenIds.setMaximum(maxValue);
    }

    public void updateBar2(int newValue){
        sendToMenIds.setValue(newValue);
    }


    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout g1 = new GroupLayout(pane);
        pane.setLayout(g1);

        g1.setAutoCreateGaps(true);
        g1.setAutoCreateContainerGaps(true);

        g1.setHorizontalGroup(g1.createParallelGroup(CENTER)
                /*.addGroup(g1.createSequentialGroup()
                        .addComponent(arg[4]))
                .addGroup(g1.createSequentialGroup()
                        .addComponent(arg[5]))*/
                .addGroup(g1.createSequentialGroup()
                        .addComponent(arg[0])
                        .addComponent(arg[1]))
                .addGroup(g1.createSequentialGroup()
                        .addComponent(arg[2])
                        .addComponent(arg[3]))
        );

        g1.setVerticalGroup(g1.createSequentialGroup()
                /*.addGroup(g1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(arg[4]))
                .addGroup(g1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(arg[5]))*/
                .addGroup(g1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(arg[0])
                        .addComponent(arg[1]))
                .addGroup(g1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(arg[2])
                        .addComponent(arg[3]))
        );

    }


}
