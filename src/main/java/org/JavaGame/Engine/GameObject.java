package org.JavaGame.Engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements Runnable
{
    public GameObject(String name)
    {
        this.m_Name = name;
        this.m_Components = new ArrayList<>();
    }

    @Override
    public void update(float dt)
    {
        System.out.println("UPDATE: PRINTING FROM " + m_Name);

        for(int i = 0; i < m_Components.size(); i++)
        {
            m_Components.get(i).update(dt);
        }
    }

    @Override
    public void Init()
    {
        System.out.println("INIT: PRINTING FROM " + m_Name);
        for(int i = 0; i < m_Components.size(); i++)
        {
            m_Components.get(i).Init();
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for(Component c : m_Components)
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
        for(int i = 0; i < m_Components.size(); i++)
        {
            Component c = m_Components.get(i);
            if(componentClass.isAssignableFrom(c.getClass()))
            {
                m_Components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component)
    {
        m_Components.add(component);
        component.gameObject = this;
    }

    private String m_Name;
    private List<Component> m_Components;
}
