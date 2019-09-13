package com.example.girlswing.UI;


import com.example.girlswing.pojo.Task;
import com.example.girlswing.services.ChatSendFormService;
import com.example.girlswing.services.LoginService;
import com.example.girlswing.services.MainFormService;
import com.example.girlswing.utils.CloseButton;
import com.example.girlswing.utils.MasterDataLoader;
import com.example.girlswing.utils.TaskFactory;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Component
@Slf4j
public class MainForm extends JFrame {

    @Autowired
    MasterDataLoader masterDataLoader;

    @Autowired
    ChatSendForm chatSendForm;

    @Autowired
    ChatSendFormService chatSendFormService;

    @Autowired
    LoginService loginService;

    @Autowired
    MainFormService mainFormService;

    @Autowired
    TaskFactory taskFactory;

    private JList listOfGirls;

    private DefaultListModel chatModel;

    private DefaultListModel mailModel;

    private JPanel chatScrollPanel;

    private JPanel distributionPANEL;

    private JScrollPane chatScrollPane;

    private JSlider chatTimer;

    JButton chatButton, startChatButton, pauseChatButton, stopChatButton;

    ArrayList<JButton> buttonsToDisable = new ArrayList<>();

    MainForm(@Value("${application.icon:}") String appIcon) {
        setSize(1200,800);
        //setUndecorated(true);
        try {
            InputStream icon = getClass().getClassLoader().getResourceAsStream(appIcon);
            setIconImage(ImageIO.read(icon));
        }
        catch(IOException e){
            log.error(getClass().getClassLoader().getResource(appIcon).getPath());
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we)
            {
                loginService.logout();
                System.exit(0);//to close the window
            }
        });


        //list of girls panel
        setListOfGirlPanel();

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


    private  void setListOfGirlPanel(){
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
    }

    private void setChatPanel(){
        JPanel chatPanel = new JPanel();
        JLabel chatLabel = new JLabel("Chat");
        chatLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        JPanel buttonsPanel = new JPanel();
        JPanel startStopPanel = new JPanel();
        chatTimer = new JSlider(2,4,3);
        chatTimer.setMajorTickSpacing(1);
        chatButton = new JButton("Add new chat message");

        startChatButton = new JButton("Start");
        MaterialUIMovement.add (startChatButton, MaterialColors.GREEN_A400);
        startChatButton.setForeground(MaterialColors.GREEN_A400);

        pauseChatButton = new JButton("Pause");
        MaterialUIMovement.add (pauseChatButton, MaterialColors.YELLOW_A400);
        pauseChatButton.setForeground(MaterialColors.YELLOW_A400);

        stopChatButton = new JButton("Stop");
        MaterialUIMovement.add (stopChatButton, MaterialColors.RED_A400);
        stopChatButton.setForeground(MaterialColors.RED_A400);

        //fill start and stop button to panel
        //startStopPanel.setLayout();
        startStopPanel.add(startChatButton);
        startStopPanel.add(pauseChatButton);
        startStopPanel.add(stopChatButton);

        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.add(chatTimer, BorderLayout.NORTH);
        buttonsPanel.add(chatButton, BorderLayout.CENTER);
        buttonsPanel.add(startStopPanel, BorderLayout.SOUTH);

        chatModel = new DefaultListModel<>();
        JList chatList = new JList(chatModel);
        //temp
        chatScrollPanel = new JPanel();
        chatScrollPanel.setLayout(new GridLayout(8, 1));
        chatScrollPane = new JScrollPane(chatScrollPanel);
        chatScrollPane.setWheelScrollingEnabled(true);


        chatButton.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);
        MaterialUIMovement.add (chatButton, MaterialColors.GRAY_700);
        chatButton.addActionListener((ActionEvent event)  -> {
            chatSendForm.setVisible(true);
            chatSendForm.setValuesToFilters();
        });

        startChatButton.addActionListener((ActionEvent event)  -> {
            JButton source = (JButton) event.getSource();
            try {
                //chatScrollPanel.
                String[] massiveOfGirls = ((String) listOfGirls.getSelectedValue()).split(":", 0);
                buttonsToDisable.add(source);
                buttonsToDisable.add(chatButton);
                /*source.setEnabled(false);
                chatButton.setEnabled(false);*/
                new Thread(()->mainFormService.sendChatMessagesToAllTasks(chatTimer.getValue(),
                        massiveOfGirls[massiveOfGirls.length-1], buttons)).start();
            }
            catch (NullPointerException e){
                JOptionPane.showMessageDialog(this,"Select a girl",
                        "Reminder",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        chatPanel.setPreferredSize(new Dimension(400,700));
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(chatLabel, BorderLayout.NORTH);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(buttonsPanel, BorderLayout.SOUTH);

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

    public Task addElementToChatList(String text, ChatSendForm chatSendForm){
        JPanel panel = (JPanel) chatScrollPane.getViewport().getView();
        JPanel innerPanel = new JPanel();
        JProgressBar pb = new JProgressBar(SwingConstants.HORIZONTAL);
        pb.setString(text);
        pb.setStringPainted(true);
        innerPanel.add(pb);
        CloseButton closeButton = new CloseButton("✗");

        MaterialUIMovement.add (closeButton, MaterialColors.DEEP_ORANGE_300);

        innerPanel.add(closeButton);
        panel.add(innerPanel);
        ((GridLayout)panel.getLayout()).setRows(((GridLayout)panel.getLayout()).getRows()+1);
        panel.revalidate();
        Task newTask = taskFactory.getTask(chatSendForm);
        newTask.setProgressBar(pb);
        pb.setToolTipText(newTask.getName());
        closeButton.setTask(newTask);
        closeButton.addActionListener((ActionEvent event)  -> {
            Task t = ((CloseButton)event.getSource()).getTask();
            chatSendForm.deleteTaskFromList(t);
            innerPanel.removeAll();
            panel.remove(innerPanel);
            panel.repaint();
            panel.revalidate();
        });
        buttonsToDisable.add(closeButton);
        return newTask;
        //chatModel.addElement(text);
    }

}
