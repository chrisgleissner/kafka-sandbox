package com.github.chrisgleissner.kafkasandbox.net;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.System.nanoTime;

@Slf4j
public class SocketCheck {

    public static boolean isSocketListening(String host, int port) {
        long startTime = nanoTime();
        try (Socket socket = new Socket()) {
            socket.setReuseAddress(true);
            socket.connect(new InetSocketAddress(host, port), 100);
            log.info("Connected to {}:{} [{}mics]", host, port, (nanoTime() - startTime) / 1000);
            return true;
        } catch (Exception e) {
            log.info("Could not connect to {}:{} due to {} [{}mics]", host, port, e.getClass().getSimpleName(),
                    (nanoTime() - startTime) / 1000);
            return false;
        }
    }
}
