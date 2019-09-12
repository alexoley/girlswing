package com.example.girlswing.utils;

import com.example.girlswing.UI.ChatSendForm;
import com.example.girlswing.pojo.DialogTask;
import com.example.girlswing.pojo.SearchTask;
import com.example.girlswing.pojo.Task;

import javax.swing.*;

public class TaskFactory {

    public Task getTask(ChatSendForm frame, JProgressBar progressBar){

        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask("onliners,nomessages", frame.getTextField().getText(), progressBar);
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask("bookmarked", frame.getTextField().getText(), progressBar);
        }
        return new SearchTask("", frame.getTextField().getText(), progressBar);
    }

    public Task getTask(ChatSendForm frame){

        if (frame.getActiveDialogs().isSelected()) {
            return new DialogTask("onliners,nomessages", frame.getTextField().getText());
        }
        if (frame.getBookmarked().isSelected()) {
            return new DialogTask("bookmarked", frame.getTextField().getText());
        }
        return new SearchTask("", frame.getTextField().getText());
    }
}
