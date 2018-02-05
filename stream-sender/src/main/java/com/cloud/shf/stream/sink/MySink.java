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
    /*********************************回复通道******************************/
    String REPLAY_SINK_CHANNEL = "replay-sink-channel";

    String REPLAY_SOURCE_CHANNEL = "replay-source-channel";

    @Input(REPLAY_SINK_CHANNEL)
    SubscribableChannel replayInput();

    @Output(REPLAY_SOURCE_CHANNEL)
    MessageChannel replayOutput();

    /*********************************消费组示例通道******************************/
    String GROUP_CHANNEL = "group-channel";

    @Output(GROUP_CHANNEL)
    MessageChannel groupOutput();

    /*********************************分区示例通道******************************/
    String PARTITION_CHANNEL = "partition-channel";

    @Output(PARTITION_CHANNEL)
    MessageChannel partitionInput();
}
