package org.JavaGame.Editor;

import imgui.ImGui;
import org.JavaGame.Engine.Components.NonPickable;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Listeners.MouseListener;
import org.JavaGame.Engine.Renderer.PickingTexture;
import org.JavaGame.Engine.Scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow
{
    private GameObject ActiveGameobject;
    private PickingTexture PickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture)
    {
        ActiveGameobject = null;
        PickingTexture = pickingTexture;
    }


    public void update(float dt, Scene currentScene)
    {


        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();

            int uid = PickingTexture.readPixel(x, y);

            GameObject pickedObj = currentScene.getGameObject(uid);
            if(pickedObj != null && pickedObj.getComponent(NonPickable.class) == null && !MouseListener.isDraggin())
            {
                ActiveGameobject = pickedObj;
            }
            else if(pickedObj == null && !MouseListener.isDraggin())
            {
                ActiveGameobject = null;
            }
        }
    }

    public void imgui()
    {
        if(ActiveGameobject != null)
        {
            ImGui.begin("Properties");
            ActiveGameobject.imgui();
            ImGui.end();
        }
    }

    public GameObject  getActiveGameobject()
    {
        return this.ActiveGameobject;
    }
    public void setActiveGameObject(GameObject go)
    {
        this.ActiveGameobject = go;
    }

    public GameObject getActiveGameObject()
    {
        return this.ActiveGameobject;
    }
}
