package org.JavaGame.Engine;

import imgui.ImGui;
import org.JavaGame.Engine.Components.Component;
import org.JavaGame.Engine.Components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    private static int ID_COUNTER = 0;
    private int UID = -1;
    private String Name;
    public Transform Transform;
    private List<Component> Components;
    private boolean DoSerialization = true;

    public GameObject(String name)
    {
        this.Name = name;
        this.Components = new ArrayList<>();
        this.Transform = new Transform();

        this.UID = ID_COUNTER++;
    }

    public GameObject(String name, Transform transform)
    {
        this.Name = name;
        this.Components = new ArrayList<>();
        this.Transform = transform;

        this.UID = ID_COUNTER++;
    }

    public void update(float dt)
    {
        for(int i = 0; i < Components.size(); i++)
        {
            Components.get(i).update(dt);
        }
    }

    public void Init()
    {
        for(int i = 0; i < Components.size(); i++)
        {
            Components.get(i).Init();
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for(Component c : Components)
        {

            if(componentClass.isAssignableFrom(c.getClass()))
            {
                try
                {
                    return componentClass.cast(c);
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                    assert false : "ERROR: CASTING COMPONENT.";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removerComponent(Class<T> componentClass)
    {
        for(int i = 0; i < Components.size(); i++)
        {
            Component c = Components.get(i);
            if(componentClass.isAssignableFrom(c.getClass()))
            {
                Components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component)
    {
        component.generateId();
        Components.add(component);
        component.setGameObject(this);
    }

    public float getZindex()
    {
        return this.Transform.getPosition().z;
    }

    public void imgui()
    {

        ImGui.text("Game Object name: " + this.Name);
        ImGui.newLine();
        for (Component c : Components)
        {
            ImGui.text("Component Name:"  + c.getName());
            c.imgui();
        }


    }

    public static void staticIinit(int maxId)
    {
        ID_COUNTER = maxId;
    }
    public int getUID()
    {
        return this.UID;
    }

    public List<Component> getAllComponents()
    {
        return this.Components;
    }

    public String getName() {
        return Name;
    }
    public void setNoSerialize()
    {
        this.DoSerialization = false;
    }
    public boolean doSerialization()
    {
        return this.DoSerialization;
    }
}
