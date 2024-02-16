package org.JavaGame.Engine.Scenes;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Runnable;

public abstract class Scene implements Runnable
{
    protected Camera m_Camera;

    public Scene(String name, int  id)
    {
        m_Name  = name;
        m_Id    = id;
    }

    public String getName() { return this.m_Name; }
    public int getId() { return this.m_Id; }
    private String m_Name;
    private int m_Id;
}
