package com.example.girlswing.utils;

import com.example.girlswing.UI.ChatSendForm;
import com.example.girlswing.UI.MailSendForm;
import com.example.girlswing.pojo.DialogTask;
import com.example.girlswing.pojo.SearchTask;
import com.example.girlswing.pojo.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Slf4j
public class TaskFactory {

    private long chatId=0;
    private long mailId=0;

    public Task getTask(ChatSendForm frame, JProgressBar progressBar){
        chatId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(chatId,"Search","", frame.getTextField().getText(), progressBar);
        }
        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask(chatId,/*onliners,*/"Active dialogs","onliners,nomessages", frame.getTextField().getText(), progressBar);
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask(chatId,"Bookmarked","bookmarked", frame.getTextField().getText(), progressBar);
        }
        return new SearchTask(chatId,"Search","", frame.getTextField().getText(), progressBar);
    }

    public Task getTask(ChatSendForm frame){
        chatId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(chatId,"Search","", frame.getTextField().getText());
        }
        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask(chatId,"Active dialogs","onliners,nomessages", frame.getTextField().getText());
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask(chatId,"Bookmarked","bookmarked", frame.getTextField().getText());
        }
        return new SearchTask(chatId,"Search","", frame.getTextField().getText());
    }

    public Task getMailTask(MailSendForm frame, JProgressBar progressBar){
        mailId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(mailId,"Search","", frame.getTextField().getText(), progressBar);
        }
        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask(mailId,/*onliners,*/"Active dialogs","nomessages", frame.getTextField().getText(), progressBar);
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask(mailId,"Bookmarked","bookmarked", frame.getTextField().getText(), progressBar);
        }
        return new SearchTask(mailId,"Search","", frame.getTextField().getText(), progressBar);
    }

    public Task getMailTask(MailSendForm frame){
        mailId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(mailId,"Search","", frame.getTextField().getText());
        }
        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask(mailId,"Active dialogs","nomessages", frame.getTextField().getText());
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask(mailId,"Bookmarked","bookmarked", frame.getTextField().getText());
        }
        return new SearchTask(mailId,"Search","", frame.getTextField().getText());
    }
}
