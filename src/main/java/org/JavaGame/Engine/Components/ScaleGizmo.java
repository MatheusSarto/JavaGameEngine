package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Listeners.MouseListener;

public class ScaleGizmo extends Gizmo
{
    public ScaleGizmo(Sprite scaleSprite, org.JavaGame.Editor.PropertiesWindow propertiesWindow)
    {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void update(float dt)
    {
        if(ActiveGameObject != null)
        {
            if(xAxisActive && !yAxisActive)
            {
                ActiveGameObject.Transform.getScale().x -= MouseListener.getWorldDx();
            }
            else if(yAxisActive)
            {
                ActiveGameObject.Transform.getScale().y -= MouseListener.getWorldDy();
            }
        }

        super.update(dt);
    }
}
