package com.dvcoders.other;

/**
 * @author Jake Loo (03 July, 2015)
 */
public class Views {
    public static class Public {}
    public static class SelfPublic extends Public {}
    public static class Internal extends SelfPublic {}
}
