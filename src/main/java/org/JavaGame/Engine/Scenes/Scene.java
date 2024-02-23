package org.JavaGame.Engine.Scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Components.Component;
import org.JavaGame.Engine.Components.SpriteSheet;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Renderer.Renderer;
import org.JavaGame.Engine.Runnable;
import org.JavaGame.Engine.Serialize.ComponentDeserializer;
import org.JavaGame.Engine.Serialize.ComponentSerializer;
import org.JavaGame.Engine.Serialize.GameObjectDeserializer;
import org.JavaGame.Engine.Util.AssetPool;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene implements Runnable
{
    protected Camera Camera;
    protected List<GameObject> GameObjects = new ArrayList<>();
    protected Renderer Renderer;
    private boolean IsRunning = false;
    private final String Name;
    private final int Id;
    protected GameObject ActiveGameobject = null;
    protected boolean LevelLoaded = false;

    public Scene(String name, int  id)
    {
        this.Name = name;
        this.Id = id;
    }

    @Override
    public void Init()
    {
        loadResources();
        this.Camera = new Camera(new Vector2f());
        this.Renderer = new Renderer();

        for(GameObject gameObject : GameObjects)
        {
            gameObject.Init();
            this.Renderer.add(gameObject);
        }
        IsRunning = true;

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
    public void addGameObjectToScene(GameObject gameObject)
    {
        if(!IsRunning)
        {
            GameObjects.add(gameObject);
        }
        else
        {
            GameObjects.add(gameObject);
            gameObject.Init();
            this.Renderer.add(gameObject);
        }
    }
    public Camera getCamera()
    {
        return this.Camera;
    }

    public String getName() { return this.Name; }
    public int getId() { return this.Id; }

    private void loadResources()
    {
        AssetPool.getShader("assets/shaders/default.glsl");

        SpriteSheet spriteSheet = new SpriteSheet();
        spriteSheet.InitSpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                16, 16, 26, 0);

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",spriteSheet);
    }

    public void sceneImgui()
    {
        if(ActiveGameobject != null)
        {
            ImGui.begin("Inspector");
            ActiveGameobject.imgui();
            ImGui.end();
        }

        imGui();
    }
    public void imGui() { }

    public void saveExit()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try
        {
            FileWriter writer = new FileWriter("saveFiles/level.txt");
            writer.write(gson.toJson(this.GameObjects));
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public void load()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try
        {
            inFile = new String(Files.readAllBytes(Paths.get("saveFiles/level.txt")));
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        if(!inFile.equals(""))
        {
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for(int i = 0; i < objs.length; i++)
            {
                addGameObjectToScene(objs[i]);
            }
            this.LevelLoaded = true;
        }
    }
}
