package com.example.girlswing.utils;

import com.example.girlswing.UI.ChatSendForm;
import com.example.girlswing.UI.MailSendForm;
import com.example.girlswing.pojo.DialogTask;
import com.example.girlswing.pojo.SearchTask;
import com.example.girlswing.pojo.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Slf4j
@Component
public class TaskFactory {

    @Autowired
    ApplicationContext applicationContext;

    private long chatId=0;
    private long mailId=0;

    public Task getTask(ChatSendForm frame, JProgressBar progressBar){
        chatId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(chatId,"Search","", frame.getTextField().getText(), progressBar);
        }
        if (frame.getActiveDialogs().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Active dialogs");
            dialogTask.setFilters("nomessages");
            dialogTask.setText(frame.getTextField().getText());
            dialogTask.setProgressBar(progressBar);
            return dialogTask;
            //return new DialogTask(chatId,/*onliners,*/"Active dialogs","nomessages", frame.getTextField().getText(), progressBar);
        }
        if (frame.getBookmarked().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Bookmarked");
            dialogTask.setFilters("bookmarked");
            dialogTask.setText(frame.getTextField().getText());
            dialogTask.setProgressBar(progressBar);
            return dialogTask;
            //return new DialogTask(chatId,"Bookmarked","bookmarked", frame.getTextField().getText(), progressBar);
        }
        if (frame.getAllDialogs().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("All Dialogs");
            dialogTask.setFilters("");
            dialogTask.setText(frame.getTextField().getText());
            dialogTask.setProgressBar(progressBar);
            return dialogTask;
            //return new DialogTask(chatId,"All Dialogs","onliners", frame.getTextField().getText(), progressBar);
        }
        return new SearchTask(chatId,"Search","", frame.getTextField().getText(), progressBar);
    }

    public Task getTask(ChatSendForm frame){
        chatId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(chatId,"Search","", frame.getTextField().getText());
        }
        if (frame.getActiveDialogs().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Active dialogs");
            dialogTask.setFilters("nomessages");
            dialogTask.setText(frame.getTextField().getText());
            return dialogTask;
            //return new DialogTask(chatId,"Active dialogs","nomessages", frame.getTextField().getText());
        }
        if (frame.getBookmarked().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Bookmarked");
            dialogTask.setFilters("bookmarked");
            dialogTask.setText(frame.getTextField().getText());
            return dialogTask;
            //return new DialogTask(chatId,"Bookmarked","bookmarked", frame.getTextField().getText());
        }
        if (frame.getAllDialogs().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("All Dialogs");
            dialogTask.setFilters("");
            dialogTask.setText(frame.getTextField().getText());
            return dialogTask;
            //return new DialogTask(chatId,"All Dialogs","onliners", frame.getTextField().getText());
        }
        return new SearchTask(chatId,"Search","", frame.getTextField().getText());
    }

    public Task getMailTask(MailSendForm frame, JProgressBar progressBar){
        mailId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(mailId,"Search","", frame.getTextField().getText(), progressBar);
        }
        if (frame.getActiveDialogs().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Active dialogs");
            dialogTask.setFilters("nomessages");
            dialogTask.setText(frame.getTextField().getText());
            dialogTask.setProgressBar(progressBar);
            return dialogTask;
            //return new DialogTask(mailId,/*onliners,*/"Active dialogs","nomessages", frame.getTextField().getText(), progressBar);
        }
        if (frame.getBookmarked().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Bookmarked");
            dialogTask.setFilters("bookmarked");
            dialogTask.setText(frame.getTextField().getText());
            dialogTask.setProgressBar(progressBar);
            return dialogTask;
            //return new DialogTask(mailId,"Bookmarked","bookmarked", frame.getTextField().getText(), progressBar);
        }
        return new SearchTask(mailId,"Search","", frame.getTextField().getText(), progressBar);
    }

    public Task getMailTask(MailSendForm frame){
        mailId++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(mailId,"Search","", frame.getTextField().getText());
        }
        if (frame.getActiveDialogs().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Active dialogs");
            dialogTask.setFilters("nomessages");
            dialogTask.setText(frame.getTextField().getText());
            return dialogTask;
            //return new DialogTask(mailId,"Active dialogs","nomessages", frame.getTextField().getText());
        }
        if (frame.getBookmarked().isSelected()) {
            DialogTask dialogTask = applicationContext.getBean(DialogTask.class);
            dialogTask.setId(chatId);
            dialogTask.setName("Bookmarked");
            dialogTask.setFilters("bookmarked");
            dialogTask.setText(frame.getTextField().getText());
            return dialogTask;
            //return new DialogTask(mailId,"Bookmarked","bookmarked", frame.getTextField().getText());
        }
        return new SearchTask(mailId,"Search","", frame.getTextField().getText());
    }
}
