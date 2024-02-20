package org.JavaGame.Engine.Renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    private int ShaderProgramID;
    private String VertexSource;
    private String FragmentSrouce;
    private String FilePath;
    private boolean BeingUsed;

    public Shader(String filepath)
    {
        BeingUsed = false;
        this.FilePath = filepath;
        try
        {
            // Splitting file content
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Finding first pattern after '#type' notation
            int index = source.indexOf("#type") + "#type ".length();
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Finding second pattern after '#type' notation
            index = source.indexOf("#type", eol) + "#type ".length();
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equals("vertex"))
            {
                VertexSource = splitString[1];
            } else if(firstPattern.equals("fragment")) {
                FragmentSrouce = splitString[1];
            }else {
                throw new IOException("UNEXPECTED TOKEN: '" + firstPattern + "' in '" + filepath + "'");
            }

            if(secondPattern.equals("vertex"))
            {
                VertexSource = splitString[2];
            } else if(secondPattern.equals("fragment")) {
                FragmentSrouce = splitString[2];
            }else {
                throw new IOException("UNEXPECTED TOKEN: '" + firstPattern + "' in '" + filepath + "'");
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            assert false : "ERROR: COULD NOT OPEN FILE FOR SHADER: '" + filepath + "'";
        }
    }

    public void compile()
    {
        // Compile and link the shaders
        int vertexID, fragmentID;

        // First, load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code
        glShaderSource(vertexID, VertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: IN " + FilePath + " VERTEX SHADER COMPILATION FAILED");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }


        // First, load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code
        glShaderSource(fragmentID, FragmentSrouce);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: IN " + FilePath + " FRAGMENT SHADER COMPILATION FAILED");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for erros
        ShaderProgramID = glCreateProgram();
        glAttachShader(ShaderProgramID, vertexID);
        glAttachShader(ShaderProgramID, fragmentID);
        glLinkProgram(ShaderProgramID);

        // check for linkin erros
        success = glGetProgrami(ShaderProgramID, GL_LINK_STATUS);

        if(success == GL_FALSE)
        {
            int len = glGetProgrami(ShaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: IN " + FilePath + " LINKIN OF SHADERS FAILED");
            System.out.println(glGetProgramInfoLog(ShaderProgramID, len));
            assert false : "";
        }

    }

    public void bind()
    {
        if(!BeingUsed)
        {
            // Bind shader program
            glUseProgram(ShaderProgramID);
            BeingUsed = true;
        }

    }

    public void detach()
    {
        glUseProgram(0);
        BeingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }


    public void uploadVec4f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float value)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String varName, int value)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        glUniform1i(varLocation, value);
    }

    public void uploadTexture(String varName, int slot)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array)
    {
        int varLocation = glGetUniformLocation(ShaderProgramID, varName);
        bind();
        glUniform1iv(varLocation, array);
    }
}
