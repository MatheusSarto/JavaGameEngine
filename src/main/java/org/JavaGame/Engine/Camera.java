package org.JavaGame.Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{

    private Matrix4f ProjectionMatrix, ViewMatrix;
    private Vector2f Position;


    public Camera(Vector2f position)
    {
        this.Position = position;
        this.ProjectionMatrix = new Matrix4f();
        this.ViewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection()
    {
        ProjectionMatrix.identity();
        ProjectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.ViewMatrix.identity();
        this.ViewMatrix = ViewMatrix.lookAt(new Vector3f(Position.x, Position.y, 20.0f),
                                                cameraFront.add(Position.x, Position.y, 0.0f),
                                                cameraUp);
        return this.ViewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        return this.ProjectionMatrix;
    }
}
