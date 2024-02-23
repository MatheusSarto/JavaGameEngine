package org.JavaGame.Engine.Serialize;

import com.google.gson.*;
import org.JavaGame.Engine.Components.Component;
import org.JavaGame.Engine.Components.Transform;
import org.JavaGame.Engine.GameObject;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject>
{

    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("Name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("Components");
        Transform transform = jsonDeserializationContext.deserialize(jsonObject.get("Transform"), Transform.class);

        GameObject go = new GameObject(name, transform);
        for(JsonElement e : components)
        {
            Component c = jsonDeserializationContext.deserialize(e, Component.class);
            go.addComponent(c);
        }
        return go;
    }
}
