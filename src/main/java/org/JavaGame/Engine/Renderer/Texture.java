package org.JavaGame.Engine.Renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    private String FilePath;
    private int TextureID;

    public Texture(String filepath)
    {
        this.FilePath = filepath;
        // Generate texture on GPU
        TextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, TextureID);

        // Set texture parameters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Solving problem of textures being rendered upside down
        stbi_set_flip_vertically_on_load(true);

        // Load image
        IntBuffer width     = BufferUtils.createIntBuffer(1);
        IntBuffer height    = BufferUtils.createIntBuffer(1);
        IntBuffer channels  = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if(image != null)
        {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        }
        else
        {
            assert false : "ERROR: (TEXTURE) COULD NOT LOAD IMAGE '" + FilePath + "'";
        }

        stbi_image_free(image);
    }
    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, TextureID);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
