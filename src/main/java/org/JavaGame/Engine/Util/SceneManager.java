package org.JavaGame.Engine.Util;

import org.JavaGame.Engine.Scenes.Scene;

import java.util.ArrayList;
import java.util.List;

public class SceneManager
{
    public SceneManager()
    {
        m_Scenes = new ArrayList<>();
        m_CurrentScene = 0;
    }

    private List<Scene> m_Scenes;
    private int m_CurrentScene;

    public Scene loadScene(int sceneid)
    {
        Scene newscene = m_Scenes.stream().filter(scene -> sceneid == scene.getId())
                .findAny()
                .orElseThrow( RuntimeException::new );
        m_CurrentScene = m_Scenes.indexOf(newscene);
        getCurrentScene().Init();

        return newscene;
    }
    public Scene loadScene(String name)
    {
        Scene newscene = m_Scenes.stream().filter(scene -> name.equals(scene.getName()))
                .findAny()
                .orElseThrow( RuntimeException::new );
        m_CurrentScene = m_Scenes.indexOf(newscene);
        getCurrentScene().Init();

        return newscene;
    }

    public void addScene(Scene scene)
    {
        m_Scenes.add(scene);
    }

    public String getCurrentSceneName()
    {
        return m_Scenes.get(m_CurrentScene).getName();
    }

    public int getCurrentSceneId()
    {
        return m_Scenes.get(m_CurrentScene).getId();
    }

    public Scene getCurrentScene()
    {
        return m_Scenes.get(m_CurrentScene);
    }

    public void updateScene()
    {
        m_Scenes.get(m_CurrentScene).update();
    }

    public void setCurrentScene(int sceneid)
    {
        Scene sceneget = m_Scenes.stream().filter(scene -> sceneid == scene.getId())
            .findAny()
            .orElseThrow( RuntimeException::new );

        m_CurrentScene = m_Scenes.indexOf(sceneget);
    }
}
