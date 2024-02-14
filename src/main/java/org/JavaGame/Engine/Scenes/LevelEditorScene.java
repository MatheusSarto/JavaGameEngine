package org.JavaGame.Engine.Scenes;

public class LevelEditorScene extends Scene
{
    public LevelEditorScene(String name, int id)
    {
        super(name, id);
    }
    @Override
    public void fixedUpdate(float dt)
    {
        System.out.println("LEVEL EDITOR SCENE FIXED UPDATE");
    }

    @Override
    public void update()
    {
       System.out.println("LEVEL EDITOR SCENE UPDATE");
    }
}
