package org.JavaGame.Engine.Components;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform
{
    private Vector3f Position;
    private Vector2f Scale;


    private void Init(Vector3f position, Vector2f scale)
    {
        this.Position   = position;
        this.Scale      = scale;
    }
    public Transform()
    {
        Init(new Vector3f(), new Vector2f());
    }

    public Transform(Vector3f position)
    {
        Init(position, new Vector2f());
    }

    public Transform(Vector3f position, Vector2f scale)
    {
       Init(position, scale);
    }

    public Vector3f getPosition()
    {
        return  Position;
    }

    public  Vector2f getScale()
    {
        return Scale;
    }

    public void setPosition(Vector3f position)
    {
        this.Position = position;
    }

    public void setScale(Vector2f scale)
    {
        this.Scale = scale;
    }

    public void setPositionScale(Vector3f position, Vector2f scale)
    {
        this.Position   = position;
        this.Scale      = scale;
    }

    public Transform copy()
    {
        return new Transform(new Vector3f(this.Position), new Vector2f(this.Scale));
    }

    public void copy(Transform to)
    {
        to.Position.set(this.Position);
        to.Scale.set(this.Scale);
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(!(o instanceof Transform)) return false;

        Transform t = (Transform)o;
        return t.Position.equals(this.Position) && t.Scale.equals(this.Scale);
    }
}
