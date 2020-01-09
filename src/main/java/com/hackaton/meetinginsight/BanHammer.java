/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight;

public class BanHammer {

    static void startScanningForMeetings() {
        while (true) {
            System.out.println("Scanning....");
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ignored){}
        }
    }
}
