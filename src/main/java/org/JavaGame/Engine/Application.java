package org.JavaGame.Engine;

import org.JavaGame.Engine.Util.Timer;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Application
{
    public Application()
    {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        this.m_Window = new Window();
        m_Running = true;
    }

    public void run()
    {
        m_Window.render();
    }


    private Window m_Window;
    private boolean m_Running;
}
