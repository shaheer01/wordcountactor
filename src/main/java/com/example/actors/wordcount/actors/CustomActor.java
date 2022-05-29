package com.example.actors.wordcount.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomActor<T> implements Runnable {

    private static Logger log = LoggerFactory.getLogger(CustomActor.class);

    private final ConcurrentLinkedQueue<T> mailbox;
    private final BiConsumer<CustomActor<T>, T> actionHandler;
    private final BiConsumer<CustomActor<T>, Throwable> errorHandler;

    public CustomActor(BiConsumer<CustomActor<T>, T> behaviourHandler,
                       BiConsumer<CustomActor<T>, Throwable> errorHandler) {
        this.mailbox = new ConcurrentLinkedQueue<>();
        this.actionHandler = behaviourHandler;
        this.errorHandler = errorHandler;
    }

    public static <T> CustomActor<T> create(BiConsumer<CustomActor<T>, T> behaviourHandler,
                                            BiConsumer<CustomActor<T>, Throwable> errorHandler) {
        return new CustomActor<>(behaviourHandler, errorHandler);
    }

    public void send(T message) {
        mailbox.offer(message);
    }

    @Override
    public void run() {
        try {
            while(true) {
                T message = mailbox.poll();
                if (message != null) {
                    actionHandler.accept(this, message);
                    log.info("Received Message: " + message);
                    log.info("Running in thread: " + Thread.currentThread().getName());
                }
            }
        } catch(Exception e) {
            errorHandler.accept(this, e);
        }
    }
}

