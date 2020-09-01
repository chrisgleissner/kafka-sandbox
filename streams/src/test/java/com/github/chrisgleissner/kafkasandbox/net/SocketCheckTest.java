package com.github.chrisgleissner.kafkasandbox.net;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SocketCheckTest {

    @Test
    void isSocketListening() {
        assertThat(SocketCheck.isSocketListening("google.com", 443)).isTrue();
        assertThat(SocketCheck.isSocketListening("doesnotexist.aaaa", 80)).isFalse();
        assertThat(SocketCheck.isSocketListening("localhost", 0)).isFalse();
    }
}