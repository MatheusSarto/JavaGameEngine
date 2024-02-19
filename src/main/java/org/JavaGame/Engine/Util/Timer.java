package org.JavaGame.Engine.Util;

import java.time.Duration;
import java.time.Instant;

public class Timer
{
    private static final float TimeStarted = System.nanoTime();

    public static float getTime()
    {
        return (float)((System.nanoTime() - TimeStarted) * 1E-9);
    }
}
