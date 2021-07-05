package com.example.shootinggame_mvp;

import com.example.shootinggame_mvp.Model.Game;
import com.example.shootinggame_mvp.Model.StepInfo;

import java.util.Timer;
import java.util.TimerTask;

public class MainPresenter implements Contract.Presenter {

    private Contract.View view;
    private Game game;
    private Timer timer;

    public MainPresenter(Contract.View view) {
        this.view = view;
        this.game = Game.getInstance();
    }

    @Override
    public void setVirtualCoordinates(float displayRatio) {
        game.setVirtualCoordinates(displayRatio);
    }

    @Override
    public void setStart(int lifeLimit, int bulletLimit) {
        game.setGameStart(lifeLimit, bulletLimit);
    }

    @Override
    public TimerTask startStepTimerTask() {
        TimerTask stepTimerTask = new TimerTask() {
            @Override
            public void run() {
                StepInfo stepInfo = game.step();

                view.setLifeViews(stepInfo.getLife());
                view.setBulletViews(stepInfo.getAliveBulletHashMap(), stepInfo.getRemovedBulletHashMap());
                view.setEnemyViews(stepInfo.getAliveEnemyHashMap(), stepInfo.getRemovedEnemyHashMap());

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

    @Override
    public void setAngle(int angle) {
        game.getCannon().setAngle(180 - angle);
    }

    @Override
    public void addBullet() {
        game.addBullet();
    }
}
