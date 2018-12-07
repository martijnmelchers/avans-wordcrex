package model.database.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogWriter {
    public static void PrintLog(String content) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        System.out.println("[" + timeStamp + "] " + content);
    }
}
