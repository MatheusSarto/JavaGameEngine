package org.JavaGame.Engine.Renderer;

import org.JavaGame.Engine.Components.SpriteRender;
import org.JavaGame.Engine.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer
{
    private List<RenderBatch> RenderBatches;
    private final int MAX_BATCH_SIZE = 1000;
    private static Shader CurrentShder;

    public Renderer()
    {
        this.RenderBatches = new ArrayList<>();
    }
    public void render()
    {
        CurrentShder.bind();
        for(RenderBatch batch : RenderBatches)
        {
            batch.render();
        }
    }

    public void add(GameObject object)
    {
        SpriteRender spr = object.getComponent(SpriteRender.class);
        if (spr != null)
        {
            add(spr);
        }
    }

    private void add(SpriteRender sprite)
    {
        boolean added = false;
        for(RenderBatch batch : RenderBatches)
        {
            if(batch.hasRoom() && batch.getzIndex() == sprite.getGameObject().getZindex())
            {
                Texture texture = sprite.getTexture();
                if(texture == null || (batch.hasTexture(texture) || batch.hasTextureRoom()))
                {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        if(!added)
        {
          RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.getGameObject().getZindex());
          newBatch.Init();
          RenderBatches.add(newBatch);
          newBatch.addSprite(sprite);
          Collections.sort(RenderBatches);
        }
    }

    public static void bindShader(Shader shader)
    {
        CurrentShder = shader;
    }

    public static Shader getBoundShader()
    {
        return CurrentShder;
    }
}
