package org.JavaGame.Engine.Serialize;

import com.google.gson.*;
import org.JavaGame.Engine.Components.Component;

import java.lang.reflect.Type;

public class ComponentSerializer implements JsonSerializer<Component>
{


    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("Type", new JsonPrimitive(component.getClass().getCanonicalName()));
        result.add("Properties", jsonSerializationContext.serialize(component, component.getClass()));
        return result;
    }
}
