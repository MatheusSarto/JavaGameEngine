package org.JavaGame.Engine.Scenes;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Renderer.Shader;
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
    public void fixedUpdate(float dt)
    {
        System.out.println("LEVEL EDITOR SCENE FIXED UPDATE");



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
        glUseProgram(0);
    }

    @Override
    public void update(float dt)
    {
        System.out.println("LEVEL EDITOR SCENE UPDATE");

        // Bind shader program
        m_DefaultShader.bind();
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
    }

    @Override
    public void Init()
    {
        this.m_Camera = new Camera(new Vector2f());
        m_DefaultShader = new Shader("assets/shaders/default.glsl");
        m_DefaultShader.compile();

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
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, true, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, true, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }
    private int vaoID, vboID, eboID;
    private Shader m_DefaultShader;

    private float[] vertexArray = {
     // positions                   // colors
     100.5f,   0.5f,     0.0f,      1.0f, 0.0f, 0.0f, 1.0f,  // Bottom Right
     0.5f,     100.5f,   0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // Top Left
     100.5f,   100.5f,   0.0f,      0.0f, 0.0f, 1.0f, 1.0f, // Top Right
     0.5f,     0.5f,     0.0f,      1.0f, 1.0f, 0.0f, 1.0f  // Bottom Left
    };

    /// IMPORTANT: Must be in counter-clockwise order
    private int[] elementArary = {
        2, 1, 0, // Top Right Triangle
        0, 1, 3 // Bottom Left Triangle
    };
}
