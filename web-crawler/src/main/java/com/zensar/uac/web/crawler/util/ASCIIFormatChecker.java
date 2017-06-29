package com.zensar.uac.web.crawler.util;

/**
 * Created by Sagar Balai on 10/16/2016.
 * Purpose of the class: Checking forAsking urls
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class ASCIIFormatChecker {

    public static boolean check(String input) {
        boolean isAsciiStr = true;
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 0x7F) {
                isAsciiStr = false;
                break;
            }
        }
        return isAsciiStr;
    }
}
