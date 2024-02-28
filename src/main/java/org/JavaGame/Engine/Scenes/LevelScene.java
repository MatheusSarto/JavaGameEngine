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

    }

    @Override
    public void render() {
        this.Renderer.render();
    }

    @Override
    public void Init() { }
}
