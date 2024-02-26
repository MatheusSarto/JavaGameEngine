package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Listeners.MouseListener;
import org.JavaGame.Engine.Util.SceneManager;
import org.JavaGame.Engine.Util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component
{
    GameObject HoldingObject = null;

    public void pickUpObject(GameObject  go)
    {
        this.HoldingObject = go;
        SceneManager.getCurrentScene().addGameObjectToScene(go);
    }


    public void place()
    {
        this.HoldingObject = null;
    }

    @Override
    public void update(float dt)
    {
        if(HoldingObject != null)
        {
            HoldingObject.Transform.getPosition().x = MouseListener.getOrthoX();
            HoldingObject.Transform.getPosition().y = MouseListener.getOrthoY();
            HoldingObject.Transform.getPosition().x = (int)(HoldingObject.Transform.getPosition().x / Settings.GRID_WIDTH ) * Settings.GRID_WIDTH;
            HoldingObject.Transform.getPosition().y = (int)(HoldingObject.Transform.getPosition().y / Settings.GRID_HEIGHT ) * Settings.GRID_HEIGHT;

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
            {
                place();
            }
        }
    }
}
