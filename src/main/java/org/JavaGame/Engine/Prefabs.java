package org.JavaGame.Engine;

import org.JavaGame.Engine.Components.Sprite;
import org.JavaGame.Engine.Components.SpriteRender;
import org.JavaGame.Engine.Components.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Prefabs
{
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY)
    {
        GameObject block = new GameObject("sprite_obj_gen", new Transform(new Vector3f(), new Vector2f(sizeX, sizeY)));

        SpriteRender render = new SpriteRender();
        render.setSprite(sprite);
        block.addComponent(render);
        return block;
    }
}
