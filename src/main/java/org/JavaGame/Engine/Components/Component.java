package org.JavaGame.Engine.Components;

import imgui.ImGui;
import org.JavaGame.Engine.GameObject;
import org.JavaGame.Engine.Runnable;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component implements Runnable
{
    protected String Name = "Unkown";
    private static int ID_COUNTER = 0;
    private int UID = -1;



    protected transient GameObject GameObject = null;

    public void Init() {}
    public void update(float dt) { }

    public void imgui()
    {
        try
        {
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields)
            {
                boolean isTransient =  Modifier.isTransient(field.getModifiers());
                if(isTransient)
                {
                    continue;
                }
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isPrivate)
                {
                    field.setAccessible(true);
                }
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class)
                {
                    int val = (int)value;
                    int[] imInt = {val};
                    if(ImGui.dragInt(name + ": ", imInt))
                    {
                        field.set(this, imInt[0]);
                    }
                }
                else if(type == float.class)
                {
                    float val = (float)value;
                    float[] imFloat = {val};
                    if(ImGui.dragFloat(name + ": ", imFloat))
                    {
                        field.set(this, imFloat[0]);
                    }
                }
                else if(type == boolean.class)
                {
                    boolean val = (boolean)value;
                    if(ImGui.checkbox(name + ": ", val))
                    {
                        field.set(this, !val);
                    }
                }
                else if(type == Vector3f.class)
                {
                    Vector3f val = (Vector3f)value;
                    float[] imVec3 = {val.x, val. y, val.z};
                    if(ImGui.dragFloat3(name + ": ", imVec3))
                    {
                        val.set(imVec3[0], imVec3[1], imVec3[2]);
                    }
                }
                else if(type == Vector4f.class)
                {
                    Vector4f val = (Vector4f)value;
                    float[] imVec4 = {val.x, val. y, val.z, val.w};
                    if(ImGui.dragFloat4(name + ": ", imVec4))
                    {
                        val.set(imVec4[0], imVec4[1], imVec4[2], imVec4[3]);
                    }
                }


                if(isPrivate)
                {
                    field.setAccessible(false);
                }
            }

        }catch (IllegalAccessException e)
        {
            e.printStackTrace();;
        }
    }
    public void generateId()
    {
        if(this.UID == -1)
        {
            this.UID = ID_COUNTER++;
        }
    }
    public int getUID()
    {
        return this.UID;
    }
    public static void staticInit(int maxId)
    {
        ID_COUNTER = maxId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public void setGameObject(GameObject gameObject)
    {
        GameObject = gameObject;
    }
    public GameObject getGameObject() {
        return GameObject;
    }
}
