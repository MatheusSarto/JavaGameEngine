package org.JavaGame.Engine.Components;

import org.JavaGame.Editor.PropertiesWindow;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Listeners.MouseListener;
import org.JavaGame.Engine.Prefabs;
import org.JavaGame.Engine.Util.SceneManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Gizmo extends Component
{
    private Vector4f xAxisColor;
    private Vector4f xAxisColorHover;
    private Vector4f yAxisColor;
    private Vector4f yAxisColorHover;

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRender xAxisSprite;
    private SpriteRender yAxisSprite;
    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    private boolean Using = false;

    private int GizmoWidth = 16;
    private int GizmoHeight = 48;

    protected GameObject ActiveGameObject = null;

    private Vector3f xAxisOffset = new Vector3f(51.0f, -16.0f, 0.0f);
    private Vector3f yAxisOffset = new Vector3f(1.0f, 50.0f, 0.0f);

    private org.JavaGame.Editor.PropertiesWindow PropertiesWindow;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow)
    {
        this.PropertiesWindow = propertiesWindow;

        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRender.class);

        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRender.class);

        this.xAxisColor = new Vector4f(1,0.3f,0,1);
        this.yAxisColor = new Vector4f(0.3f,1,0.3f,1);

        this.xAxisColorHover = new Vector4f(1,0,0,1);
        this.yAxisColorHover = new Vector4f(0,0,0,1);

        this.xAxisObject.addComponent(new NonPickable());
        this.yAxisObject.addComponent(new NonPickable());


        SceneManager.getCurrentScene().addGameObjectToScene(this.xAxisObject);
        SceneManager.getCurrentScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void update(float dt)
    {
        if(!Using) return;

        this.ActiveGameObject = this.PropertiesWindow.getActiveGameObject();
        if(this.ActiveGameObject != null)
        {
            this.setActive();
        }
        else
        {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if((xAxisHot || xAxisActive) && MouseListener.isDraggin() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            System.out.println("IS DRAGGIN X ");
            xAxisActive = true;
            yAxisActive = false;
        }
        else if((yAxisHot || yAxisActive) && MouseListener.isDraggin() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            System.out.println("IS DRAGGIN Y ");

            xAxisActive = false;
            yAxisActive = true;
        }
        else
        {
            xAxisActive = false;
            yAxisActive = false;
        }

        if(this.ActiveGameObject != null)
        {
            this.xAxisObject.Transform.setPosition(new Vector3f(this.ActiveGameObject.Transform.getPosition().x,
                    this.ActiveGameObject.Transform.getPosition().y, this.ActiveGameObject.Transform.getPosition().z));

            this.xAxisObject.Transform.getPosition().add(this.xAxisOffset);

            this.yAxisObject.Transform.setPosition(new Vector3f(this.ActiveGameObject.Transform.getPosition().x,
                    this.ActiveGameObject.Transform.getPosition().y, this.ActiveGameObject.Transform.getPosition().z));

            this.yAxisObject.Transform.getPosition().add(this.yAxisOffset);
        }
    }

    private boolean checkXHoverState()
    {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if(mousePos.x <= xAxisObject.Transform.getPosition().x &&
                mousePos.x >= xAxisObject.Transform.getPosition().x - GizmoHeight &&
                mousePos.y >= xAxisObject.Transform.getPosition().y &&
                mousePos.y <= xAxisObject.Transform.getPosition().y + GizmoWidth
        )
        {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }
        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState()
    {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if(mousePos.x <= yAxisObject.Transform.getPosition().x
                && mousePos.x >= yAxisObject.Transform.getPosition().x - GizmoWidth &&
                mousePos.y <= yAxisObject.Transform.getPosition().y &&
                mousePos.y >= yAxisObject.Transform.getPosition().y - GizmoHeight
        )
        {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }
        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    @Override
    public void Init()
    {
        this.xAxisObject.Transform.setRotation(90);
        this.yAxisObject.Transform.setRotation(180);
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
    }


    private void setActive()
    {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive()
    {
        this.ActiveGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0,0,0,0));
        this.yAxisSprite.setColor(new Vector4f(0,0,0,0));

    }
    public void setUsing()
    {
        this.Using = true;
    }
    public void setNotUsing()
    {
        this.Using = false;
        this.setInactive();
    }
}
