package org.JavaGame.Engine.Renderer;

import org.JavaGame.Engine.Components.SpriteRender;
import org.JavaGame.Engine.Util.SceneManager;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch
{
    // Vertex
    // =====
    // Position             Color
    // float, float         float, float, float, float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRender[] Sprites;
    private int SpritesNumber;
    private boolean HasRoom;
    private float[] Vertices;

    private int VaoID, VboID;
    private int MaxBatchSize;
    private Shader Shader;

    public RenderBatch(int maxBatchSize)
    {
        this.MaxBatchSize = maxBatchSize;
        Shader = new Shader("assets/shaders/default.glsl");
        Shader.compile();
        this.Sprites = new SpriteRender[MaxBatchSize];

        // 4 Vertices Quads
        Vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.SpritesNumber = 0;
        this.HasRoom = true;
    }

    public void render()
    {
        // For now, it's rebuffering ALL data EVERY frame
        glBindBuffer(GL_ARRAY_BUFFER, VboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Vertices);

        // Use shader
        Shader.bind();
        Shader.uploadMat4f("uProjection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        Shader.uploadMat4f("uView", SceneManager.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(VaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.SpritesNumber * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        Shader.detach();
    }

    public void Init()
    {
        // Generate and bind a Vertex Array Object
        VaoID = glGenVertexArrays();
        glBindVertexArray(VaoID);

        // Allocate space for the vertices
        VboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VboID);
        glBufferData(GL_ARRAY_BUFFER, (long) Vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        // Position pointer
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        // Color pointer
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteRender sprite)
    {
        // Get index and add renderObject
        int index = this.SpritesNumber;
        this.Sprites[index] = sprite;
        this.SpritesNumber++;

        // Add properties to local vertices array
        loadVertexProperties(index);

        if(SpritesNumber >= this.MaxBatchSize)
        {
            this.HasRoom = false;
        }
    }

    public boolean hasRoom()
    {
        return this.HasRoom;
    }

    private void loadVertexProperties(int index)
    {
        SpriteRender sprite = this.Sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4* VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertice with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i = 0; i < 4; i++)
        {
            switch (i)
            {
                case 1:
                    yAdd = 0.0f;
                    break;
                case 2:
                    xAdd = 0.0f;
                    break;
                case 3:
                    yAdd = 1.0f;
                    break;
            }
            // Load Positions
            Vertices[offset] = sprite.getGameObject().Transform.getPosition().x + (xAdd * sprite.getGameObject().Transform.getScale().x);
            Vertices[offset + 1] = sprite.getGameObject().Transform.getPosition().y + (yAdd * sprite.getGameObject().Transform.getScale().y);

            // Load Color
            Vertices[offset + 2] = color.x;
            Vertices[offset + 3] = color.y;
            Vertices[offset + 4] = color.z;
            Vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    }
    private int[] generateIndices()
    {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * MaxBatchSize];
        for(int i = 0; i < MaxBatchSize; i++)
        {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // Triangle 1
        elements[offsetArrayIndex]      = offset + 3;
        elements[offsetArrayIndex + 1]  = offset + 2;
        elements[offsetArrayIndex + 2]  = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3]  = offset + 0;
        elements[offsetArrayIndex + 4]  = offset + 2;
        elements[offsetArrayIndex + 5]  = offset + 1;


    }
}
