package com.oneone.modules.task.dto;

/**
 * Created by here on 18/5/2.
 */

public class LoginReceiveAwardDTO {
    private int taskAward;
    private int received;

    public int getTaskAward() {
        return taskAward;
    }

    public void setTaskAward(int taskAward) {
        this.taskAward = taskAward;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }
}
