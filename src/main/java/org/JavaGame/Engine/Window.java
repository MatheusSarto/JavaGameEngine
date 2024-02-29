package org.JavaGame.Engine;

import org.JavaGame.Engine.ImGui.ImGuiLayer;
import org.JavaGame.Engine.Listeners.KeyListener;
import org.JavaGame.Engine.Listeners.MouseListener;
import org.JavaGame.Engine.Renderer.*;
import org.JavaGame.Engine.Scenes.LevelEditorScene;
import org.JavaGame.Engine.Scenes.LevelScene;
import org.JavaGame.Engine.Util.AssetPool;
import org.JavaGame.Engine.Util.SceneManager;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private SceneManager SceneManager;
    private static int Width;
    private static int Height;
    private final String Title;
    private long GlfwWindow;
    private static ImGuiLayer ImGuiLayer;
    private static FrameBuffer FrameBuffer;

    public static org.JavaGame.Engine.Renderer.PickingTexture getPickingTexture() {
        return PickingTexture;
    }

    private static PickingTexture PickingTexture;


    public Window()
    {
        Width = 1920;
        Height = 1080;
        this.Title = "JAVA GAME ENGINE";
        init();
    }

    public void run()
    {
        float beginTime = (float)glfwGetTime();
        float dt = 0;
        float endTime = (float)glfwGetTime();

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while(!glfwWindowShouldClose(GlfwWindow))
        {
            // Pool Events
            glfwPollEvents();

            glDisable(GL_BLEND);
            PickingTexture.enableWriting();

            glViewport(0, 0,Width,Height);
            glClearColor(0.0f,0.0f,0.0f,0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            SceneManager.renderScene();

            PickingTexture.disableWriting();
            glEnable(GL_BLEND);

            DebugDraw.beginFrame();

            this.FrameBuffer.bind();
            glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0)
            {
                Renderer.bindShader(defaultShader);
                SceneManager.updateScene(dt);
                SceneManager.renderScene();
            }
            this.FrameBuffer.unbind();


            this.ImGuiLayer.update(dt, SceneManager.getCurrentScene());

            glfwSwapBuffers(GlfwWindow);
            MouseListener.endFrame();

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        SceneManager.getCurrentScene().saveExit();

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


        this.FrameBuffer = new FrameBuffer(Width, Height);
        this.PickingTexture = new PickingTexture(Width, Height);
        glViewport(0, 0,Width, Height);

        this.ImGuiLayer = new ImGuiLayer(GlfwWindow, PickingTexture);
        this.ImGuiLayer.InitImGui();

        SceneManager = new SceneManager();
        SceneManager.addScene(new LevelEditorScene("LEVEL EDITOR SCENE", 0));
        SceneManager.addScene(new LevelScene("LEVEL SCENE", 1));
        SceneManager.loadScene(0);
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
    public static FrameBuffer getFramebuffer()
    {
        return FrameBuffer;
    }

    public static float getTargetAspectRatio()
    {
        return 16.0f / 9.0f;
    }
    public static int getHeight()
    {
        return Height;
    }
    public static ImGuiLayer getImGuiLayer()
    {
        return Window.ImGuiLayer;
    }
}
