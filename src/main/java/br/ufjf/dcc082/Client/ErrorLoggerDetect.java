package br.ufjf.dcc082.Client;

public class ErrorLoggerDetect {

    private static String[] errorMessages = new String[] {
            "playback too early",
            "discontinuity received",
            "packet(s) lost",
            "duplicate packet ",
            "picture might be displayed late",
            "playback way too early",
            "emulated sync word"
    };

    public static boolean isError(String message) {
        for(String error: errorMessages) {
            if(message.contains(error)) {
                System.out.println(message);
                return true;
            }
        }
        return false;
    }
}
