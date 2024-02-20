package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite
{
    private Vector4f Color;
    private Vector2f[] TextCoords;
    private Texture Texture;

    public Sprite(Texture texture)
    {
        this.Texture = texture;
        this.TextCoords = new Vector2f[]{
                new Vector2f(1,1),
                new Vector2f(1,0),
                new Vector2f(0,0),
                new Vector2f(0,1)
        };
    }

    public Sprite(Texture texture, Vector2f[] textCoords)
    {
        this.Texture = texture;
        this.TextCoords = textCoords;
    }

    public Texture getTexture()
    {
        return  this.Texture;
    }

    public Vector2f[] getTextCoords()
    {
        return this.TextCoords;
    }
}
