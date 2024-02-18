package org.JavaGame.Engine.Scenes;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Runnable;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene implements Runnable
{
    protected Camera m_Camera;
    private boolean m_IsRunning = false;
    protected List<GameObject> m_GameObjects = new ArrayList<>();

    public Scene(String name, int  id)
    {
        m_Name  = name;
        m_Id    = id;
    }

    @Override
    public void Init()
    {
        for(GameObject gameObject : m_GameObjects)
        {
            gameObject.Init();
        }
        m_IsRunning = true;
    }
    public void addGameObjectToScene(GameObject gameObject)
    {
        if(!m_IsRunning)
        {
            m_GameObjects.add(gameObject);
        }
        else
        {
            m_GameObjects.add(gameObject);
            gameObject.Init();
        }
    }

    public String getName() { return this.m_Name; }
    public int getId() { return this.m_Id; }
    private String m_Name;
    private int m_Id;
}
