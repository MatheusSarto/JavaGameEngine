package org.JavaGame.Engine;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
public class MouseListener
{
    public static MouseListener get()
    {
        if(MouseListener.instance == null)
        {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos)
    {
        get().m_lastX = get().m_xPos;
        get().m_lastY = get().m_yPos;

        get().m_xPos = xpos;
        get().m_yPos = ypos;

        get().m_isDragging = get().m_mouseButtonPressed[0] || get().m_mouseButtonPressed[1] || get().m_mouseButtonPressed[2];
    }

    public static void mouserButtonCallback(long window, int button, int action, int mods)
    {
        if(button > get().m_mouseButtonPressed.length) { return; }
        if(action == GLFW_PRESS)
        {
            get().m_mouseButtonPressed[button] = true;
        }
        else if(action == GLFW_RELEASE)
        {
            get().m_mouseButtonPressed[button] = false;
            get().m_isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffSet,double yOffSet)
    {
        get().m_ScrollX = xOffSet;
        get().m_ScrollY = yOffSet;
    }

    public static void endFrame()
    {
        get().m_ScrollX = 0.0f;
        get().m_ScrollY = 0.0f;
        get().m_lastX   = get().m_xPos;
        get().m_lastY   = get().m_yPos;
    }

    public static float getX()
    {
        return (float)get().m_xPos;
    }
    public static float getY()
    {
        return (float)get().m_yPos;
    }
    public static float getDx()
    {
        return (float)(get().m_lastX - get().m_xPos);
    }
    public static float getDy()
    {
        return (float)(get().m_lastY - get().m_yPos);
    }
    public static float getScrollX()
    {
        return (float)get().m_ScrollX;
    }
    public static float getScrollY()
    {
        return (float)get().m_ScrollY;
    }
    public static boolean isDraggin()
    {
        return get().m_isDragging;
    }
    public static boolean mouseButtonDown(int button)
    {
        if(button > get().m_mouseButtonPressed.length)
        {
            return false;
        }
        return get().m_mouseButtonPressed[button];
    }


    private static MouseListener instance;
    private double m_ScrollX, m_ScrollY;
    private double m_xPos, m_yPos, m_lastY, m_lastX;
    private boolean m_mouseButtonPressed[] = new boolean[3];
    private boolean m_isDragging;

    public MouseListener()
    {
        this.m_ScrollX  = 0.0f;
        this.m_ScrollY  = 0.0f;
        this.m_xPos     = 0.0f;
        this.m_yPos     = 0.0f;
        this.m_lastX    = 0.0f;
        this.m_lastY    = 0.0f;
    }


}
