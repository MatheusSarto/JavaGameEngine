package org.JavaGame.Editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.JavaGame.Engine.Listeners.MouseListener;
import org.JavaGame.Engine.Window;
import org.joml.Vector2f;

public class GameViewWindow
{
    private float LeftX, RightX, TopY, BottomY;
    public void imgui()
    {
        ImGui.begin("Game ViewPort", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();

        LeftX = topLeft.x;
        BottomY = topLeft.y;
        RightX = topLeft.x + windowSize.x;
        TopY = topLeft.y + windowSize.y;

        int textureId = Window.getFramebuffer().getTextureID();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    private ImVec2 getLargestSizeForViewport()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if(aspectHeight > windowSize.y)
        {
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectsize)
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectsize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectsize.y / 2.0f) ;

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public boolean getWantCaptureMouse()
    {
        return MouseListener.getX() >= LeftX && MouseListener.getX() <= RightX &&
                MouseListener.getY() >= BottomY && MouseListener.getY() <= TopY;
    }
}
