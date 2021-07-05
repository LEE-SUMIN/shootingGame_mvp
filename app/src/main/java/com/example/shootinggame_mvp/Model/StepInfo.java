package com.example.shootinggame_mvp.Model;

import java.util.HashMap;

public class StepInfo {
    private int life;
    private HashMap<Integer, Bullet> bulletHashMap;
    private HashMap<Integer, Enemy> enemyHashMap;

    StepInfo(int life, HashMap<Integer, Bullet> bulletHashMap, HashMap<Integer, Enemy> enemyHashMap) {
        this.life = life;
        this.bulletHashMap = bulletHashMap;
        this.enemyHashMap = enemyHashMap;
    }

    public int getLife() {
        return life;
    }

    public HashMap getBulletHashMap() {
        return bulletHashMap;
    }

    public HashMap getEnemyHashMap() {
        return enemyHashMap;
    }
}
