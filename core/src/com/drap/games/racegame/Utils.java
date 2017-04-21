package com.drap.games.racegame;

/**
 * Created by DrAP on 22/04/2017.
 */

public class Utils {
    static public String floatToTime(float time){
        int min = (int)time/60000;
        int sec = (int) ((time/1000)-(min*60));
        int mil = (int) (time-(min*60000+sec*1000));
        return String.valueOf(min)+":"+((sec<10)?"0"+String.valueOf(sec):String.valueOf(sec))+"."+String.format("%03d",mil);
    }
}
