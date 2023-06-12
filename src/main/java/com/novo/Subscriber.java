package com.novo;

import com.solace.messaging.MessagingService;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.receiver.DirectMessageReceiver;
import com.solace.messaging.receiver.MessageReceiver.MessageHandler;
import com.solace.messaging.resources.TopicSubscription;

import java.io.IOException;
import java.util.Properties;

public class Subscriber {

    public static void main(String... args) throws IOException {
        final Properties dockerLocalProps = PropsFiller.getDockerLocalProps();

        final MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(dockerLocalProps).build().connect();  // blocking connect to the broker

        String topic = "com/novo/>";
        final DirectMessageReceiver receiver = messagingService.createDirectMessageReceiverBuilder()
                .withSubscriptions(TopicSubscription.of(topic)).build().start();
        System.out.printf("Listening on topic: \"%s\"\n", topic);
        MessageHandleService service = new MessageHandleService();
        final MessageHandler messageHandler = (inboundMessage) -> {
            service.handle(inboundMessage);
        };
        receiver.receiveAsync(messageHandler);

        System.in.read();

        receiver.terminate(500);
        messagingService.disconnect();
        service.stop();
        System.out.println("Main thread quitting.");
    }

}