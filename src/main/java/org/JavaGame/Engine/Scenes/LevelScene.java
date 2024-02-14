package org.JavaGame.Engine.Scenes;

public class LevelScene extends Scene
{
    public LevelScene(String name, int id)
    {
        super(name, id);
    }

    @Override
    public void fixedUpdate(float dt)
    {
        System.out.println("LEVEL SCENE FIXED UPDATE");

    }

    @Override
    public void update()
    {
        System.out.println("LEVEL SCENE UPDATE");
    }
}
