package org.JavaGame.Engine.Renderer;

import org.JavaGame.Engine.Components.SpriteRender;
import org.JavaGame.Engine.Util.AssetPool;
import org.JavaGame.Engine.Util.SceneManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>
{
    // Vertex
    // =====
    // Position             Color                              Texture Coords         Texture ID
    // float, float         float, float, float, float         float, float           float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXT_COORDS_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXT_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXT_COORDS_OFFSET + TEXT_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRender[] Sprites;
    private int SpritesNumber;
    private boolean HasRoom;
    private float[] Vertices;
    private int[] TextureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> Textures;
    private int VaoID, VboID;
    private int MaxBatchSize;
    private Shader Shader;
    private float zIndex;

    public RenderBatch(int maxBatchSize, float zIndex)
    {
        this.zIndex = zIndex;
        this.MaxBatchSize = maxBatchSize;
        Shader = AssetPool.getShader("assets/shaders/default.glsl");
        this.Sprites = new SpriteRender[maxBatchSize];

        // 4 Vertices Quads
        Vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.SpritesNumber = 0;
        this.HasRoom = true;
        this.Textures = new ArrayList<>();
    }

    public void render()
    {
        boolean rebufferData = false;
        for(int i = 0; i < SpritesNumber; i++)
        {
            SpriteRender spr = Sprites[i];
            if(spr.IsDiry())
            {
                loadVertexProperties(i);
                spr.setClean();
                rebufferData = true;
            }
        }
        if(rebufferData)
        {
            glBindBuffer(GL_ARRAY_BUFFER, VboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, Vertices);
        }

        // Use shader
        Shader.bind();
        Shader.uploadMat4f("uProjection", SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
        Shader.uploadMat4f("uView", SceneManager.getCurrentScene().getCamera().getViewMatrix());
        for(int i = 0; i < Textures.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            Textures.get(i).bind();
        }
        Shader.uploadIntArray("uTextures", TextureSlots);

        glBindVertexArray(VaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.SpritesNumber * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for(int i = 0; i < Textures.size(); i++)
        {
            Textures.get(i).unbind();
        }

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

        // Texture coordinates pointer
        glVertexAttribPointer(2, TEXT_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXT_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        // Texture ID pointer
        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRender sprite)
    {
        // Get index and add renderObject
        int index = this.SpritesNumber;
        this.Sprites[index] = sprite;
        this.SpritesNumber++;

        if(sprite.getTexture() != null)
        {
            if (!Textures.contains(sprite.getTexture()))
            {
                Textures.add(sprite.getTexture());
            }
        }

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

    public boolean hasTextureRoom()
    {
        return this.Textures.size() < 8;
    }

    public boolean hasTexture(Texture texture)
    {
        return this.Textures.contains(texture);
    }

    private void loadVertexProperties(int index)
    {
        SpriteRender sprite = this.Sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4* VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] textCoords = sprite.getTextCoords();

        int textureId = 0;
        if(sprite.getTexture() != null)
        {
            for(int i = 0; i < Textures.size(); i++)
            {
                if(Textures.get(i).equals(sprite.getTexture()))
                {
                    textureId = i + 1;
                    break;
                }
            }
        }

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

             // Load Texture Coordinates
            Vertices[offset + 6] = textCoords[i].x;
            Vertices[offset + 7] = textCoords[i].y;

            // Load Texture ID's
            Vertices[offset + 8] = textureId;


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

    public float getzIndex()
    {
        return this.zIndex;
    }

    @Override
    public int compareTo(RenderBatch o)
    {
        return Integer.compare((int)zIndex, (int)o.getzIndex());
    }
}
