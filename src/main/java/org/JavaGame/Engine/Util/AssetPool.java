package org.JavaGame.Engine.Util;
import org.JavaGame.Engine.Components.SpriteSheet;
import org.JavaGame.Engine.Renderer.Shader;
import org.JavaGame.Engine.Renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool
{
    private static Map<String, Shader> ShaderMap = new HashMap<>();
    private static Map<String, Texture> TextureMap = new HashMap<>();
    private static Map<String, SpriteSheet> SpriteSheetMap = new HashMap<>();

    public static Shader getShader(String resourceName)
    {
        File file = new File(resourceName);
        if (AssetPool.ShaderMap.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.ShaderMap.get(file.getAbsolutePath());
        }
        else
        {
            Shader newShader = new Shader(resourceName);
            newShader.compile();
            AssetPool.ShaderMap.put(file.getAbsolutePath(), newShader);

            return newShader;
        }
    }

    public static Texture getTexture(String resourceName)
    {
        File file = new File(resourceName);
        if (AssetPool.TextureMap.containsKey(file.getAbsolutePath()))
        {
            return AssetPool.TextureMap.get(file.getAbsolutePath());
        }
        else
        {
            Texture newTexture = new Texture();
            newTexture.InitTexture(resourceName);
            AssetPool.TextureMap.put(file.getAbsolutePath(), newTexture);

            return newTexture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet)
    {
        File file = new File(resourceName);
        if(!AssetPool.SpriteSheetMap.containsKey(file.getAbsolutePath()))
        {
            AssetPool.SpriteSheetMap.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName)
    {
        File file = new File(resourceName);
        if(!AssetPool.SpriteSheetMap.containsKey(file.getAbsolutePath()))
        {
            assert false : "ERROR: TRIED TO ACCESS SPRITESHEET '" + resourceName + "' AND IT HAS NOT BEEN ADDED TO ASSET POOL.";
        }
        return AssetPool.SpriteSheetMap.getOrDefault(file.getAbsolutePath(), null);
    }
}
