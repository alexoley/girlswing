package com.example.girlswing.utils;

import com.example.girlswing.pojo.Task;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

public class CloseButton extends JButton {
    @Getter
    @Setter
    public Task task;


    public CloseButton(String x) {
        super(x);
    }
}
