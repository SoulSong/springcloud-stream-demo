package com.cloud.shf.stream.controller;

import com.cloud.shf.stream.sink.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.router.ExpressionEvaluatingRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试验证动态通道消息发送
 *
 * @author songhaifeng
 * @date 2018/2/6
 */
@EnableBinding
@Controller
public class DynamicDestinationController {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /************************************方式一************************************/
    @RequestMapping(path = "/{dest}", method = RequestMethod.POST, consumes = "*/*")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handleRequest(@PathVariable("dest") String dest,
                              @RequestBody String body,
                              @RequestHeader(HttpHeaders.CONTENT_TYPE) Object contentType) {
        sendMessage(body, dest, contentType);
    }

    private void sendMessage(String body, String dest, Object contentType) {
        resolver.resolveDestination(dest).send(MessageBuilder.createMessage(body,
                new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, contentType))));
    }

    /************************************方式二************************************/
    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handleRequest(@RequestBody User body, @RequestHeader(HttpHeaders.CONTENT_TYPE) Object contentType, @RequestHeader(name = "dest", required = false) String dest) {
        Map<String, Object> headers = new HashMap<>(2);
        headers.put(MessageHeaders.CONTENT_TYPE, contentType);
        if (!StringUtils.isEmpty(dest)) {
            headers.put("dest", dest);
        }
        sendMessage(body, headers);
    }

    private void sendMessage(User body, Map<String, Object> headers) {
        routerChannel().send(MessageBuilder.createMessage(body,
                new MessageHeaders(headers)));
    }

    @Bean(name = "router-channel")
    public MessageChannel routerChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "router-channel")
    public ExpressionEvaluatingRouter router() {
        ExpressionEvaluatingRouter router = new ExpressionEvaluatingRouter(new SpelExpressionParser().parseExpression("headers[dest]"));
        //作用于通过spel表达式没有获取到对应的通道信息
        router.setDefaultOutputChannelName("dynamic1-channel");
        router.setChannelResolver(resolver);
        return router;
    }
}
