/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight.zoom;

import feign.Client;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ZoomClient {
    @RequestLine("DELETE /meetings/{meetingId}")
    @Headers("Authorization: {authToken}")
    Response deleteMeeting(@Param("authToken") String authToken, @Param("meetingId") String meetingId);
}

class Builder {
    private Client client;
    private String url;
}
