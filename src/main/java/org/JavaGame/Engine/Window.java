package org.JavaGame.Engine;

import org.JavaGame.Engine.Scenes.LevelEditorScene;
import org.JavaGame.Engine.Scenes.LevelScene;
import org.JavaGame.Engine.Scenes.Scene;
import org.JavaGame.Engine.Util.SceneManager;
import org.JavaGame.Engine.Util.Timer;
import org.lwjgl.opengl.GL;

import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    public Window()
    {
        this.m_Width = 1920;
        this.m_Height = 1080;
        this.m_Title = "Testing";
        init();

        sceneManager = new SceneManager();
        sceneManager.addScene(new LevelEditorScene("LEVEL EDITOR SCENE", 0));
        sceneManager.addScene(new LevelScene("LEVEL SCENE", 1));
        sceneManager.setCurrentScene(0);
        sceneManager.getCurrentScene().Init();
    }

    public void run()
    {
        System.out.println("RENDER THREAD");
        float beginTime = Timer.getTime();
        float endTime = Timer.getTime();

        while(!glfwWindowShouldClose(m_GlfwWindow))
        {
            // Pool Events
            glfwPollEvents();

            endTime = Timer.getTime();
            float dt = endTime - beginTime;
            beginTime = endTime;

            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            sceneManager.getCurrentScene().update(dt);
            glfwSwapBuffers(m_GlfwWindow);

            if(KeyListener.isKeyPressed(KeyEvent.VK_1))
            {
                sceneManager.setCurrentScene(1);
                sceneManager.getCurrentScene().Init();
            }
            if(KeyListener.isKeyPressed(KeyEvent.VK_0))
            {
                sceneManager.setCurrentScene(0);
                sceneManager.getCurrentScene().Init();
            }

        }

        // Free the Memory
        glfwFreeCallbacks(m_GlfwWindow);
        glfwDestroyWindow(m_GlfwWindow);

        // Terminate GLFW and free memory of error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }



    private void glfwFreeCallbacks(long mGlfwWindow) { }
    private SceneManager sceneManager;
    private int m_Width, m_Height;
    private String m_Title;
    private long m_GlfwWindow;

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
        m_GlfwWindow = glfwCreateWindow(this.m_Width, this.m_Height, this.m_Title, NULL, NULL);
        if(m_GlfwWindow == NULL)
        {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Mouse CallBacks
        glfwSetCursorPosCallback(m_GlfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(m_GlfwWindow, MouseListener::mouserButtonCallback);
        glfwSetScrollCallback(m_GlfwWindow, MouseListener::mouseScrollCallback);

        // Key Listeners
        glfwSetKeyCallback(m_GlfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(m_GlfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the Window Visible
        glfwShowWindow(m_GlfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }
}
