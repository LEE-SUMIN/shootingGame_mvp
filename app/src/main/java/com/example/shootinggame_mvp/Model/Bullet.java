package com.example.shootinggame_mvp.Model;

public class Bullet extends Item {

    //----------------------------------------------------------------------------
    // Constant definitions.
    //

    public static final int height = 4; // 화면 너비 100을 기준으로 했을 때, bullet의 크기
    public static final int width = 4;


    //----------------------------------------------------------------------------
    // Instance variables.
    //
    
    private int reflection; // 벽에 튕긴 횟수

    //----------------------------------------------------------------------------
    // Constructor.
    //

    public Bullet(int id, int angle) {
        super(id);

        this.x = (Game.virtualWidth / 2f) - (width / 2f);
        this.y = Game.virtualHeight - height;

        this.dx = angle > 90 ? -1 : 1;
        this.dy = (float) (-Math.tan(Math.toRadians(angle)) * dx);

        this.reflection = 0;
    }

    //----------------------------------------------------------------------------
    // Public interface.
    //

    /**
     * TimerTask 내에서 주기적으로 호출되며 bullet의 위치가 dx, dy 만큼 이동함
     */
    @Override
    public void move() {
        if(x == Game.virtualWidth - width || x == 0) { //벽에 부딪히면 이동 방향 바뀜
            dx = -dx;
            reflection++;
        }
        x += dx;
        y += dy;
    }


    /**
     * 1회 이상 반사되어 enemy를 죽일 수 있는 bullet인지 확인
     * @return
     */
    public boolean bounced() { return (reflection >= 1); }

}
