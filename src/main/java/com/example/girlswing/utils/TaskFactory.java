package com.example.girlswing.utils;

import com.example.girlswing.UI.ChatSendForm;
import com.example.girlswing.pojo.DialogTask;
import com.example.girlswing.pojo.SearchTask;
import com.example.girlswing.pojo.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Slf4j
public class TaskFactory {

    private long ids=0;
    public Task getTask(ChatSendForm frame, JProgressBar progressBar){
        ids++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(ids,"Search","", frame.getTextField().getText(), progressBar);
        }
        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask(ids,/*onliners,*/"Active dialogs","onliners,nomessages", frame.getTextField().getText(), progressBar);
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask(ids,"Bookmarked","bookmarked", frame.getTextField().getText(), progressBar);
        }
        return new SearchTask(ids,"Search","", frame.getTextField().getText(), progressBar);
    }

    public Task getTask(ChatSendForm frame){
        ids++;

        if(frame.getActiveDialogs().isSelected() && frame.getBookmarked().isSelected()){
            return new SearchTask(ids,"Search","", frame.getTextField().getText());
        }
        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask(ids,"Active dialogs","onliners,nomessages", frame.getTextField().getText());
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask(ids,"Bookmarked","bookmarked", frame.getTextField().getText());
        }
        return new SearchTask(ids,"Search","", frame.getTextField().getText());
    }
}
