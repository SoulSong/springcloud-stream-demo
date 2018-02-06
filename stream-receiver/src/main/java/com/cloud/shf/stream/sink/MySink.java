package com.cloud.shf.stream.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author songhaifeng
 * @date 2018/1/26
 */
public interface MySink {

    String CHANNEL = "myInput";

    @Input(MySink.CHANNEL)
    SubscribableChannel input();

    String OUTPUT1_CHANNEL = "OutPut-1";
    String OUTPUT2_CHANNEL = "OutPut-2";

    @Input(OUTPUT1_CHANNEL)
    SubscribableChannel input1();

    @Input(OUTPUT2_CHANNEL)
    SubscribableChannel input2();

    //原生通道
    String ORIGINAL_CHANNEL = "original-channel";

    /*********************************User对象处理实现******************************/
    String USER_CHANNEL = "user-channel";

    @Input(USER_CHANNEL)
    SubscribableChannel userInput();

    /*********************************回复通道******************************/
    String REPLAY_SINK_CHANNEL = "replay-sink-channel";
    String REPLAY_SOURCE_CHANNEL = "replay-source-channel";

    @Input(REPLAY_SOURCE_CHANNEL)
    SubscribableChannel replayInput();

    @Output(REPLAY_SINK_CHANNEL)
    MessageChannel replayOutput();


    /*********************************消费组示例通道******************************/
    String GROUP_CHANNEL = "group-channel";

    @Input(GROUP_CHANNEL)
    SubscribableChannel groupInput();

    /*********************************分区示例通道******************************/
    String PARTITION_CHANNEL = "partition-channel";

    @Input(PARTITION_CHANNEL)
    SubscribableChannel partitionInput();

    /*********************************动态通道选择示例******************************/
    String DYNAMIC1_CHANNEL = "dynamic1-channel";
    String DYNAMIC2_CHANNEL = "dynamic2-channel";

    @Input(DYNAMIC1_CHANNEL)
    SubscribableChannel dynamic1Input();

    @Input(DYNAMIC2_CHANNEL)
    SubscribableChannel dynamic2Input();

    /*********************************多binder示例******************************/
    String KAFKA_CHANNEL = "kafka-channel";
    String RABBIT_CHANNEL = "rabbit-channel";

    @Input(KAFKA_CHANNEL)
    SubscribableChannel kafkaInput();

    @Input(RABBIT_CHANNEL)
    SubscribableChannel rabbitInput();
}
