package org.JavaGame.Engine.Scenes;

public class LevelScene extends Scene
{
    public LevelScene(String name, int id)
    {
        super(name, id);
    }

    @Override
    public void update(float dt)
    {
        System.out.println("LEVEL SCENE UPDATE");
    }

    @Override
    public void Init() { }
}
