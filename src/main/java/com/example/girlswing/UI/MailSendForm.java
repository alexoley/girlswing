package com.example.girlswing.UI;

import com.example.girlswing.pojo.Task;
import com.example.girlswing.services.ChatSendFormService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialBorders;
import mdlaf.utils.MaterialColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

@Component
@Getter
@Slf4j
public class MailSendForm extends JFrame {

    @Autowired
    MainForm mainForm;

    @Autowired
    ChatSendFormService chatSendFormService;

    private JComboBox country, lastOnline, education, maritialStatus, children, bodyType, religion, drinking, smoking;


    private JCheckBox withPhoto, wantsMoreChildren, activeDialogs, bookmarked;


    private JTextField textField;

    public java.util.List<java.util.List<Task>> taskList = new LinkedList<>();

    MailSendForm(@Value("${application.icon:}") String appIcon){
        try {
            InputStream icon = getClass().getClassLoader().getResourceAsStream(appIcon);
            setIconImage(ImageIO.read(icon));
        }
        catch(IOException e){
            log.error(getClass().getClassLoader().getResource(appIcon).getPath());
        }
        setTitle("Mail Settings");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        setSize(800,600);
        setLayout(null);

        textField = new JTextField("Hi! How are you?");
        JButton saveButton = new JButton("Save");
        MaterialUIMovement.add (saveButton, MaterialColors.GRAY_700);
        saveButton.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);
        textField.setBounds(100, 500, 200, 50);
        saveButton.setBounds(400, 500, 100, 50);

        add(initFilters());
        add(textField);
        add(saveButton);
        saveButton.addActionListener((ActionEvent eventIn)  -> {
            addTaskToList(mainForm.addElementToMailList(textField.getText(),this));
        });
        setLocationRelativeTo(null);
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
        withPhoto = new JCheckBox("With Photo");
        wantsMoreChildren = new JCheckBox("Wants More Children");
        activeDialogs = new JCheckBox("Active dialogs");
        bookmarked = new JCheckBox("Bookmarked");

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
        filters.add(withPhoto); filters.add(wantsMoreChildren); filters.add(activeDialogs);
        filters.add(bookmarked);

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

    private void addTaskToList(Task newTask){
        boolean wasSet=false;
        if(taskList.isEmpty()) {
            ArrayList newTaskArray = new ArrayList<>();
            newTaskArray.add(newTask);
            taskList.add(newTaskArray);
        }
        else {
            for (java.util.List listOfTasks : taskList) {
                if(newTask.equals(listOfTasks.get(0))){
                    listOfTasks.add(newTask);
                    wasSet=true;
                }
            }
            if(!wasSet){
                ArrayList newtaskArray = new ArrayList<>();
                newtaskArray.add(newTask);
                taskList.add(newtaskArray);
            }
        }
    }

    public void deleteTaskFromList(Task deleteTask){
        for (java.util.List listOfTasks : taskList) {
            if(deleteTask.equals(listOfTasks.get(0))){
                for(int i=0; i<listOfTasks.size(); i++){
                    if(deleteTask.getId()==((Task)listOfTasks.get(i)).getId()){
                        listOfTasks.remove(i);
                        if(listOfTasks.isEmpty()){
                            taskList.remove(listOfTasks);
                        }
                    }
                }
            }
        }
    }
}
