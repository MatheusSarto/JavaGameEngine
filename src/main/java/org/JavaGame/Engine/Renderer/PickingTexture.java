package org.JavaGame.Engine.Renderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class PickingTexture
{
     private int PickingTextureID;
     private int FboID;
     private int DepthTexture;

     public PickingTexture(int width, int height)
     {
         if (!Init(width, height))
         {
             assert false : "ERROR INITIALIZING PICKING TEXTURE";
         }
     }

     public boolean Init(int width, int height)
     {
         // Generate frame buffer
         FboID = glGenFramebuffers();
         glBindFramebuffer(GL_FRAMEBUFFER, FboID);

         // Create the texture to render the data to, and attach it to frame buffer
         PickingTextureID = glGenTextures();
         glBindTexture(GL_TEXTURE_2D, PickingTextureID);
         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
         glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F,  width, height, 0, GL_RGB, GL_FLOAT, 0);
         glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.PickingTextureID, 0);

         // Create the texture object for the depth buffer
         glEnable(GL_TEXTURE_2D);
         DepthTexture = glGenTextures();
         glBindTexture(GL_TEXTURE_2D, DepthTexture);
         glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLAT, 0);
         glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, DepthTexture, 0);

         // Create render buffer
         int rboID = glGenRenderbuffers();
         glBindRenderbuffer(GL_RENDERBUFFER, rboID);
         glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
         glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

         // Disable the reading
         glReadBuffer(GL_NONE);
         glDrawBuffer(GL_COLOR_ATTACHMENT0);

         if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
         {
             assert false : "ERROR : FRAMEBUFFER IS NOT COMPLETE";
             return  false;
         }
         // Unbind the texture and framebuffer
         glBindTexture(GL_TEXTURE_2D, 0);
         glBindFramebuffer(GL_FRAMEBUFFER, 0);

         return true;
     }

     public void enableWriting()
     {
         glBindFramebuffer(GL_DRAW_FRAMEBUFFER, FboID);
     }

    public void disableWriting()
    {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    public int readPixel(int x, int y)
    {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, FboID);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        float pixels[] = new float[3];
        glReadPixels(x, y,1, 1, GL_RGB, GL_FLOAT, pixels);
        return (int)(pixels[0]) - 1;
    }
}
