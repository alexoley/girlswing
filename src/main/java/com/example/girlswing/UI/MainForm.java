package com.example.girlswing.UI;


import com.example.girlswing.services.ChatSendFormService;
import com.example.girlswing.utils.MasterDataLoader;
import lombok.extern.slf4j.Slf4j;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialBorders;
import mdlaf.utils.MaterialColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class MainForm extends JFrame {

    @Autowired
    MasterDataLoader masterDataLoader;

    @Autowired
    ChatSendForm chatSendForm;

    @Autowired
    ChatSendFormService chatSendFormService;

    private JList listOfGirls;

    private DefaultListModel<String> chatModel;

    private DefaultListModel<String> mailModel;

    private JPanel distributionPANEL;

    MainForm() {
        setSize(1200,800);
        //setUndecorated(true);
        try {
            InputStream icon = getClass().getClassLoader().getResourceAsStream("s_32.png");
            setIconImage(ImageIO.read(icon));
        }
        catch(IOException e){
            log.error(getClass().getClassLoader().getResource("s_32.png").getPath());
        }


        //list of girls panel
        listOfGirls = new JList();
        JLabel girlsLabel = new JLabel("Girls");
        girlsLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        JPanel girlsListPanel = new JPanel();
        girlsListPanel.setPreferredSize(new Dimension(200,700));
        girlsListPanel.setLayout(new BorderLayout());
        girlsListPanel.add(girlsLabel, BorderLayout.NORTH);
        girlsListPanel.add(new JScrollPane(listOfGirls), BorderLayout.CENTER);
        girlsListPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        distributionPANEL = new JPanel();
        distributionPANEL.setLayout(new BorderLayout());


        distributionPANEL.add(girlsListPanel, BorderLayout.LINE_START);

        JTabbedPane tPane = new JTabbedPane();
        tPane.addTab("Рассылка", distributionPANEL);
        tPane.addTab("Статистика", new JPanel());
        tPane.addTab("VIP", new JPanel());
        add(tPane);

        //chat panel
        setChatPanel();

        //mail panel
        setMailPanel();

        setLocationRelativeTo(null);
    }

    private void setChatPanel(){
        JPanel chatPanel = new JPanel();
        JLabel chatLabel = new JLabel("Chat");
        chatLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        JPanel buttonsPanel = new JPanel();
        JPanel startStopPanel = new JPanel();
        JSlider timer = new JSlider(2,4,3);
        timer.setMajorTickSpacing(1);
        JButton chatButton = new JButton("Add new chat message");

        JButton startButton = new JButton("Start");
        MaterialUIMovement.add (startButton, MaterialColors.GREEN_300);
        //startButton.setBorder(MaterialBorders.LIGHT_LINE_BORDER);
        startButton.setForeground(MaterialColors.GREEN_300);

        JButton stopButton = new JButton("Stop");
        MaterialUIMovement.add (stopButton, MaterialColors.RED_300);
        //stopButton.setBorder(MaterialBorders.DEFAULT_SHADOW_BORDER);
        stopButton.setForeground(MaterialColors.RED_300);
        //fill start and stop button to panel
        //startStopPanel.setLayout();
        startStopPanel.add(startButton);
        startStopPanel.add(stopButton);

        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.add(timer, BorderLayout.NORTH);
        buttonsPanel.add(chatButton, BorderLayout.CENTER);
        buttonsPanel.add(startStopPanel, BorderLayout.SOUTH);

        chatModel = new DefaultListModel<>();
        JList chatList = new JList(chatModel);

        chatButton.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);
        MaterialUIMovement.add (chatButton, MaterialColors.GRAY_700);
        chatButton.addActionListener((ActionEvent event)  -> {
            chatSendForm.setVisible(true);
            chatSendForm.setValuesToFilters();
        });

        startButton.addActionListener((ActionEvent event)  -> {
            chatSendFormService.getAllActiveUsersAndSendThemMessage();
        });

        chatPanel.setPreferredSize(new Dimension(400,700));
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(chatLabel, BorderLayout.NORTH);
        chatPanel.add(new JScrollPane(chatList), BorderLayout.CENTER);
        chatPanel.add(buttonsPanel, BorderLayout.SOUTH);

        startButton.setSize(startStopPanel.getWidth()/2, startStopPanel.getHeight());
        stopButton.setSize(startStopPanel.getWidth()/2, startStopPanel.getHeight());
        chatPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        distributionPANEL.add(chatPanel, BorderLayout.CENTER);
    }

    private void setMailPanel(){
        JLabel mailLabel = new JLabel("Mail");
        mailLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        JButton mailButton = new JButton("Add new mail message");
        JPanel mailPanel = new JPanel();
        mailButton.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);
        MaterialUIMovement.add (mailButton, MaterialColors.GRAY_700);
        mailButton.addActionListener((ActionEvent event)  -> {

        });

        mailModel = new DefaultListModel<>();
        JList mailList = new JList(mailModel);

        mailPanel.setPreferredSize(new Dimension(400,700));
        mailPanel.setLayout(new BorderLayout());
        mailPanel.add(mailLabel, BorderLayout.NORTH);
        mailPanel.add(mailList, BorderLayout.CENTER);
        mailPanel.add(mailButton, BorderLayout.SOUTH);
        mailPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        distributionPANEL.add(mailPanel, BorderLayout.LINE_END);
    }
    public void setElementsToListOfGirls(ListModel elements) {
        this.listOfGirls.setModel(elements);
    }

    public void addElementToChatList(String text){
        chatModel.addElement(text);
    }

}
