package org.example.net;

import lombok.Data;
import lombok.SneakyThrows;
import org.example.strategy.ActionStrategy;

import java.awt.*;
import java.io.*;

@Data
public abstract class BattleshipUser implements Runnable {
    protected Point shootPoint;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected ObjectInputStream objectInputStream;
    protected ObjectOutputStream objectOutputStream;

    protected ActionStrategy readStrategy() throws IOException, ClassNotFoundException {
        ActionStrategy actionStrategy = (ActionStrategy) objectInputStream.readObject();
        System.out.println("Read " + actionStrategy);
        return actionStrategy;
    }

    protected void writeStrategy(ActionStrategy actionStrategy) throws IOException {
        System.out.println("Writing " + actionStrategy);
        objectOutputStream.writeObject(actionStrategy);
    }

    public abstract void stop() throws Exception;
}
