package com.example.girlswing.UI;


import com.example.girlswing.utils.MasterDataLoader;
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
public class MainForm extends JFrame {

    @Autowired
    MasterDataLoader masterDataLoader;

    @Autowired
    ChatSendForm chatSendForm;

    private JList listOfGirls;

    private DefaultListModel<String> chatModel;

    private DefaultListModel<String> mailModel;

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

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());


        panel1.add(girlsListPanel, BorderLayout.LINE_START);

        JTabbedPane tPane = new JTabbedPane();
        tPane.addTab("Рассылка", panel1);
        tPane.addTab("Статистика", new JPanel());
        tPane.addTab("VIP", new JPanel());
        add(tPane);

        //chat panel
        JLabel chatLabel = new JLabel("Chat");
        chatLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        JButton chatButton = new JButton("Add new chat message");
        JPanel chatPanel = new JPanel();
        chatModel = new DefaultListModel<>();
        JList chatList = new JList(chatModel);

        chatButton.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);
        MaterialUIMovement.add (chatButton, MaterialColors.GRAY_700);
        chatButton.addActionListener((ActionEvent event)  -> {
            chatSendForm.setVisible(true);
        });

        chatPanel.setPreferredSize(new Dimension(400,700));
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(chatLabel, BorderLayout.NORTH);
        chatPanel.add(new JScrollPane(chatList), BorderLayout.CENTER);
        chatPanel.add(chatButton, BorderLayout.SOUTH);
        chatPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        panel1.add(chatPanel, BorderLayout.CENTER);

        //mail panel
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
        panel1.add(mailPanel, BorderLayout.LINE_END);

        setLocationRelativeTo(null);
    }

    public void setElementsToListOfGirls(ListModel elements) {
        this.listOfGirls.setModel(elements);
    }

    public void addElementToChatList(String text){
        chatModel.addElement(text);
    }

}
