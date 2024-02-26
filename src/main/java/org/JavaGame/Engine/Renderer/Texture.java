package org.JavaGame.Engine.Renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    private String FilePath;
    private transient int TextureID;
    private int Width;
    private int Height;

    public Texture()
    {
        TextureID = -1;
        Width = -1;
        Height = -1;
    }
    public Texture(int width, int height)
    {
        this.FilePath = "Generated";

        this.Width = width;
        this.Height = height;
        TextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, TextureID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }

    public void InitTexture(String filepath)
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

        // Load image
        IntBuffer width     = BufferUtils.createIntBuffer(1);
        IntBuffer height    = BufferUtils.createIntBuffer(1);
        IntBuffer channels  = BufferUtils.createIntBuffer(1);
        // Solving problem of textures being rendered upside down
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if(image != null)
        {
            this.Width = width.get(0);
            this.Height = height.get(0);

            if(channels.get(0) == 3)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_INT, image);
            }
            else if(channels.get(0) == 4)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else
            {
                assert false : "ERROR: (TEXTURE) UNKOWN NUMBER OF CHANNELS '" + channels.get(0) + "'";
            }
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

    public int getWidth()
    {
        return this.Width;
    }

    public  int getHeight()
    {
        return this.Height;
    }
    public String getFilePath()
    {
        return this.FilePath;
    }
    public int getTextureID()
    {
        return this.TextureID;
    }
    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(!(o instanceof  Texture)) return false;
        Texture oTex = (Texture)o;
        return oTex.getWidth() == this.getWidth() && oTex.Height == this.getHeight() && oTex.getTextureID() == this.TextureID
                && oTex.getFilePath().equals(this.FilePath);
    }
}
