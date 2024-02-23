package org.JavaGame.Engine.Serialize;

import com.google.gson.*;
import org.JavaGame.Engine.Components.Component;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonDeserializer<Component>
{
    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String typeS = jsonObject.get("Type").getAsString();
        JsonElement element = jsonObject.get("Properties");

        try
        {
            return jsonDeserializationContext.deserialize(element, Class.forName(typeS));
        } catch (ClassNotFoundException e)
        {
            throw new JsonParseException(" UNKNOWN ELEMENT TYPE: " + typeS, e);
        }
    }
}
