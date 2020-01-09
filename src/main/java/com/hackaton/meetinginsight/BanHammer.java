/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hackaton.meetinginsight.redis.MeetingRepository;
import com.hackaton.meetinginsight.slack.SlackClient;
import com.hackaton.meetinginsight.slack.SlackUser;
import com.hackaton.meetinginsight.slack.UserLookupResponse;
import com.hackaton.meetinginsight.zoom.ZoomClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
public class BanHammer {

    private static final String SLACK_TOKEN = "xoxb-888934271185-898700894916-uc6oYQSU7tAVinFYEQNUzbUY";

    private MeetingRepository repository;
    private ZoomClient zoomClient;
    private SlackClient slackClient;

    public void startScanningForMeetings() {
        repository.saveMeeting("981140454", "{\"startTime\":\"2020-01-09T05:00:00Z\", \"timezone\": \"America/New_York\", \"hostEmail\":\"jonnycameron17@gmail.com\"}");
        repository.saveMeeting("981440454", "{\"startTime\":\"2020-01-09T01:00:00Z\", \"timezone\": \"America/New_York\", \"hostEmail\":\"jonnycameron17@gmail.com\"}");
        repository.saveMeeting("981740454", "{\"startTime\":\"2020-01-09T07:00:00Z\", \"timezone\": \"America/New_York\", \"hostEmail\":\"jonnycameron17@gmail.com\"}");

        Flux.interval(Duration.ofSeconds(5))
                .map(l -> scanForOverdueMeetings())
                .subscribe();

        //var res = client.deleteMeeting("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJlYTZsMzFTa1J1T2Vwa01Kc0dycTR3IiwiZXhwIjoxNTc4NjMyNDAwfQ.2O2cWdJfvxgxHFbL7SaQodCMEtiScVOn1QyH0MEniOI", "259452760");
    }

    private Mono<Void> scanForOverdueMeetings() {
        var meetingIds = repository.getAllMeetingIds();

        meetingIds.forEach(id -> {
            var meeting = repository.getMeeting(id);
            var timeDiff = LocalDateTime.parse(meeting.getStartTime().replace("Z", "")).until(LocalDateTime.now(), ChronoUnit.MINUTES);
            if (timeDiff > 5) {
                try {
                    var userLookup = slackClient.lookupUserByEmail(SLACK_TOKEN, meeting.getHostEmail());
                    var userId = userLookup.getUser().getId();

                    var openDMResponse = slackClient.openDMToUser(SLACK_TOKEN, userId);
                    var channelId = openDMResponse.getChannel().getId();

                    slackClient.sendMessage(SLACK_TOKEN, channelId, "Hi! Looks like you scheduled Zoom meeting " + id + " but didn't start it. If this meeting isn't happening anymore please cancel it via the Zoom app so the room can be freed up.");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return Mono.empty();
    }
}
