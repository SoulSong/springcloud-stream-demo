package com.cloud.shf.stream.source;

import com.cloud.shf.stream.sink.MySink;
import com.cloud.shf.stream.sink.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 用于验证分区的生产者示例
 *
 * @author songhaifeng
 * @date 2018/2/3
 */
//@EnableBinding(value = MySink.class)
public class PartitionSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionSource.class);

    @Bean
    @InboundChannelAdapter(value = MySink.PARTITION_CHANNEL, poller = @Poller(fixedRate = "5000", maxMessagesPerPoll = "1"))
    public MessageSource<User> partitionMessageSource() {
        return () -> {
            Double value = Math.random() * 10 % 5;
            int age = value.intValue();
            LOGGER.info("current age : {}", age);
            Map<String, Object> headers = new HashMap<>();
            headers.put("router", age);
            return new GenericMessage<>(new User().setUsername("shuaishuai").setAge(age), headers);
        };
    }
}