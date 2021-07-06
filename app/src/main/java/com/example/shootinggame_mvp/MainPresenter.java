package com.example.shootinggame_mvp;

import com.example.shootinggame_mvp.Model.Game;
import com.example.shootinggame_mvp.Model.StepInfo;

import java.util.Timer;
import java.util.TimerTask;

public class MainPresenter implements Contract.Presenter {

    //----------------------------------------------------------------------------
    // Instance variables.
    //

    private Game game;
    private Timer timer;

    
    //----------------------------------------------------------------------------
    // MVP.
    //

    private Contract.View view;


    //----------------------------------------------------------------------------
    // Constructor.
    //

    public MainPresenter(Contract.View view) {
        this.view = view;
        this.game = Game.getInstance();
    }


    //----------------------------------------------------------------------------
    // Implements Contract.Presenter
    //

    /**
     * 가로 길이 100을 기준으로 화면 비율에 맞게 가상 좌표계 설정
     * @param displayRatio : 실제 화면 비율
     */
    @Override
    public void setVirtualCoordinates(float displayRatio) {
        game.setVirtualCoordinates(displayRatio);
    }


    /**
     * 게임 시작 준비 (생명 개수, 화면 상에 존재할 수 있는 최대 bullet 개수 변수 설정)
     * @param lifeLimit : 생명 개수
     * @param bulletLimit : 화면 상에 존재할 수 있는 최대 bullet 개수
     */
    @Override
    public void setStart(int lifeLimit, int bulletLimit) {
        game.setGameStart(lifeLimit, bulletLimit);
    }


    /**
     * 게임 전환 step 시작
     * @return 
     */
    @Override
    public TimerTask startStepTimerTask() {
        TimerTask stepTimerTask = new TimerTask() {
            @Override
            public void run() {
                // (1) game step 진행
                StepInfo stepInfo = game.step();
                
                // (2) 새로 업데이트 된 life, bullet, enemy에 대한 ImageView 업데이트
                view.setLifeViews(stepInfo.getLife());
                view.setBulletViews(stepInfo.getAliveBulletHashMap(), stepInfo.getRemovedBulletHashMap());
                view.setEnemyViews(stepInfo.getAliveEnemyHashMap(), stepInfo.getRemovedEnemyHashMap());
                
                // (3) 게임 종료 조건 확인
                if(game.gameOver()) {
                    timer.cancel();
                    view.readyForRestart();
                }
            }
        };

        timer = new Timer();
        timer.schedule(stepTimerTask, 0, 10);

        return stepTimerTask;
    }


    /**
     * SeekBar 조정 시 호출되며, Cannon 각도 설정
     * @param angle
     */    
    @Override
    public void setAngle(int angle) {
        game.getCannon().setAngle(180 - angle);
    }


    /**
     * shoot 버튼 클릭 시 호출되며, bullet 추가
     */
    @Override
    public void addBullet() {
        game.addBullet();
    }
}
