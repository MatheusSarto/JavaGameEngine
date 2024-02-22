package org.JavaGame.Engine;

import org.JavaGame.Engine.ImGui.ImGuiLayer;
import org.JavaGame.Engine.Listeners.KeyListener;
import org.JavaGame.Engine.Listeners.MouseListener;
import org.JavaGame.Engine.Scenes.LevelEditorScene;
import org.JavaGame.Engine.Scenes.LevelScene;
import org.JavaGame.Engine.Util.SceneManager;
import org.lwjgl.opengl.GL;

import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private SceneManager SceneManager;
    private static int Width;
    private static int Height;
    private String Title;
    private long GlfwWindow;
    private ImGuiLayer ImGuiLayer;


    public Window()
    {
        Width = 1920;
        Height = 1080;
        this.Title = "Testing";
        init();

        SceneManager = new SceneManager();
        SceneManager.addScene(new LevelEditorScene("LEVEL EDITOR SCENE", 0));
        SceneManager.addScene(new LevelScene("LEVEL SCENE", 1));
        SceneManager.loadScene(0);
    }

    public void run()
    {
        System.out.println("RENDER THREAD");
        float beginTime = (float)glfwGetTime();
        float endTime = (float)glfwGetTime();

        while(!glfwWindowShouldClose(GlfwWindow))
        {
            // Pool Events
            glfwPollEvents();

            endTime = (float)glfwGetTime();
            float dt = endTime - beginTime;
            beginTime = endTime;

            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            SceneManager.updateScene(dt);

            this.ImGuiLayer.update(dt, SceneManager.getCurrentScene());

            glfwSwapBuffers(GlfwWindow);

            if(KeyListener.isKeyPressed(KeyEvent.VK_1))
            {
                SceneManager.setCurrentScene(1);
                SceneManager.getCurrentScene().Init();
            }
            if(KeyListener.isKeyPressed(KeyEvent.VK_0))
            {
                SceneManager.setCurrentScene(0);
                SceneManager.getCurrentScene().Init();
            }

        }

        // Free the Memory
        glfwFreeCallbacks(GlfwWindow);
        glfwDestroyWindow(GlfwWindow);

        // Terminate GLFW and free memory of error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void glfwFreeCallbacks(long mGlfwWindow) { }

    private void init()
    {
        if(!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the Window
        GlfwWindow = glfwCreateWindow(this.Width, this.Height, this.Title, NULL, NULL);
        if(GlfwWindow == NULL)
        {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Mouse CallBacks
        glfwSetCursorPosCallback(GlfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(GlfwWindow, MouseListener::mouserButtonCallback);
        glfwSetScrollCallback(GlfwWindow, MouseListener::mouseScrollCallback);

        // Key Listeners
        glfwSetKeyCallback(GlfwWindow, KeyListener::keyCallback);

        // Window Resize Callback
        glfwSetWindowSizeCallback(GlfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(GlfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the Window Visible
        glfwShowWindow(GlfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.ImGuiLayer = new ImGuiLayer(GlfwWindow);
        this.ImGuiLayer.InitImGui();


    }

    public static void setHeight(int newHeight)
    {
        Height = newHeight;
    }

    public static int getWidth()
    {
        return Width;
    }
    public static void setWidth(int newWidth)
    {
        Width = newWidth;
    }



    public static int getHeight()
    {
        return Height;
    }
}
