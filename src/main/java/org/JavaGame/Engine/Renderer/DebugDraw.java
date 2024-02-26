package org.JavaGame.Engine.Renderer;

import org.JavaGame.Engine.Util.AssetPool;
import org.JavaGame.Engine.Util.SceneManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw
{
    private static int MAX_LINES = 500;

    private static List<Line2D> Lines = new ArrayList<>();
    // 6 floats per vertex, 2 vertices per line
    private static float[]  VertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader  Shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int VaoID;
    private static int VboID;

    private static boolean Started = false;

    public static void start()
    {
        // Generate de VAO
        VaoID = glGenVertexArrays();
        glBindVertexArray(VaoID);

        // Create the VBO and buffer some memory
        VboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VaoID);
        glBufferData(GL_ARRAY_BUFFER, (long) VertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the vertex array attributes
        glVertexAttribPointer(0,3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(4.0f);
    }

    public static void beginFrame()
    {
        if(!Started)
        {
            start();
            Started = true;
        }

        // Remove all dead lines
        Lines.removeIf(line -> line.beginFrame() < 0);
    }

    public static void draw()
    {
        if(Lines.isEmpty()) { return; }

        int index = 0;
        for(Line2D line : Lines)
        {
            System.out.println("DRAWING LINE");
            for(int i = 0; i < 2; i++)
            {
                Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                // Load position
                VertexArray[index]      = position.x;
                VertexArray[index + 1]  = position.y;
                VertexArray[index + 2]  = -10;


                // Load color
                VertexArray[index + 3]  = color.x;
                VertexArray[index + 4]  = color.y;
                VertexArray[index + 5]  = color.z;

                index += 6;
            }
        }

        // Bind VBO
        glBindBuffer(GL_ARRAY_BUFFER, VboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(VertexArray, 0, Lines.size() * 6 * 2));

        // Use shader
        Shader.bind();
        Shader.uploadMat4f("uProjection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        Shader.uploadMat4f("uView", SceneManager.getCurrentScene().getCamera().getViewMatrix());

        // Bind VAO
        glBindVertexArray(VaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, Lines.size());

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbid shader
        Shader.detach();
    }


    public static void addLine2D(Vector2f from, Vector2f to)
    {
        addLine2D(from, to, new Vector3f(0, 1, 0), 1);
    }
    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color)
    {
        addLine2D(from, to, color, 1);
    }


    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime)
    {
        if(Lines.size() >= MAX_LINES) return;
        DebugDraw.Lines.add(new Line2D(from, to, color, lifetime));
    }
}
