package org.JavaGame.Engine;

public interface Runnable
{
    void fixedUpdate(float dt);
    void update(float dt);

    void Init();
}
