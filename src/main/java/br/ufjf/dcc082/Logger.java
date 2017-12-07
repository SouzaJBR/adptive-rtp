package br.ufjf.dcc082;

import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;
import uk.co.caprica.vlcj.log.NativeLog;

public class Logger {

    private static NativeLog logger = null;

    public static NativeLog getLogger() {
        return logger;
    }

    public static void log(libvlc_log_level_e level, String module, String file, Integer line, String name, String header, Integer id, String message) {
        if(logger != null)
            if(System.currentTimeMillis() == 0)
                System.out.printf("[%-20s] (%-20s) %7s: %s\n", module, name, level, message);
    }

    public static void setLogger(NativeLog logger) {
        Logger.logger = logger;
    }
}
