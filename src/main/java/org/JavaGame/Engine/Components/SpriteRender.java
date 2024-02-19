package org.JavaGame.Engine.Components;

import org.joml.Vector4f;

public class SpriteRender extends Component {
    private Vector4f Color;


    public SpriteRender(Vector4f color)
    {
        this.Color = color;
    }

    @Override
    public void update(float dt)
    {
    }

    @Override
    public void Init()
    {

    }

    public Vector4f getColor()
    {
        return this.Color;
    }

}
