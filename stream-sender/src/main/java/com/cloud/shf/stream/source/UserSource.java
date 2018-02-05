package com.cloud.shf.stream.source;

import com.cloud.shf.stream.sink.MySink;
import com.cloud.shf.stream.sink.entity.User;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

/**
 * 用于验证@Sendto注解使用
 *
 * @author songhaifeng
 * @date 2018/2/3
 */
//@EnableBinding(value = MySink.class)
public class UserSource {
    /**
     * 轮询生产消息
     *
     * @return
     */
    @Bean
    @InboundChannelAdapter(value = MySink.REPLAY_SOURCE_CHANNEL, poller = @Poller(fixedRate = "5000", maxMessagesPerPoll = "1"))
    public MessageSource<User> timerMessageSource() {
        return () -> new GenericMessage<>(new User().setUsername("shuaishuai").setAge(12));
    }
}