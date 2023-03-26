package org.example.net;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.*;

@Data
public class Listener implements Runnable {
    private Deque<String> messages = new ArrayDeque<>();
    private Scanner in;

    public Listener(Scanner scanner) {
        this.in = scanner;
    }

    @Override
    @SneakyThrows
    public void run() {
        while (in.hasNextLine()) {
            messages.add(in.nextLine());
            System.out.println(messages);
            Thread.sleep(200);
        }
    }
}

