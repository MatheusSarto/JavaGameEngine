package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Listeners.KeyListener;
import org.JavaGame.Engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class GizmoSystem extends Component
{
    private SpriteSheet Gizmos;
    private int UsingGizmo = 0;
    private TranslateGizmo TranslateGizmo;
    private ScaleGizmo ScaleGizmo;

    public GizmoSystem(SpriteSheet gizmoSprites)
    {
        Gizmos = gizmoSprites;
    }

    @Override
    public void Init()
    {
        GameObject.addComponent(new TranslateGizmo(Gizmos.getSprite(1), Window.getImGuiLayer().getPropertiesWindow()));
        GameObject.addComponent(new ScaleGizmo(Gizmos.getSprite(2),     Window.getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void update(float dt)
    {
        if(UsingGizmo == 0)
        {
            GameObject.getComponent(TranslateGizmo.class).setUsing();
            GameObject.getComponent(ScaleGizmo.class).setNotUsing();
        }
        else if(UsingGizmo == 1)
        {
            GameObject.getComponent(TranslateGizmo.class).setNotUsing();
            GameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_E))
        {
            UsingGizmo = 0;
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_R))
        {
            UsingGizmo = 1;
        }
    }
}
