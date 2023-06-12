package com.novo;

import com.solace.messaging.receiver.InboundMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandleService {

    private final ExecutorService executorService;

    public MessageHandleService() {
        executorService = Executors.newFixedThreadPool(5);
    }

    void handle(InboundMessage message) {
        Handler handler = new Handler(message);
        executorService.submit(handler);
    }

    public void stop() {
        executorService.shutdown();
    }

    class Handler implements Runnable {

        private InboundMessage message;

        public Handler(InboundMessage message) {
            this.message = message;
        }

        @Override
        public void run() {
            System.out.printf("Received a message: %s handled by thread %s%n", message.getPayloadAsString(), Thread.currentThread().getName());
        }
    }
}
