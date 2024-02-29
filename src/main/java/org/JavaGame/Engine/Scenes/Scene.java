package org.JavaGame.Engine.Scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Components.Component;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Renderer.Renderer;
import org.JavaGame.Engine.Runnable;
import org.JavaGame.Engine.Serialize.ComponentDeserializer;
import org.JavaGame.Engine.Serialize.ComponentSerializer;
import org.JavaGame.Engine.Serialize.GameObjectDeserializer;
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

    }
    public abstract void render();
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

    protected void loadResources()
    {

    }

    public void imgui() { }

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
            List<GameObject> objsToSerialize = GameObjects.stream().filter(GameObject::doSerialization).toList();

            writer.write(gson.toJson(objsToSerialize));
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

        if(!inFile.isEmpty())
        {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for(int i = 0; i < objs.length; i++)
            {
                addGameObjectToScene(objs[i]);

                for(Component c : objs[i].getAllComponents())
                {
                    if(c.getUID() > maxCompId)
                    {
                        maxCompId = c.getUID();
                    }
                }
                if(objs[i].getUID() > maxGoId)
                {
                    maxGoId = objs[i].getUID();
                }
            }
            maxGoId++;
            maxCompId++;
            GameObject.staticIinit(maxGoId);
            Component.staticInit(maxCompId);
            LevelLoaded = true;
        }
    }
    public void setLevelLoaded(boolean levelLoaded)
    {
        this.LevelLoaded = levelLoaded;
    }
    public boolean getLevelLoaded()
    {
        return this.LevelLoaded;
    }

    public List<GameObject> getGameObjects()
    {
        return this.GameObjects;
    }
    public GameObject getGameObject(int uid)
    {
        return this.GameObjects.stream().filter(go -> go.getUID() == uid).findFirst().orElse(null);
    }
}
