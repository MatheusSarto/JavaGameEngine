package org.JavaGame.Engine.Components;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform extends Component
{
    private Vector3f Position;
    private Vector2f Scale;
    private float Rotation = 0.0f;

    public Transform()
    {
        this.Position   = new Vector3f();
        this.Scale      = new Vector2f();
        this.Name = "Transform";
    }

    public Transform(Vector3f position, Vector2f scale)
    {
        this.Position   = position;
        this.Scale      = scale;
    }
    public Transform(Vector3f position)
    {
        this.Position   = position;
        this.Scale      = new Vector2f();
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

    @Override
    public void imgui()
    {

    }

    public float getRotation()
    {
        return Rotation;
    }

    public void setRotation(float rotation)
    {
        Rotation = rotation;
    }
}
