package org.JavaGame.Engine.Components;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Rigidbody extends Component
{
    private int ColliderType = 0;
    private float Friction = 0.8f;
    public Vector3f Velocity = new Vector3f(0, 0.5f, 1);
    public transient Vector4f temp = new Vector4f(0, 0, 0, 0);
}
