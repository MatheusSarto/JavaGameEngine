package org.JavaGame.Engine.Listeners;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Util.SceneManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
public class MouseListener
{
    private static MouseListener Instance;
    private double ScrollX, ScrollY;
    private double xPos, yPos, LastY, LastX, WorldX, WorldY, LastWorldX, LastWorldY;
    private boolean MouseButtonPressed[] = new boolean[3];
    private boolean IsDragging;

    private int MouseButtonsDown = 0;

    private static Vector2f GameViewportPos = new Vector2f();
    private static Vector2f GameViewportSize = new Vector2f();

    public MouseListener()
    {
        this.ScrollX = 0.0f;
        this.ScrollY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
        this.LastX = 0.0f;
        this.LastY = 0.0f;
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
        if(get().MouseButtonsDown > 0)
        {
            get().IsDragging = true;
        }

        get().LastX = get().xPos;
        get().LastY = get().yPos;

        get().LastWorldX = get().WorldX;
        get().LastWorldY = get().WorldY;

        get().xPos = xpos;
        get().yPos = ypos;

        calcOrthoX();
        calcOrthoY();
    }

    public static void mouserButtonCallback(long window, int button, int action, int mods)
    {
        if(button > get().MouseButtonPressed.length) { return; }
        if(action == GLFW_PRESS)
        {
            get().MouseButtonsDown++;
            get().MouseButtonPressed[button] = true;
        }
        else if(action == GLFW_RELEASE)
        {
            get().MouseButtonsDown--;
            if(button < get().MouseButtonPressed.length)
            {
                get().MouseButtonPressed[button] = false;
                get().IsDragging = false;
            }

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
        get().LastX = get().xPos;
        get().LastY = get().yPos;
        get().LastWorldX = get().WorldX;
        get().LastWorldY = get().WorldY;
    }


    public static float getX()
    {
        return (float)get().xPos;
    }
    public static float getY()
    {
        return (float)get().yPos;
    }
    public static float getDx()
    {
        return (float)(get().LastX - get().xPos);
    }

    public static float getWorldDx()
    {
        return (float)(get().LastWorldX - get().WorldX);

    } public static float getWorldDy()
    {
        return (float)(get().LastWorldY - get().WorldY);

    }

    public static float getDy()
    {
        return (float)(get().LastY - get().yPos);
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
    public static void setGameViewportPos(Vector2f gameViewportPos)
    {
        GameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f gameViewportSize)
    {
        GameViewportSize.set(gameViewportSize);
    }
    public static boolean mouseButtonDown(int button)
    {
        if(button > get().MouseButtonPressed.length)
        {
            return false;
        }
        return get().MouseButtonPressed[button];
    }
    public static float getOrthoX()
    {
        return (float)get().WorldX;
    }

    private static void calcOrthoX()
    {
        float currentX = getX() - get().GameViewportPos.x;
        currentX = (currentX / get().GameViewportSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        Matrix4f viewProjection = new Matrix4f();

        Camera camera = SceneManager.getCurrentScene().getCamera();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        get().WorldX = tmp.x;
    }

    public static float getOrthoY()
    {
        return (float)get().WorldY;
    }

    private static void calcOrthoY()
    {
        float currentY = getY() - get().GameViewportPos.y;
        currentY = -((currentY / get().GameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
        Matrix4f viewProjection = new Matrix4f();

        Camera camera = SceneManager.getCurrentScene().getCamera();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);

        tmp.mul(viewProjection);

        get().WorldY = tmp.y;
    }

    public static float getScreenX()
    {
        float currentX = getX() - get().GameViewportPos.x;
        currentX = (currentX / get().GameViewportSize.x) * 1920.0f;

        return currentX;
    }

    public static float getScreenY()
    {
        float currentY = getY() - get().GameViewportPos.y;
        currentY =  1080.0f - ((currentY / get().GameViewportSize.y) * 1080.0f);

        return currentY;
    }
}
