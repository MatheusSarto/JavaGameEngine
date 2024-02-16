package org.JavaGame.Engine.Scenes;

import static org.lwjgl.opengl.GL11.*;

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
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

    }

    @Override
    public void update()
    {
        System.out.println("LEVEL SCENE UPDATE");
    }

    @Override
    public void Init() { }
}
