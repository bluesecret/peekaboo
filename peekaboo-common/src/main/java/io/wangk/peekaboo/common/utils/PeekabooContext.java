package io.wangk.peekaboo.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class PeekabooContext {

    @Autowired
    private Environment env;

    //TODO 后面考虑换掉此方式
    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://"+address.getHostAddress() +":"+env.getProperty("server.port")+env.getProperty("server.servlet.context-path");
    }
}
