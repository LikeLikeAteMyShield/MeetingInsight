/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton.meetinginsight.redis.MeetingRepository;
import com.hackaton.meetinginsight.redis.RedisConnectionPool;
import com.hackaton.meetinginsight.slack.SlackClient;
import com.hackaton.meetinginsight.zoom.ZoomClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MeetingInsightService {

    @PostConstruct
    public void init() {
        var redisPool = new RedisConnectionPool(RedisConnectionPool.setupJedisConnectionPool());
        var repository = new MeetingRepository(redisPool.getJedis());
        var objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        var zoomClient = Feign.builder().client(new OkHttpClient()).decoder(new JacksonDecoder(objectMapper)).target(ZoomClient.class, "https://api.zoom.us/v2");
        var slackClient = Feign.builder().client(new OkHttpClient()).decoder(new JacksonDecoder(objectMapper)).target(SlackClient.class, "https://slack.com/api");
        new BanHammer(repository, zoomClient, slackClient).startScanningForMeetings();
    }
}
