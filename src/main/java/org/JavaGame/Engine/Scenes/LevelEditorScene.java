package org.JavaGame.Engine.Scenes;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Components.FontRender;
import org.JavaGame.Engine.Components.SprintRender;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Renderer.Shader;
import org.JavaGame.Engine.Renderer.Texture;
import org.JavaGame.Engine.Util.Timer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class LevelEditorScene extends Scene
{
    public LevelEditorScene(String name, int id)
    {
        super(name, id);

    }
    @Override
    public void update(float dt)
    {
        // Bind shader program
        m_DefaultShader.bind();

        // Upload texture to shader
        m_DefaultShader.uploadTexture("TEXTURE_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        m_TestTexture.bind();

        // Upload data to shader
        m_DefaultShader.uploadMat4f("uProjection", m_Camera.getProjectionMatrix());
        m_DefaultShader.uploadMat4f("uView", m_Camera.getViewMatrix());
        m_DefaultShader.uploadFloat("uTime", Timer.getTime());

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArary.length, GL_UNSIGNED_INT, 0);

        // Unbid everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        m_DefaultShader.detach();

        // Update all game objects
        for(GameObject gameobject : m_GameObjects)
        {
            gameobject.update(dt);
        }
    }

    @Override
    public void Init()
    {
        // Calls 'Scene' Init method
        super.Init();

        // Creating GameObjectS to be added on Scene before Scene's update method
        GameObject m_GameObject = new GameObject("test object");
        m_GameObject.addComponent(new SprintRender());
        m_GameObject.addComponent(new FontRender());
        this.addGameObjectToScene(m_GameObject);

        GameObject gameObject2 = new GameObject("GAME OBJECT 2");
        gameObject2.addComponent(new SprintRender());
        this.addGameObjectToScene(gameObject2);

        // Creating Camera and setting shaders and textures
        this.m_Camera = new Camera(new Vector2f());
        m_DefaultShader = new Shader("assets/shaders/default.glsl");
        m_DefaultShader.compile();
        this.m_TestTexture = new Texture("assets/images/TRISTANA.jpg");

        // Generate VAO, VBO and EBO buffer objects
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArary.length);
        elementBuffer.put(elementArary).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, true, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, true, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }
    private int vaoID, vboID, eboID;
    private Shader m_DefaultShader;
    private Texture m_TestTexture;
    private float[] vertexArray = {
     // positions                   // colors                    // UV Coordinates
     100.5f,   0.5f,     0.0f,      1.0f, 0.0f, 0.0f, 1.0f,      1, 0,   // Bottom Right
     0.5f,     100.5f,   0.0f,      0.0f, 1.0f, 0.0f, 1.0f,      0, 1,   // Top Left
     100.5f,   100.5f,   0.0f,      0.0f, 0.0f, 1.0f, 1.0f,      1, 1,   // Top Right
     0.5f,     0.5f,     0.0f,      1.0f, 1.0f, 0.0f, 1.0f,      0, 0    // Bottom Left
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArary = {
        2, 1, 0, // Top Right Triangle
        0, 1, 3 // Bottom Left Triangle
    };
}
