package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet
{
    private Texture Texture;
    private List<Sprite> Sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numberSprites, int spacing)
    {
        this.Sprites = new ArrayList<>();

        this.Texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for(int i = 0; i < numberSprites; i++)
        {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords =
                    {
                            new Vector2f(rightX,topY),
                            new Vector2f(rightX,bottomY),
                            new Vector2f(leftX,bottomY),
                            new Vector2f(leftX,topY)
                    };

            Sprite sprite = new Sprite(texture, texCoords);
            this.Sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth())
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
