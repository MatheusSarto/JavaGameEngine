package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Listeners.MouseListener;

public class TranslateGizmo extends Gizmo
{
    public TranslateGizmo(Sprite arrowSprite, org.JavaGame.Editor.PropertiesWindow propertiesWindow)
    {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void update(float dt)
    {
        if(ActiveGameObject != null)
        {
            if(xAxisActive && !yAxisActive)
            {
                ActiveGameObject.Transform.getPosition().x -= MouseListener.getWorldDx();
            }
            else if(yAxisActive)
            {
                ActiveGameObject.Transform.getPosition().y -= MouseListener.getWorldDy();
            }
        }

        super.update(dt);
    }
}
