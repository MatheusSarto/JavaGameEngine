package org.JavaGame.Engine.Listeners;

import org.JavaGame.Engine.Util.SceneManager;
import org.JavaGame.Engine.Window;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
public class MouseListener
{
    private static MouseListener Instance;
    private double ScrollX, ScrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean MouseButtonPressed[] = new boolean[9];
    private boolean IsDragging;

    public MouseListener()
    {
        this.ScrollX = 0.0f;
        this.ScrollY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
    }

    public static MouseListener get()
    {
        if(MouseListener.Instance == null)
        {
            MouseListener.Instance = new MouseListener();
        }
        return MouseListener.Instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos)
    {
        get().lastX = get().xPos;
        get().lastY = get().yPos;

        get().xPos = xpos;
        get().yPos = ypos;

        get().IsDragging = get().MouseButtonPressed[0] || get().MouseButtonPressed[1] || get().MouseButtonPressed[2]
                || get().MouseButtonPressed[3] || get().MouseButtonPressed[4] || get().MouseButtonPressed[5] || get().MouseButtonPressed[6]
                || get().MouseButtonPressed[7] || get().MouseButtonPressed[8];
    }

    public static void mouserButtonCallback(long window, int button, int action, int mods)
    {
        if(button > get().MouseButtonPressed.length) { return; }
        if(action == GLFW_PRESS)
        {
            get().MouseButtonPressed[button] = true;
        }
        else if(action == GLFW_RELEASE)
        {
            get().MouseButtonPressed[button] = false;
            get().IsDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffSet,double yOffSet)
    {
        get().ScrollX = xOffSet;
        get().ScrollY = yOffSet;
    }

    public static void endFrame()
    {
        get().ScrollX = 0.0f;
        get().ScrollY = 0.0f;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX()
    {
        return (float)get().xPos;
    }
    public static float getY()
    {
        return (float)get().yPos;
    }
    public static float getOrthoX()
    {
        float currentX = getX();
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        temp.mul(SceneManager.getCurrentScene().getCamera().getInverseProjection())
                .mul(SceneManager.getCurrentScene().getCamera().getInverseView());
        currentX = temp.x;
        return currentX;
    }
    public static float getOrthoY()
    {
        float currentY = Window.getHeight() - getY();
        currentY = (currentY / (float) Window.getHeight()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        temp.mul(SceneManager.getCurrentScene().getCamera().getInverseProjection())
                .mul(SceneManager.getCurrentScene().getCamera().getInverseView());
        currentY = temp.y;
        return currentY;
    }
    public static float getDx()
    {
        return (float)(get().lastX - get().xPos);
    }
    public static float getDy()
    {
        return (float)(get().lastY - get().yPos);
    }
    public static float getScrollX()
    {
        return (float)get().ScrollX;
    }
    public static float getScrollY()
    {
        return (float)get().ScrollY;
    }
    public static boolean isDraggin()
    {
        return get().IsDragging;
    }
    public static boolean mouseButtonDown(int button)
    {
        if(button > get().MouseButtonPressed.length)
        {
            return false;
        }
        return get().MouseButtonPressed[button];
    }
}
