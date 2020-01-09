/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton.meetinginsight.zoom.ZoomMeeting;
import lombok.AllArgsConstructor;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class MeetingRepository {

    private Jedis jedis;

    public List<String> getAllMeetingIds() {
        var ids = new ArrayList<String>();
        var scanResult = jedis.scan("0");
        ids.addAll(scanResult.getResult());
        return ids;
    }

    public void saveMeeting(String meetingId, String json) {
        jedis.set(meetingId, json);
    }

    public ZoomMeeting getMeeting(String meetingId) {
        var meeting = jedis.get(meetingId);

        try {
            return new ObjectMapper().readValue(meeting, ZoomMeeting.class);
        } catch(Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException();
    }

    public void deleteMeeting(String meetingId) {
        jedis.del(meetingId);
    }
}
