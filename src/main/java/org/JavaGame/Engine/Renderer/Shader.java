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
    public Shader(String filepath)
    {
        m_BeingUsed = false;
        this.m_FilePath = filepath;
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
                m_VertexSource = splitString[1];
            } else if(firstPattern.equals("fragment")) {
                m_FragmentSrouce = splitString[1];
            }else {
                throw new IOException("UNEXPECTED TOKEN: '" + firstPattern + "' in '" + filepath + "'");
            }

            if(secondPattern.equals("vertex"))
            {
                m_VertexSource = splitString[2];
            } else if(secondPattern.equals("fragment")) {
                m_FragmentSrouce = splitString[2];
            }else {
                throw new IOException("UNEXPECTED TOKEN: '" + firstPattern + "' in '" + filepath + "'");
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            assert false : "ERROR: COULD NOT OPEN FILE FOR SHADER: '" + filepath + "'";
        }

        System.out.println(m_VertexSource);
        System.out.println(m_FragmentSrouce);
    }

    public void compile()
    {
        // Compile and link the shaders
        int vertexID, fragmentID;

        // First, load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code
        glShaderSource(vertexID, m_VertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: IN " + m_FilePath + " VERTEX SHADER COMPILATION FAILED");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }


        // First, load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code
        glShaderSource(fragmentID, m_FragmentSrouce);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: IN " + m_FilePath + " FRAGMENT SHADER COMPILATION FAILED");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for erros
        m_ShaderProgramID = glCreateProgram();
        glAttachShader(m_ShaderProgramID, vertexID);
        glAttachShader(m_ShaderProgramID, fragmentID);
        glLinkProgram(m_ShaderProgramID);

        // check for linkin erros
        success = glGetProgrami(m_ShaderProgramID, GL_LINK_STATUS);

        if(success == GL_FALSE)
        {
            int len = glGetProgrami(m_ShaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: IN " + m_FilePath + " LINKIN OF SHADERS FAILED");
            System.out.println(glGetProgramInfoLog(m_ShaderProgramID, len));
            assert false : "";
        }

    }

    public void bind()
    {
        if(!m_BeingUsed)
        {
            // Bind shader program
            glUseProgram(m_ShaderProgramID);
            m_BeingUsed = true;
        }

    }

    public void detach()
    {
        glUseProgram(0);
        m_BeingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }


    public void uploadVec4f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float value)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String varName, int value)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        glUniform1i(varLocation, value);
    }

    public void uploadTexture(String varName, int slot)
    {
        int varLocation = glGetUniformLocation(m_ShaderProgramID, varName);
        bind();
        glUniform1i(varLocation, slot);
    }


    private int m_ShaderProgramID;
    private String m_VertexSource;
    private String m_FragmentSrouce;
    private String m_FilePath;
    private boolean m_BeingUsed;
}
