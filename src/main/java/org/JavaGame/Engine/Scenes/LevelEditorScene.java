package org.JavaGame.Engine.Scenes;

import imgui.ImGui;
import org.JavaGame.Engine.Components.Rigidbody;
import org.JavaGame.Engine.Components.SpriteRender;
import org.JavaGame.Engine.Components.SpriteSheet;
import org.JavaGame.Engine.Components.Transform;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class LevelEditorScene extends Scene
{
    private GameObject obj1;
    private GameObject obj2;

    public LevelEditorScene(String name, int id)
    {
        super(name, id);

    }
    @Override
    public void update(float dt)
    {
        super.update(dt);
    }

    @Override
    public void Init()
    {
        super.Init();
        if(LevelLoaded)
        {
            this.ActiveGameobject = GameObjects.get(0);
            return;
        }

        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("obj1", new Transform());
        obj1.Transform.InitTransform(new Vector3f(100, 100, 1), new Vector2f(256, 256));
        obj1.addComponent(new SpriteRender());
        obj1.getComponent(SpriteRender.class).InitSprite(sprites.getSprite(20));
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);

        obj2 = new GameObject("obj2", new Transform());
        obj2.Transform.InitTransform(new Vector3f(400, 100,0), new Vector2f(256, 256));

        obj2.addComponent(new SpriteRender());
        obj2.getComponent(SpriteRender.class).InitSprite(sprites.getSprite(1));
        this.addGameObjectToScene(obj2);
    }

    @Override
    public void imGui()
    {
        ImGui.begin("TEST WINDOW");
        ImGui.text("RANDOM");
        ImGui.end();
    }
}
