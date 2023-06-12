package com.novo;

import com.solace.messaging.config.SolaceProperties;

import java.util.Properties;

class PropsFiller {

    private final Properties dockerLocalProps = new Properties();
    private static PropsFiller instance = new PropsFiller();

    private PropsFiller() {
        String solaceHost = System.getenv("solace_host");
        solaceHost = "tcp://"+solaceHost+":55555";
        dockerLocalProps.setProperty(SolaceProperties.TransportLayerProperties.HOST, solaceHost);
        dockerLocalProps.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, "default");
        dockerLocalProps.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, "default");
        dockerLocalProps.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, "default");
        dockerLocalProps.setProperty(SolaceProperties.ServiceProperties.RECEIVER_DIRECT_SUBSCRIPTION_REAPPLY, "true");  // subscribe Direct subs after reconnect
    }

    public static Properties getDockerLocalProps() {
        return instance.dockerLocalProps;
    }
}