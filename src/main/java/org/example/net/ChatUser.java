package org.example.net;

import lombok.Data;

import java.util.ArrayDeque;
import java.util.Deque;

@Data
public abstract class ChatUser implements Runnable {
    protected Deque<String> messages = new ArrayDeque<>();

    public void addMessage(String message) {
        messages.add(message);
    }

    public abstract void stop() throws Exception;
}
