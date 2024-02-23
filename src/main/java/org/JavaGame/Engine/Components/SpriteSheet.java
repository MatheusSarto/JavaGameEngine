package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet
{
    private Texture Texture;
    private List<Sprite> Sprites;

    public SpriteSheet()
    {

    }

    public void InitSpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numberSprites, int spacing)
    {
        this.Sprites = new ArrayList<>();

        this.Texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for(int i = 0; i < numberSprites; i++)
        {
            float topY = (currentY + spriteHeight) / (float)Texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)Texture.getWidth();
            float leftX = currentX / (float)Texture.getWidth();
            float bottomY = currentY / (float)Texture.getHeight();

            Vector2f[] texCoords =
                    {
                            new Vector2f(rightX,topY),
                            new Vector2f(rightX,bottomY),
                            new Vector2f(leftX,bottomY),
                            new Vector2f(leftX,topY)
                    };

            Sprite sprite = new Sprite(Texture, texCoords);
            this.Sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= Texture.getWidth())
            {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index)
    {
        return this.Sprites.get(index);
    }
}
