package org.JavaGame.Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{

    private Matrix4f ProjectionMatrix, ViewMatrix, InverseProjection, InverseView;
    private Vector2f Position;
    private Vector2f ProjectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 21.0f);

    private float Zoom = 1.0f;

    public Camera(Vector2f position)
    {
        this.Position           = position;
        this.ProjectionMatrix   = new Matrix4f();
        this.ViewMatrix         = new Matrix4f();
        this.InverseProjection  = new Matrix4f();
        this.InverseView        = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection()
    {
        ProjectionMatrix.identity();
        ProjectionMatrix.ortho(0.0f, ProjectionSize.x * this.Zoom,
                0.0f,ProjectionSize.y * this.Zoom, 0.0f, 100.0f);
        ProjectionMatrix.invert(InverseProjection);
    }

    public Matrix4f getViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.ViewMatrix.identity();
        this.ViewMatrix = ViewMatrix.lookAt(new Vector3f(Position.x, Position.y, 20.0f),
                                                cameraFront.add(Position.x, Position.y, 0.0f),
                                                cameraUp);
        this.ViewMatrix.invert(InverseView);
        return this.ViewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        return this.ProjectionMatrix;
    }
    public Matrix4f getInverseProjection()
    {
        return this.InverseProjection;
    }
    public Matrix4f getInverseView()
    {
        return this.InverseView;
    }
    public Vector2f getProjectionSize()
    {
        return this.ProjectionSize;
    }
    public Vector2f getPosition()
    {
        return this.Position;
    }
    public void setPosition(Vector2f newPosition)
    {
        this.Position.set(newPosition);
    }

    public float getZoom()
    {
        return Zoom;
    }

    public void setZoom(float zoom)
    {
        Zoom = zoom;
    }
    public void addZoom(float value)
    {
        this.Zoom += value;
    }
}
