package org.JavaGame.Engine.Util;

import org.JavaGame.Engine.Scenes.Scene;

import java.util.ArrayList;
import java.util.List;

public class SceneManager
{
    private static List<Scene> Scenes;
    private static int CurrentScene;


    public SceneManager()
    {
        Scenes = new ArrayList<>();
        CurrentScene = 0;
    }
    public void loadScene(int sceneid)
    {
        Scene newscene = Scenes.stream().filter(scene -> sceneid == scene.getId())
                .findAny()
                .orElseThrow( RuntimeException::new );
        CurrentScene = Scenes.indexOf(newscene);
        getCurrentScene().load();
        getCurrentScene().Init();
    }
    public void loadScene(String name)
    {
        Scene newscene = Scenes.stream().filter(scene -> name.equals(scene.getName()))
                .findAny()
                .orElseThrow( RuntimeException::new );
        CurrentScene = Scenes.indexOf(newscene);
        getCurrentScene().load();
        getCurrentScene().Init();
    }

    public void addScene(Scene scene)
    {
        Scenes.add(scene);
    }
    public static Scene getCurrentScene()
    {
        return Scenes.get(CurrentScene);
    }
    public String getCurrentSceneName()
    {
        return Scenes.get(CurrentScene).getName();
    }
    public int getCurrentSceneId()
    {
        return Scenes.get(CurrentScene).getId();
    }


    public void updateScene(float dt)
    {
        Scenes.get(CurrentScene).update(dt);
    }
    public void renderScene() { Scenes.get(CurrentScene).render(); }
}
