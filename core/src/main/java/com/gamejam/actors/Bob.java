package com.gamejam.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris on 12/13/13.
 * Bob is our main actor
 */
public class Bob {

    enum State {
        MOVING,
        PUNCHING_TICKET,
        IDLE;


    }

    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();
    Rectangle bounds = new Rectangle();
    int currentRow = 3;
    boolean facingLeft = true;
    float stateTime = 0;

    public Bob(Vector2 position) {
        this.bounds.x = position.x;
        this.bounds.y = position.y;

    }

    public Bob() {

    }

    public void update(float delta) {
        stateTime += delta;
//        position.add(velocity.cpy().scl(delta));
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentRow() {
        return this.currentRow;
    }
}
