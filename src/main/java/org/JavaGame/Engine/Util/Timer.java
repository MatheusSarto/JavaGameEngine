package org.JavaGame.Util;

public class Timer
{
    public static float TimeStarted = System.nanoTime();

    public static float getTime()
    {
        return (float)((System.nanoTime() - TimeStarted) * 1E-9);
    }
}
