package org.JavaGame.Engine.Renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D
{
    private Vector2f From;
    private Vector2f To;
    private Vector3f Color;
    private int LifeTime;


    public Line2D(Vector2f from, Vector2f to, Vector3f color, int lifeTime)
    {
        this.From = from;
        this.To = to;
        this.Color = color;
        this.LifeTime = lifeTime;
    }

    public int beginFrame()
    {
        this.LifeTime--;
        return this.LifeTime;
    }
    public Vector2f getFrom()
    {
        return this.From;
    }

    public Vector2f getTo()
    {
        return this.To;
    }

    public Vector3f getColor()
    {
        return this.Color;
    }
}
