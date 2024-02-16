package org.JavaGame.Engine.Scenes;

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


        // Bind shader program
        glUseProgram(shaderProgram);
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
    public void update()
    {
        System.out.println("LEVEL EDITOR SCENE UPDATE");


        // Bind shader program
        glUseProgram(shaderProgram);
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
    public void Init()
    {
        System.out.println("LEVEL EDITOR SCENE INIT METHOD");
        // Compile and link the shaders

        // First, load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: VERTEX SHADER COMPILATION FAILED");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }


        // First, load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: FRAGMENT SHADER COMPILATION FAILED");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for erros
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // check for linkin erros
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);

        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: LINKIN OF SHADERS FAILED");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

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

    private String vertexShaderSrc =
            "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}\n";
    private String fragmentShaderSrc =
            "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;
    private int vaoID, vboID, eboID;

    private float[] vertexArray = {
    // positions                    // colors
     0.5f,   -0.5f,  0.0f,           1.0f, 0.0f, 0.0f, 1.0f,  // Bottom Right
     -0.5f,  0.5f,   0.0f,           0.0f, 1.0f, 0.0f, 1.0f, // Top Left
     0.5f,   0.5f,   0.0f,           0.0f, 0.0f, 1.0f, 1.0f, // Top Right
     -0.5f,  -0.5f,  0.0f,           1.0f, 1.0f, 0.0f, 1.0f  // Bottom Left
    };

    /// IMPORTANT: Must be in counter-clockwise order
    private int[] elementArary = {
        2, 1, 0, // Top Right Triangle
        0, 1, 3 // Bottom Left Triangle
    };
}
