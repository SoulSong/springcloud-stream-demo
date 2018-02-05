package com.cloud.shf.stream.sink;

import com.cloud.shf.stream.sink.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author songhaifeng
 * @date 2018/1/26
 */
@EnableBinding(value = {MySink.class})
public class SinkReceiver {

    private static Logger LOGGER = LoggerFactory.getLogger(SinkReceiver.class);

    /*********************************回复消息监听******************************/
    @StreamListener(value = MySink.REPLAY_SINK_CHANNEL)
    public void userReceive(@Payload User user) {
        LOGGER.info("Received from {} channel age: {}", MySink.REPLAY_SINK_CHANNEL, user.getAge());
    }

}
