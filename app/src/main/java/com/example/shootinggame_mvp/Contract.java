package com.example.shootinggame_mvp;

import com.example.shootinggame_mvp.Model.Bullet;
import com.example.shootinggame_mvp.Model.Enemy;
import com.example.shootinggame_mvp.Model.StepInfo;

import java.util.HashMap;
import java.util.TimerTask;

public interface Contract {
    interface View {
        void start();

        void setLifeViews(int life);
        void setBulletViews(HashMap<Integer, Bullet> bulletHashMap);
        void setEnemyViews(HashMap<Integer, Enemy> enemyHashMap);

        void readyForRestart();
    }

    interface Presenter {
        void setVirtualCoordinates(float displayRatio);
        void setStart(int lifeLimit, int bulletLimit);
        TimerTask startStepTimerTask();
        void setAngle(int angle);
        void addBullet();
    }
}
