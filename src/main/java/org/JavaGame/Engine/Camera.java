package org.JavaGame.Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    public Camera(Vector2f position)
    {
        this.m_Position = position;
        this.m_ProjectionMatrix = new Matrix4f();
        this.m_ViewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection()
    {
        m_ProjectionMatrix.identity();
        m_ProjectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.m_ViewMatrix.identity();
        this.m_ViewMatrix = m_ViewMatrix.lookAt(new Vector3f(m_Position.x, m_Position.y, 20.0f),
                                                cameraFront.add(m_Position.x, m_Position.y, 0.0f),
                                                cameraUp);
        return this.m_ViewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        return this.m_ProjectionMatrix;
    }
    private Matrix4f m_ProjectionMatrix, m_ViewMatrix;
    private Vector2f m_Position;





}
