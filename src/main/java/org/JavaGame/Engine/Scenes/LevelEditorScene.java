package org.JavaGame.Engine.Scenes;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Components.SpriteRender;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Components.Transform;
import org.JavaGame.Engine.Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class LevelEditorScene extends Scene
{
    public LevelEditorScene(String name, int id)
    {
        super(name, id);

    }
    @Override
    public void update(float dt)
    {
        // Update all game objects
        for(GameObject gameobject : GameObjects)
        {
            gameobject.update(dt);
        }
        this.Renderer.render();
    }

    @Override
    public void Init()
    {
        // Calls 'Scene' Init method
        super.Init();

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);

        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for(int x = 0; x < 100; x++)
        {
            for(int y = 0; y < 100; y++)
            {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject go = new GameObject("OBJ " + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRender(new Vector4f(xPos /  totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }

        loadResources();
    }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");
    }
}
