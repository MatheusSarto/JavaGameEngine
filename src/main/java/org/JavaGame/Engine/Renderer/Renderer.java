package org.JavaGame.Engine.Renderer;

import org.JavaGame.Engine.Components.SpriteRender;
import org.JavaGame.Engine.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer
{
    private List<RenderBatch> RenderBatches;
    private final int MAX_BATCH_SIZE = 1000;


    public Renderer()
    {
        this.RenderBatches = new ArrayList<>();
    }
    public void render()
    {
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
            if(batch.hasRoom())
            {
                batch.addSprite(sprite);
                added = true;
                break;
            }
        }
        if(!added)
        {
          RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
          newBatch.Init();
          RenderBatches.add(newBatch);
          newBatch.addSprite(sprite);
        }
    }
}
