package com.cloud.shf.stream.sink;

import com.cloud.shf.stream.sink.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author songhaifeng
 * @date 2018/1/26
 */
@EnableBinding(value = {Sink.class, MySink.class})
public class SinkReceiver {

    private static Logger LOGGER = LoggerFactory.getLogger(SinkReceiver.class);

    @StreamListener(Sink.INPUT)
    public void receive(Object payload) {
        LOGGER.info("Received from default channel : {}", payload.toString());
    }

    @StreamListener(MySink.CHANNEL)
    public void myReceive(Object payload) {
        LOGGER.info("Received from {} channel : {}", MySink.CHANNEL, payload.toString());
    }

    @StreamListener(MySink.OUTPUT1_CHANNEL)
    public void myReceive1(Object payload) {
        LOGGER.info("Received from {} channel : {}", MySink.OUTPUT1_CHANNEL, payload.toString());
    }

    @StreamListener(MySink.OUTPUT2_CHANNEL)
    public void myReceive2(Object payload) {
        LOGGER.info("Received from {} channel : {}", MySink.OUTPUT2_CHANNEL, payload.toString());
    }

    /*********************************Integration 原生实现******************************/
    @ServiceActivator(inputChannel = MySink.ORIGINAL_CHANNEL)
    public void originalReceiver(Object payload) {
        LOGGER.info("Received from {} channel : {}", MySink.ORIGINAL_CHANNEL, payload.toString());
    }

    @Transformer(inputChannel = MySink.ORIGINAL_CHANNEL, outputChannel = MySink.ORIGINAL_CHANNEL)
    public Object transform(Date message) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(message);
    }

    /*********************************User对象处理实现******************************/
    @StreamListener(value = MySink.USER_CHANNEL)
    public void userReceive(@Payload User user, @Headers Map headers, @Header(name = "name") Object name) {
        LOGGER.info(headers.get("contentType").toString());
        LOGGER.info("name : {}", name.toString());
        LOGGER.info("Received from {} channel username: {}", MySink.USER_CHANNEL, user.getUsername());
    }

    /*********************************实现多个监听者应用******************************/
    @StreamListener(value = MySink.USER_CHANNEL, condition = "headers['flag']=='aa'")
    public void userReceiveByHeader1(@Payload User user) {
        LOGGER.info("Received from {} channel : {} with head (flag:aa)", MySink.USER_CHANNEL, user.getUsername());
    }

    @StreamListener(value = MySink.USER_CHANNEL, condition = "headers['flag']=='bb'")
    public void userReceiveByHeader2(@Payload User user) {
        LOGGER.info("Received from {} channel : {} with head (flag:bb)", MySink.USER_CHANNEL, user.getUsername());
    }

    /*********************************自动回复消息******************************/
    @StreamListener(value = MySink.REPLAY_SOURCE_CHANNEL)
    @SendTo(value = {MySink.REPLAY_SINK_CHANNEL})
    public User userReplay(@Payload User user) {
        LOGGER.info("Received from {} channel age: {}", MySink.REPLAY_SOURCE_CHANNEL, user.getAge());
        return user.setAge(user.getAge() + 1);
    }

    /*********************************消费组示例******************************/
    @Value("${spring.profiles.active:0}")
    private String active;

    @StreamListener(value = MySink.GROUP_CHANNEL)
    public void groupReceiver(@Payload String payload) {
        LOGGER.info("Received-{} from {} channel payload: {}", active, MySink.GROUP_CHANNEL, payload);
    }

    /********************************分区示例******************************/
    @StreamListener(value = MySink.PARTITION_CHANNEL)
    public void partitionReceiver(@Payload User user) {
        LOGGER.info("Received-{} from {} channel age: {}", active, MySink.PARTITION_CHANNEL, user.getAge());
    }

    /*********************************动态通道选择示例******************************/
    @StreamListener(value = MySink.DYNAMIC1_CHANNEL)
    public void dynamic1Receiver(@Payload User user) {
        LOGGER.info("Received-{} from {} channel age: {}", active, MySink.DYNAMIC1_CHANNEL, user.getAge());
    }

    @StreamListener(value = MySink.DYNAMIC2_CHANNEL)
    public void dynamic2Receiver(@Payload User user) {
        LOGGER.info("Received-{} from {} channel age: {}", active, MySink.DYNAMIC2_CHANNEL, user.getAge());
    }

    /*********************************多binder示例******************************/
    @StreamListener(value = MySink.KAFKA_CHANNEL)
    public void kafkaReceiver(@Payload User user) {
        LOGGER.info("Received-{} from {} channel age: {}", active, MySink.KAFKA_CHANNEL, user.getAge());
    }

    @StreamListener(value = MySink.RABBIT_CHANNEL)
    public void rabbitReceiver(@Payload User user) {
        LOGGER.info("Received-{} from {} channel age: {}", active, MySink.RABBIT_CHANNEL, user.getAge());
    }
}
