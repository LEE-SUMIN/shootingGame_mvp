package com.example.shootinggame_mvp.Model;

public class Enemy extends Item {
    //----------------------------------------------------------------------------
    // Constant definitions.
    //

    public static final int height = 10; // 화면 너비 100을 기준으로 했을 때, enemy의 크기
    public static final int width = 10;

    //----------------------------------------------------------------------------
    // Constructor.
    //

    public Enemy(int id) {
        super(id);

        this.x = (float) (Math.random() * (Game.virtualWidth - width));
        this.y = 0;

        this.dx = 0;
        this.dy = (float) (Math.random() * 0.05 + 0.1);
    }

    //----------------------------------------------------------------------------
    // Public interface.
    //

    /**
     * Timertask 내에서 주기적으로 호출되며 enemy의 위치가 단위 벡터(dx, dy) 만큼 이동함
     */
    @Override
    public void move() {
        x += dx;
        y += dy;
    }

}
