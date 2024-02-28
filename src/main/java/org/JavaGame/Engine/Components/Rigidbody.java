package org.JavaGame.Engine.Components;

import imgui.ImGui;
import imgui.type.ImFloat;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Rigidbody extends Component
{
    private int ColliderType = 0;
    private float Friction = 0.8f;
    public Vector3f Velocity = new Vector3f(0, 0.5f, 1);
    public transient Vector4f temp = new Vector4f(0, 0, 0, 0);

    public Rigidbody ()
    {
        this.Name = "Rigidbody";
    }

    @Override
    public void imgui()
    {
        ImGui.begin(this.Name);
        ImFloat[] ImVelocity = {new ImFloat(this.Velocity.x) , new ImFloat(this.Velocity.y), new ImFloat(this.Velocity.z)};
        if(ImGui.inputFloat("Velocity x: ", ImVelocity[0]))
        {
            this.Velocity.x = Float.parseFloat(ImVelocity[0].toString());
        }
        if(ImGui.inputFloat("Velocity y: ", ImVelocity[1]))
        {
            this.Velocity.y = Float.parseFloat(ImVelocity[1].toString());
        }
        if(ImGui.inputFloat("Velocity z: ", ImVelocity[2]))
        {
            this.Velocity.z = Float.parseFloat(ImVelocity[2].toString());
        }
        ImGui.end();

    }
}
