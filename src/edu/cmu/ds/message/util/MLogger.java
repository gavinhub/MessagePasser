package edu.cmu.ds.message.util;

/**
 * Created by Mingyang Ge on 1/30/16.
 */
public class MLogger {
    public static void log(String tag, String content) {
        MLogger.output(tag, content, 33);
    }

    public static void error(String tag, String content) {
        MLogger.output(tag, content, 91);
    }

    public static void info(String tag, String content) {
        MLogger.output(tag, content, 36);
    }

    public static void message(String tag, String content) { MLogger.output(tag, content, 31);}

    private static void output(String tag, String content, int color) {
        System.out.println("\033[" + color + "m[" + tag + "] " + content + "\033[0m");
    }

    public static void customOput(String str) {
        System.out.print(str);
    }

    public static void customOput(String str, int color) {
        System.out.print("\033[" + color +"m" + str + "\033[0m");
    }
}
