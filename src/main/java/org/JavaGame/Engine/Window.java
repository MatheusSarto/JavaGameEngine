package org.JavaGame.Engine;

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
    private int Width, Height;
    private String Title;
    private long GlfwWindow;


    public Window()
    {
        this.Width = 1920;
        this.Height = 1080;
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
    }
}
