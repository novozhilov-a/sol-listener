package com.novo;

import com.solace.messaging.MessagingService;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.resources.Topic;

import java.io.IOException;
import java.util.Properties;

import static java.lang.System.out;

public class Publisher {
    public static void main(String... args) throws IOException {

        final Properties properties = PropsFiller.getDockerLocalProps();

        final MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties).build().connect();  // blocking connect to the broker

        final DirectMessagePublisher publisher = messagingService.createDirectMessagePublisherBuilder()
                .onBackPressureWait(1).build().start();

        OutboundMessageBuilder messageBuilder = messagingService.messageBuilder();
        int i = 0;
        Topic topic = Topic.of("solace/samples/java/hello/qqq");
        try {
            while (System.in.available() == 0) {  // loop now, just use main thread
                OutboundMessage message = messageBuilder.build("Hello World " + i++);
                out.printf("Sending: %s%n", message.getPayloadAsString());
                publisher.publish(message, topic);
                Thread.sleep(1000);
            }

        } catch (RuntimeException | InterruptedException e) {
            out.printf("### Exception caught during producer.send(): %s%n", e);
        }
        publisher.terminate(500);
        messagingService.disconnect();
        out.println("Main thread quitting.");
    }
}