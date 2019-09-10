package com.example.girlswing.UI;

import com.example.girlswing.services.ChatSendFormService;
import com.fasterxml.jackson.databind.JsonNode;
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

    @Autowired
    ChatSendFormService chatSendFormService;

    private JComboBox country, lastOnline, education, maritialStatus, children, bodyType, religion, drinking, smoking;

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

        JPanel countryPanel = new JPanel();
        JPanel lastOnlinePanel = new JPanel();
        JPanel educationPanel = new JPanel();
        JPanel maritialStatusPanel = new JPanel();
        JPanel childrenPanel = new JPanel();
        JPanel bodyTypePanel = new JPanel();
        JPanel religionPanel = new JPanel();
        JPanel drinkingPanel = new JPanel();
        JPanel smokingPanel = new JPanel();

        JLabel ageLabel = new JLabel("Age");
        JLabel countryLabel = new JLabel("Country");
        JLabel lastOnlineLabel = new JLabel("Last online");
        JLabel educationLabel = new JLabel("Education");
        JLabel maritialStatusLabel = new JLabel("Maritial Status");
        JLabel childrenLabel = new JLabel("Children");
        JLabel bodyTypeLabel = new JLabel("Body Type");
        JLabel religionLabel = new JLabel("Religion");
        JLabel drinkingLabel = new JLabel("Drinking");
        JLabel smokingLabel = new JLabel("Smoking");


        JSpinner ageFrom = new JSpinner(new SpinnerNumberModel(1, 1, 200, 1));
        JSpinner ageTo = new JSpinner(new SpinnerNumberModel(100, 1, 200, 1));
        country = new JComboBox();
        lastOnline = new JComboBox();
        education = new JComboBox();
        maritialStatus = new JComboBox();
        children = new JComboBox();
        bodyType = new JComboBox();
        religion = new JComboBox();
        drinking = new JComboBox();
        smoking = new JComboBox();
        JCheckBox withPhoto = new JCheckBox("With Photo");
        JCheckBox wantsMoreChildren = new JCheckBox("Wants More Children");

        //add labels to filter panels
        countryPanel.add(countryLabel, BorderLayout.NORTH);
        lastOnlinePanel.add(lastOnlineLabel, BorderLayout.NORTH);
        educationPanel.add(educationLabel, BorderLayout.NORTH);
        maritialStatusPanel.add(maritialStatusLabel, BorderLayout.NORTH);
        childrenPanel.add(childrenLabel, BorderLayout.NORTH);
        bodyTypePanel.add(bodyTypeLabel, BorderLayout.NORTH);
        religionPanel.add(religionLabel, BorderLayout.NORTH);
        drinkingPanel.add(drinkingLabel, BorderLayout.NORTH);
        smokingPanel.add(smokingLabel, BorderLayout.NORTH);
        agePanel.add(ageLabel, BorderLayout.NORTH);




        //add comboBoxes to filter panels
        countryPanel.add(country);
        lastOnlinePanel.add(lastOnline);
        educationPanel.add(education);
        maritialStatusPanel.add(maritialStatus);
        childrenPanel.add(children);
        bodyTypePanel.add(bodyType);
        religionPanel.add(religion);
        drinkingPanel.add(drinking);
        smokingPanel.add(smoking);


        agePanel.add(ageFrom); agePanel.add(ageTo);
        filters.add(agePanel); filters.add(countryPanel); filters.add(lastOnlinePanel); filters.add(educationPanel);
        filters.add(maritialStatusPanel); filters.add(childrenPanel); filters.add(bodyTypePanel);
        filters.add(religionPanel); filters.add(drinkingPanel); filters.add(smokingPanel);
        filters.add(withPhoto); filters.add(wantsMoreChildren);

        filters.setLayout(new GridLayout(4,3));
        filters.setBounds(30, 10, 600, 400);
        return filters;
    }

    public void setValuesToFilters(){
        JsonNode jsonNode = chatSendFormService.getFilterValues();
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, country, "3");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, lastOnline, "2");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, education, "6");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, maritialStatus, "5");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, children, "17");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, bodyType, "4");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, religion, "7");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, drinking, "9");
        chatSendFormService.fillFilterComboBoxWithItemFromJsonNode(jsonNode, smoking, "8");
    }

}
