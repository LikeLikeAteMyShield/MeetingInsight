/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight.slack;

import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface SlackClient {
    @RequestLine("GET /users.lookupByEmail?token={token}&email={email}")
    public UserLookupResponse lookupUserByEmail(@Param("token") String token, @Param("email") String email);

    @RequestLine("POST /conversations.open?token={token}&users={userId}")
    public OpenDMResponse openDMToUser(@Param("token") String token, @Param("userId") String userId);

    @RequestLine("POST /chat.postMessage?token={token}&channel={channel}&text={text}")
    public Response sendMessage(@Param("token") String token, @Param("channel") String channelId, @Param("text") String text);
}
