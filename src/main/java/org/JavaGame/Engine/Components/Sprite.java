package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Renderer.Texture;
import org.joml.Vector2f;

public class Sprite
{
    private Vector2f[] TextCoords;
    private Texture Texture;
    private float Width, Height;

    public Sprite()
    {
        this.Texture = new Texture();
        this.TextCoords = new Vector2f[]{
                new Vector2f(1,1),
                new Vector2f(1,0),
                new Vector2f(0,0),
                new Vector2f(0,1)
        };
    }

    public void InitSprite(Texture texture)
    {
        this.Texture = texture;
        this.TextCoords = new Vector2f[]{
                new Vector2f(1,1),
                new Vector2f(1,0),
                new Vector2f(0,0),
                new Vector2f(0,1)
        };
    }

    public void InitSprite(Texture texture, Vector2f[] textCoords)
    {
        this.Texture = texture;
        this.TextCoords = textCoords;
    }

    public Texture getTexture()
    {
        return  this.Texture;
    }

    public Vector2f[] getTexCoords()
    {
        return this.TextCoords;
    }

    public float getWidth() {
        return this.Width;
    }

    public void setWidth(float width) {
        Width = width;
    }

    public float getHeight() {
        return this.Height;
    }

    public void setHeight(float height) {
        Height = height;
    }
    public int getTexId()
    {
        return Texture == null ? -1 : Texture.getTextureID();
    }
    public void setTexture(Texture texture)
    {
        this.Texture =  texture;
    }
}
