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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;

@Component
@Slf4j
public class MainForm extends JFrame {

    @Autowired
    MasterDataLoader masterDataLoader;

    @Autowired
    ChatSendForm chatSendForm;

    @Autowired
    MailSendForm mailSendForm;

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

    private JPanel chatScrollPanel, mailScrollPanel;

    private JPanel distributionPANEL;

    private JScrollPane chatScrollPane, mailScrollPane;

    private JSlider chatTimer, mailTimer;

    JButton chatButton, startChatButton, pauseChatButton, stopChatButton;

    JButton mailButton, startMailButton, pauseMailButton, stopMailButton;

    /*ArrayList<JButton> chatButtonsToDisable = new ArrayList<>();

    ArrayList<JButton> mailButtonsToDisable = new ArrayList<>();*/

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
        JPanel chatButtonsPanel = new JPanel();
        JPanel chatStartStopPanel = new JPanel();
        chatTimer = new JSlider(2,4,3);
        chatTimer.setMajorTickSpacing(1);
        chatTimer.setPaintLabels(true);
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
        //chatStartStopPanel.setLayout();
        chatStartStopPanel.add(startChatButton);
        chatStartStopPanel.add(pauseChatButton);
        chatStartStopPanel.add(stopChatButton);

        chatButtonsPanel.setLayout(new BorderLayout());
        chatButtonsPanel.add(chatTimer, BorderLayout.NORTH);
        chatButtonsPanel.add(chatButton, BorderLayout.CENTER);
        chatButtonsPanel.add(chatStartStopPanel, BorderLayout.SOUTH);

        /*chatModel = new DefaultListModel<>();
        JList chatList = new JList(chatModel);*/

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
                String[] massiveOfGirls = ((String) listOfGirls.getSelectedValue()).split(":", 0);
                java.util.List<JButton> buttons = new LinkedList();
                for(java.awt.Component comp: chatScrollPanel.getComponents()){
                    buttons.add((JButton)((JPanel)comp).getComponents()[1]);
                }
                buttons.add(source);
                buttons.add(chatButton);
                buttons.forEach(button -> button.setEnabled(false));
                new Thread(()-> {
                    try {
                        mainFormService.sendChatMessagesToAllTasks(chatTimer.getValue(),
                                massiveOfGirls[massiveOfGirls.length-1], buttons);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            catch (NullPointerException e){
                JOptionPane.showMessageDialog(this,"Select a girl",
                        "Reminder",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        pauseChatButton.addActionListener((ActionEvent event)  -> {});

        stopChatButton.addActionListener((ActionEvent event)  -> {});

        chatPanel.setPreferredSize(new Dimension(300,700));
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(chatLabel, BorderLayout.NORTH);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatButtonsPanel, BorderLayout.SOUTH);

        chatPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        distributionPANEL.add(chatPanel, BorderLayout.CENTER);
    }

    private void setMailPanel(){
        JPanel mailPanel = new JPanel();
        JLabel mailLabel = new JLabel("Mail");
        mailLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        JPanel mailButtonsPanel = new JPanel();
        JPanel mailStartStopPanel = new JPanel();
        mailTimer = new JSlider(2,4,3);
        mailTimer.setMajorTickSpacing(1);
        mailTimer.setPaintLabels(true);
        mailButton = new JButton("Add new mail message");

        startMailButton = new JButton("Start");
        MaterialUIMovement.add (startMailButton, MaterialColors.GREEN_A400);
        startMailButton.setForeground(MaterialColors.GREEN_A400);

        pauseMailButton = new JButton("Pause");
        MaterialUIMovement.add (pauseMailButton, MaterialColors.YELLOW_A400);
        pauseMailButton.setForeground(MaterialColors.YELLOW_A400);

        stopMailButton = new JButton("Stop");
        MaterialUIMovement.add (stopMailButton, MaterialColors.RED_A400);
        stopMailButton.setForeground(MaterialColors.RED_A400);

        //fill start and stop button to panel
        //startStopPanel.setLayout();
        mailStartStopPanel.add(startMailButton);
        mailStartStopPanel.add(pauseMailButton);
        mailStartStopPanel.add(stopMailButton);

        mailButtonsPanel.setLayout(new BorderLayout());
        mailButtonsPanel.add(mailTimer, BorderLayout.NORTH);
        mailButtonsPanel.add(mailButton, BorderLayout.CENTER);
        mailButtonsPanel.add(mailStartStopPanel, BorderLayout.SOUTH);

        mailScrollPanel = new JPanel();
        mailScrollPanel.setLayout(new GridLayout(8, 1));
        mailScrollPane = new JScrollPane(mailScrollPanel);
        mailScrollPane.setWheelScrollingEnabled(true);

        mailButton.setBorder(MaterialBorders.LIGHT_SHADOW_BORDER);
        MaterialUIMovement.add (mailButton, MaterialColors.GRAY_700);
        mailButton.addActionListener((ActionEvent event)  -> {
            mailSendForm.setVisible(true);
            mailSendForm.setValuesToFilters();
        });

        /*mailModel = new DefaultListModel<>();
        JList mailList = new JList(mailModel);*/

        startMailButton.addActionListener((ActionEvent event)  -> {
            JButton source = (JButton) event.getSource();
            try {
                String[] massiveOfGirls = ((String) listOfGirls.getSelectedValue()).split(":", 0);
                java.util.List<JButton> buttons = new LinkedList();
                for(java.awt.Component comp: mailScrollPanel.getComponents()){
                    buttons.add((JButton)((JPanel)comp).getComponents()[1]);
                }
                buttons.add(source);
                buttons.add(mailButton);
                buttons.forEach(button -> button.setEnabled(false));
                new Thread(()-> {
                    try {
                        mainFormService.sendMailMessagesToAllTasks(mailTimer.getValue(),
                                massiveOfGirls[massiveOfGirls.length-1], buttons);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            catch (NullPointerException e){
                JOptionPane.showMessageDialog(this,"Select a girl",
                        "Reminder",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        pauseChatButton.addActionListener((ActionEvent event)  -> {});

        stopChatButton.addActionListener((ActionEvent event)  -> {});

        mailPanel.setPreferredSize(new Dimension(400,700));
        mailPanel.setLayout(new BorderLayout());
        mailPanel.add(mailLabel, BorderLayout.NORTH);
        mailPanel.add(mailScrollPane, BorderLayout.CENTER);
        mailPanel.add(mailButtonsPanel, BorderLayout.SOUTH);

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
        pb.setString(text.substring(0, 40));
        pb.setStringPainted(true);
        innerPanel.add(pb);
        CloseButton closeButton = new CloseButton("✗");

        MaterialUIMovement.add (closeButton, MaterialColors.DEEP_ORANGE_300);

        innerPanel.add(closeButton);
        panel.add(innerPanel);
        ((GridLayout)panel.getLayout()).setRows(((GridLayout)panel.getLayout()).getRows()+1);
        panel.revalidate();
        Task newTask = taskFactory.getTask(chatSendForm, pb);
        newTask.setText(text);
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
        //chatButtonsToDisable.add(closeButton);
        return newTask;
        //chatModel.addElement(text);
    }

    public Task addElementToMailList(String text, MailSendForm mailSendForm){
        JPanel panel = (JPanel) mailScrollPane.getViewport().getView();
        JPanel innerPanel = new JPanel();
        JProgressBar pb = new JProgressBar(SwingConstants.HORIZONTAL);
        pb.setString(text.substring(0, 40));
        pb.setStringPainted(true);
        innerPanel.add(pb);
        CloseButton closeButton = new CloseButton("✗");

        MaterialUIMovement.add (closeButton, MaterialColors.DEEP_ORANGE_300);

        innerPanel.add(closeButton);
        panel.add(innerPanel);
        ((GridLayout)panel.getLayout()).setRows(((GridLayout)panel.getLayout()).getRows()+1);
        panel.revalidate();
        Task newTask = taskFactory.getMailTask(mailSendForm, pb);
        pb.setToolTipText(newTask.getName());
        closeButton.setTask(newTask);
        closeButton.addActionListener((ActionEvent event)  -> {
            Task t = ((CloseButton)event.getSource()).getTask();
            mailSendForm.deleteTaskFromList(t);
            innerPanel.removeAll();
            panel.remove(innerPanel);
            panel.repaint();
            panel.revalidate();
        });
        //mailButtonsToDisable.add(closeButton);
        return newTask;
    }

}
