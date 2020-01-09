/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MeetingInsightService {

    @PostConstruct
    public void init() {
        BanHammer.startScanningForMeetings();
    }
}
