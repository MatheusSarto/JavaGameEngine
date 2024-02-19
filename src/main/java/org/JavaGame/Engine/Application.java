package org.JavaGame.Engine;

import org.lwjgl.glfw.GLFWErrorCallback;

public class Application
{

    private Window Window;
    private boolean Running;


    public Application()
    {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        this.Window = new Window();
        Running = true;
    }

    public void run()
    {
        Window.run();
    }
}
