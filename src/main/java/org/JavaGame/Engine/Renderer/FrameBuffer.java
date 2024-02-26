package org.JavaGame.Engine.Renderer;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer
{
    private int FboID = 0;
    private Texture Texture = null;
    public FrameBuffer(int width, int height)
    {
        // Generate frame buffer
        FboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, FboID);

        // Create the texture to render the data to, and attach it to frame buffer
        this.Texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.Texture.getTextureID(), 0);

        // Create render buffer
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            assert false : "ERROR : FRAMEBUFFER IS NOT COMPLETE";
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, FboID);
    }

    public void unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFboID() {
        return FboID;
    }
    public int getTextureID() {
        return Texture.getTextureID();
    }
}
