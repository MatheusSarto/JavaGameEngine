package org.JavaGame.Engine.Listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener
{
    private static KeyListener insntance;
    private boolean KeyPressed[] = new boolean[350];


    public static KeyListener get()
    {
        if(KeyListener.insntance == null)
        {
            KeyListener.insntance = new KeyListener();
        }
        return KeyListener.insntance;
    }
    private KeyListener() { }

    public static void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        if(action == GLFW_PRESS)
        {
            get().KeyPressed[key] = true;
        }
        else if(action == GLFW_RELEASE)
        {
            get().KeyPressed[key] = false;
        }
    }

    public  static boolean isKeyPressed(int keycode)
    {
        if(keycode >  get().KeyPressed.length)
        {
            return false;
        }
        return get().KeyPressed[keycode];
    }
}
