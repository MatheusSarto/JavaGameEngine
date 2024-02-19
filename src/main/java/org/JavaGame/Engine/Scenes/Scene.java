package org.JavaGame.Engine.Scenes;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Renderer.Renderer;
import org.JavaGame.Engine.Runnable;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene implements Runnable
{
    protected Camera Camera;
    protected List<GameObject> GameObjects = new ArrayList<>();
    protected final Renderer Renderer;
    private boolean IsRunning = false;
    private final String Name;
    private final int Id;


    public Scene(String name, int  id)
    {
        this.Name = name;
        this.Id = id;
        this.Camera = new Camera(new Vector2f());
        this.Renderer = new Renderer();
    }

    @Override
    public void Init()
    {
        for(GameObject gameObject : GameObjects)
        {
            gameObject.Init();
            this.Renderer.add(gameObject);
        }
        IsRunning = true;
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
}
