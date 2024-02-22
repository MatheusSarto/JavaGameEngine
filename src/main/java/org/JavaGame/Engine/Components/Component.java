package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Runnable;

public abstract class Component implements Runnable
{
    private GameObject GameObject = null;

    public GameObject getGameObject() {
        return GameObject;
    }

    public void setGameObject(GameObject gameobject)
    {
        this.GameObject = gameobject;
    }

    public void Init() {}
    public void update(float dt) { }

    public void imgui() { }
}
