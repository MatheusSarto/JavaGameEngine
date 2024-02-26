package org.JavaGame.Engine.Scenes;

import imgui.ImGui;
import imgui.ImVec2;
import org.JavaGame.Engine.Components.*;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Prefabs;
import org.JavaGame.Engine.Renderer.DebugDraw;
import org.JavaGame.Engine.Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class LevelEditorScene extends Scene
{
    private SpriteSheet Sprites;
    private GameObject LevelEditorObjects = new GameObject("LevelEditor", new Transform());

    public LevelEditorScene(String name, int id)
    {
        super(name, id);

    }
    @Override
    public void update(float dt)
    {
        super.update(dt);

        LevelEditorObjects.update(dt);
        DebugDraw.draw();

    }

    @Override
    public void Init()
    {
        LevelEditorObjects.addComponent(new MouseControls());
        LevelEditorObjects.addComponent(new GridLines());

        DebugDraw.addLine2D(new Vector2f(0f, 0f), new Vector2f(800.0f, 800.0f), new Vector3f(2,0,0), 2000);
        DebugDraw.addLine2D(new Vector2f(0f, 0f), new Vector2f(800.0f, 800.0f), new Vector3f(2,0,0), 2);


        super.Init();
        Sprites = AssetPool.getSpriteSheet("assets/images/decorationsAndBlocks.png");
        if(LevelLoaded && !GameObjects.isEmpty())
        {
            this.ActiveGameobject = GameObjects.get(0);
            this.ActiveGameobject.addComponent(new Rigidbody());
            return;
        }
    }

    @Override
    protected void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        SpriteSheet spriteSheet = new SpriteSheet();
        spriteSheet.InitSpriteSheet(AssetPool.getTexture("assets/images/decorationsAndBlocks.png"),
                16, 16, 81, 0);

        AssetPool.addSpriteSheet("assets/images/decorationsAndBlocks.png",spriteSheet);


        List<GameObject> goWithTextures = GameObjects.stream().filter(o -> o.getComponent(SpriteRender.class) != null
                && o.getComponent(SpriteRender.class).getTexture() != null).toList();

        for(GameObject o : goWithTextures)
        {
            o.getComponent(SpriteRender.class).setTexture(AssetPool.getTexture(
                    o.getComponent(SpriteRender.class).getTexture().getFilePath()
            ));
        }
    }

    @Override
    public void imGui()
    {
        // FIXME FIX WINDOW NOT ALWAYS RESIZING
        ImGui.begin("Assets");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2  = windowPos.x * windowSize.x;
        for(int i = 0; i < Sprites.size(); i++)
        {
            Sprite sprite = Sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y))
            {
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                // Attach this to mouse cursor
                LevelEditorObjects.getComponent(MouseControls.class).pickUpObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < Sprites.size() && nextButtonX2 < windowX2)
            {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
