package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRender extends Component {
    private Vector4f Color;
    private Sprite Sprite;
    private Transform LastTransform;
    private boolean IsDirty;

    public SpriteRender(Vector4f color)
    {
        this.Color = color;
        this.Sprite = new Sprite(null);
        this.IsDirty = true;
    }
    public SpriteRender(Sprite sprite)
    {
        this.Sprite = sprite;
        this.Color = new Vector4f(1, 1, 1, 1);
        this.IsDirty = true;
    }

    @Override
    public void update(float dt)
    {
        if(!this.LastTransform.equals(this.getGameObject().Transform))
        {
            this.getGameObject().Transform.copy(this.LastTransform);
            IsDirty = true;
        }
    }

    @Override
    public void Init()
    {
        LastTransform = getGameObject().Transform.copy();
    }

    public Vector4f getColor()
    {
        return this.Color;
    }

    public Texture getTexture()
    {
        return Sprite.getTexture();
    }

    public Vector2f[] getTextCoords()
    {
        return Sprite.getTextCoords();
    }
    public void setSprite(Sprite sprite)
    {
        this.Sprite = sprite;
        IsDirty = true;
    }
    public void setColor(Vector4f color)
    {
        if(!this.Color.equals(color))
        {
            IsDirty = true;
            this.Color.set(color);
        }
    }

    public boolean IsDiry()
    {
        return this.IsDirty;
    }

    public void setClean()
    {
        this.IsDirty = false;
    }
}
