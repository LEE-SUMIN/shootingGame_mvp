package com.example.shootinggame_mvp.Model;

public abstract class Item {
    //----------------------------------------------------------------------------
    // Instance variables.
    //

    protected final int id; // Game에서 관리하기 위해 할당되는 id

    protected float dx; // 이동하는 단위 벡터
    protected float dy;

    protected float x; // 위치 좌표
    protected float y;

    //----------------------------------------------------------------------------
    // Constructor.
    //

    public Item(int id) {
        this.id = id;
    }

    //-----------------------------------------------------------------------------
    // Public interface.
    //

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getId() { return id; }

    /**
     * 게임 진행 step 마다 호출 -> 각 bullet과 enemy가 단위 벡터 만큼 이동한다.
     */
    public abstract void move();
}
