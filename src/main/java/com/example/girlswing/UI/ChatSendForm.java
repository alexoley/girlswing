package com.example.girlswing.UI;

import lombok.extern.slf4j.Slf4j;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialBorders;
import mdlaf.utils.MaterialColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class ChatSendForm extends JFrame {

    @Autowired
    MainForm mainForm;

    ChatSendForm(){
        try {
            InputStream icon = getClass().getClassLoader().getResourceAsStream("s_32.png");
            setIconImage(ImageIO.read(icon));
        }
        catch(IOException e){
            log.error(getClass().getClassLoader().getResource("s_32.png").getPath());
        }
        setTitle("Chat Settings");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,600);
        setLayout(null);

        JTextField textField = new JTextField("Hi! How are you?");
        JButton saveButton = new JButton("Save");
        MaterialUIMovement.add (saveButton, MaterialColors.GRAY_700);
        saveButton.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);
        textField.setBounds(100, 500, 200, 50);
        saveButton.setBounds(400, 500, 100, 50);

        add(initFilters());
        add(textField);
        add(saveButton);
        saveButton.addActionListener((ActionEvent eventIn)  -> {
            mainForm.addElementToChatList(textField.getText());
        });
    }

    private JPanel initFilters(){
        JPanel filters = new JPanel();
        JPanel agePanel = new JPanel();

        JSpinner ageFrom = new JSpinner(new SpinnerNumberModel(1, 1, 200, 1));
        JSpinner ageTo = new JSpinner(new SpinnerNumberModel(100, 1, 200, 1));
        JComboBox country = new JComboBox();
        JComboBox lastOnline = new JComboBox();
        JComboBox education = new JComboBox();
        JComboBox maritialStatus = new JComboBox();
        JComboBox children = new JComboBox();
        JComboBox bodyType = new JComboBox();
        JComboBox religion = new JComboBox();
        JComboBox drinking = new JComboBox();
        drinking.setToolTipText("Drinking");
        JComboBox smoking = new JComboBox();
        JCheckBox withPhoto = new JCheckBox("With Photo");
        JCheckBox wantsMoreChildren = new JCheckBox("Wants More Children");

        agePanel.add(ageFrom); agePanel.add(ageTo);
        filters.add(agePanel); filters.add(country); filters.add(lastOnline); filters.add(education);
        filters.add(maritialStatus); filters.add(children); filters.add(bodyType); filters.add(religion);
        filters.add(drinking); filters.add(smoking); filters.add(withPhoto); filters.add(wantsMoreChildren);

        filters.setLayout(new GridLayout(4,3));
        filters.setBounds(30, 10, 600, 400);
        return filters;
    }
}
