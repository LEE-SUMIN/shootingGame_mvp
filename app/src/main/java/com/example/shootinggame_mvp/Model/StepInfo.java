package com.example.shootinggame_mvp.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class StepInfo {
    private int life;
    private HashMap<Integer, Bullet> aliveBulletHashMap;
    private HashMap<Integer, Bullet> removedBulletHashMap;
    private HashMap<Integer, Enemy> aliveEnemyHashMap;
    private HashMap<Integer, Enemy> removedEnemyHashMap;


    StepInfo(int lifeLimit) {
        life = lifeLimit;
        aliveBulletHashMap = new HashMap<>();
        removedBulletHashMap = new HashMap<>();
        aliveEnemyHashMap = new HashMap<>();
        removedEnemyHashMap = new HashMap<>();
    }

    public void clear() {
        removedBulletHashMap = new HashMap<>();
        removedEnemyHashMap = new HashMap<>();
    }

    public void addAliveBullet(Bullet bullet) {
        aliveBulletHashMap.put(bullet.getId(), bullet);
    }

    public void addAliveEnemy(Enemy enemy) {
        aliveEnemyHashMap.put(enemy.getId(), enemy);
    }

    public void addRemovedBullet(Bullet bullet) {
        removedBulletHashMap.put(bullet.getId(), bullet);
        aliveBulletHashMap.remove(bullet.getId());
    }

    public void addRemovedEnemy(Enemy enemy) {
        removedEnemyHashMap.put(enemy.getId(), enemy);
        aliveEnemyHashMap.remove(enemy.getId());
    }

    public void decreaseLife() {
        life--;
    }

    public int getLife() {
        return life;
    }

    public HashMap<Integer, Bullet> getAliveBulletHashMap() {
        return aliveBulletHashMap;
    }

    public HashMap<Integer, Enemy> getAliveEnemyHashMap() {
        return aliveEnemyHashMap;
    }

    public HashMap<Integer, Bullet> getRemovedBulletHashMap() {
        return removedBulletHashMap;
    }

    public HashMap<Integer, Enemy> getRemovedEnemyHashMap() {
        return removedEnemyHashMap;
    }
}
