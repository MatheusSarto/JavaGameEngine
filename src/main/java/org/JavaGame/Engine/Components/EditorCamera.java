package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Camera;
import org.JavaGame.Engine.Listeners.KeyListener;
import org.JavaGame.Engine.Listeners.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_INSERT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class EditorCamera extends Component
{
    private float dragDebounce = 0.032f;
    private Camera LevelEditorCamera;
    private Vector2f ClickOrigin;

    private float LerpTime = 0.0f;
    private float DragSensitivity = 30.0f;
    private float ScrollSensitivity  = 0.1f;
    private boolean Reset = false;


    public EditorCamera(Camera levelEditorCamera)
    {
        this.LevelEditorCamera = levelEditorCamera;
        this.ClickOrigin = new Vector2f();
    }
    @Override
    public void update(float dt)
    {
        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && dragDebounce > 0)
        {
            this.ClickOrigin =  new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT))
        {
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos)
                    .sub(this.ClickOrigin);

            LevelEditorCamera.getPosition().sub(delta.mul(dt).mul(DragSensitivity));
            this.ClickOrigin.lerp(mousePos, dt);
        }

        if(dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT))
        {
            dragDebounce = 0.1f;
        }

        if(MouseListener.getScrollY() != 0.0f)
        {
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * ScrollSensitivity),
                    1 / LevelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());
            LevelEditorCamera.addZoom(addValue);
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_INSERT))
        {
            Reset = true;
        }

        if(Reset)
        {
            LevelEditorCamera.getPosition().lerp(new Vector2f(), LerpTime);
            LevelEditorCamera.setZoom(this.LevelEditorCamera.getZoom() +
                    ((1.0f - LevelEditorCamera.getZoom()) * LerpTime));

            this.LerpTime += 0.1f * dt;

            if(Math.abs(LevelEditorCamera.getPosition().x) <= 5.0f &&
                    Math.abs(LevelEditorCamera.getPosition().y) <= 5.0f)
            {
                this.LerpTime = 0;
                LevelEditorCamera.setPosition(new Vector2f(0.0f, 0.0f));
                this.LevelEditorCamera.setZoom(1);
                Reset = false;
            }
        }
    }
}
