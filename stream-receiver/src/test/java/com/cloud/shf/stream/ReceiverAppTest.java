package com.cloud.shf.stream;

import com.cloud.shf.stream.sink.MySink;
import com.cloud.shf.stream.sink.entity.User;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.MediaType;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;

/**
 * @author songhaifeng
 * @date 2018/1/26
 */
@RunWith(SpringRunner.class)
@EnableBinding(value = {ReceiverAppTest.SinkSender.class,
        ReceiverAppTest.MySinkSender.class,
        ReceiverAppTest.MyOutputSource.class,
        ReceiverAppTest.UserSender.class})
public class ReceiverAppTest {
    @Autowired
    private SinkSender sinkSender;

    @Test
    public void sinkSenderTester() {
        sinkSender.output().send(MessageBuilder.withPayload("produce a message to " + Sink.INPUT + " channel").build());
    }

    public interface SinkSender {
        @Output(Sink.INPUT)
        MessageChannel output();

    }

    @Autowired
    private MySinkSender mySinkSender;

    @Test
    public void mySinkSenderTester() {
        mySinkSender.output().send(MessageBuilder.withPayload("produce a message to " + MySink.CHANNEL + " channel").build());
    }

    public interface MySinkSender {
        @Output(MySink.CHANNEL)
        MessageChannel output();
    }

    @Resource(name = MySink.OUTPUT1_CHANNEL)
    private MessageChannel send1;

    @Resource(name = MySink.OUTPUT2_CHANNEL)
    private MessageChannel send2;

    @Test
    public void myOutputSourceTester() {
        send1.send(MessageBuilder.withPayload("produce a message to " + MySink.OUTPUT1_CHANNEL + " channel").build());
        send2.send(MessageBuilder.withPayload("produce a message to " + MySink.OUTPUT2_CHANNEL + " channel").build());
    }

    public interface MyOutputSource {
        @Output(MySink.OUTPUT1_CHANNEL)
        MessageChannel output1();

        @Output(MySink.OUTPUT2_CHANNEL)
        MessageChannel output2();
    }

    @Autowired
    private UserSender userSender;

    @Test
    public void myUserSenderTester() {
        User user = new User().setUsername("shuaishuai").setAge(12);
        userSender.userChannelSender().send(MessageBuilder.withPayload(ReflectionToStringBuilder.toString(user, ToStringStyle.JSON_STYLE))
                .setHeader("name", "song")
                .setHeader("flag","bb")
                .build());
    }

    public interface UserSender {
        @Output(MySink.USER_CHANNEL)
        MessageChannel userChannelSender();
    }
}