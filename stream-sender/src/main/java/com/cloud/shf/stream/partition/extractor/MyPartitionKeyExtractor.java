package com.cloud.shf.stream.partition.extractor;

import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.messaging.Message;

import java.util.Map;

/**
 * 参考：https://github.com/spring-cloud/spring-cloud-stream-binder-kafka/blob/master/spring-cloud-stream-binder-kafka/src/test/java/org/springframework/cloud/stream/binder/kafka/RawKafkaPartitionTestSupport.java
 *
 * @author songhaifeng
 * @date 2018/2/5
 */
public class MyPartitionKeyExtractor implements PartitionKeyExtractorStrategy, PartitionSelectorStrategy {

    @Override
    public int selectPartition(Object key, int divisor) {
//        int routerKey = key.hashCode() % divisor;
        return ((Map<String, Integer>) key).get("router");
    }

    @Override
    public Object extractKey(Message<?> message) {
        return message.getHeaders();
    }

}