package com.cloud.shf.stream.source;

import com.cloud.shf.stream.sink.MySink;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

import java.util.Date;

/**
 * 原生生产者
 *
 * @author songhaifeng
 * @date 2018/2/3
 */
//@EnableBinding
public class TimerSource {

    /**
     * 轮询生产消息
     *
     * @return
     */
    @Bean
    @InboundChannelAdapter(value = MySink.ORIGINAL_CHANNEL, poller = @Poller(fixedRate = "4000", maxMessagesPerPoll = "1"))
    public MessageSource<Date> timerMessageSource() {
        return () -> new GenericMessage<>(new Date());
    }
}