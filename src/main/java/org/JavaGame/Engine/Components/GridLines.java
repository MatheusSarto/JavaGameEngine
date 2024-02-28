package org.JavaGame.Engine.Components;

import org.JavaGame.Engine.Renderer.DebugDraw;
import org.JavaGame.Engine.Util.SceneManager;
import org.JavaGame.Engine.Util.Settings;
import org.joml.Vector2f;

public class GridLines extends Component
{
    public GridLines()
    {
        this.Name = "Grid Lines";
    }
    @Override
    public void update(float dt)
    {
        Vector2f cameraPos = SceneManager.getCurrentScene().getCamera().getPosition();
        Vector2f projectionSize = SceneManager.getCurrentScene().getCamera().getProjectionSize();

        int firstX = (int)(((cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH);
        int firstY = (int)(((cameraPos.y / Settings.GRID_HEIGHT)  -1) * Settings.GRID_WIDTH);

        int numVerticalLines = (int)(projectionSize.x / Settings.GRID_WIDTH);
        int numHorizontalLines = (int)(projectionSize.y / Settings.GRID_HEIGHT);

        int height = (int)projectionSize.y + Settings.GRID_HEIGHT * 2;
        int width = (int)projectionSize.x + Settings.GRID_WIDTH * 2;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);

        for(int i = 0; i < maxLines; i++)
        {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if(i < numVerticalLines)
            {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height));
            }
            if(i < numHorizontalLines)
            {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y ));

            }
        }

    }

}
