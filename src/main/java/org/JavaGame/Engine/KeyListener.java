package org.JavaGame.Engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener
{
    public static KeyListener get()
    {
        if(KeyListener.insntance == null)
        {
            KeyListener.insntance = new KeyListener();
        }
        return KeyListener.insntance;
    }

    private static KeyListener insntance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener() { }

    public static void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        if(action == GLFW_PRESS)
        {
            get().keyPressed[key] = true;
        }
        else if(action == GLFW_RELEASE)
        {
            get().keyPressed[key] = false;
        }
    }

    public  static boolean isKeyPressed(int keycode)
    {
        if(keycode >  get().keyPressed.length)
        {
            return false;
        }
        return get().keyPressed[keycode];
    }
}
