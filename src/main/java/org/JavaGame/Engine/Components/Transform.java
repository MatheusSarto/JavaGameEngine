package org.JavaGame.Engine.Components;

import org.joml.Vector2f;

public class Transform
{
    private Vector2f Position;
    private Vector2f Scale;


    private void Init(Vector2f position, Vector2f scale)
    {
        this.Position   = position;
        this.Scale      = scale;
    }
    public Transform()
    {
        Init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position)
    {
        Init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale)
    {
       Init(position, scale);
    }

    public Vector2f getPosition()
    {
        return  Position;
    }

    public  Vector2f getScale()
    {
        return Scale;
    }

    public void setPosition(Vector2f position)
    {
        this.Position = position;
    }

    public void setScale(Vector2f scale)
    {
        this.Scale = scale;
    }

    public void setPositionScale(Vector2f position, Vector2f scale)
    {
        this.Position   = position;
        this.Scale      = scale;
    }

}
