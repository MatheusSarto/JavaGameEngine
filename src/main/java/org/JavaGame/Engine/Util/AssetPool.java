package org.JavaGame.Engine.Util;

import org.JavaGame.Engine.Renderer.Shader;
import org.JavaGame.Engine.Renderer.Texture;
import org.lwjgl.system.CallbackI;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool
{
    private static Map<String, Shader> ShaderMap = new HashMap<>();
    private static Map<String, Texture> TextureMap = new HashMap<>();

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
            Texture newTexture = new Texture(resourceName);
            AssetPool.TextureMap.put(file.getAbsolutePath(), newTexture);

            return newTexture;
        }
    }

}
