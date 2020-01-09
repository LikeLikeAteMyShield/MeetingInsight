/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight.zoom;

import lombok.Data;

@Data
public class ZoomMeeting {
    private String startTime;
    private String timezone;
    private String hostEmail;
}
