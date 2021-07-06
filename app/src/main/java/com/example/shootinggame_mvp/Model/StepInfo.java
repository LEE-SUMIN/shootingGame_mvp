package com.example.shootinggame_mvp.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class StepInfo {

    //----------------------------------------------------------------------------
    // Instance variables.
    //

    private int life; // 남아있는 생명 개수

    private HashMap<Integer, Bullet> aliveBulletHashMap; // 현재 step에서 화면 상에 존재하는 bullet
    private HashMap<Integer, Bullet> removedBulletHashMap; // 현재 step에서 제거된 bullet

    private HashMap<Integer, Enemy> aliveEnemyHashMap; // 현재 step에서 화면 상에 존재하는 enemy
    private HashMap<Integer, Enemy> removedEnemyHashMap; // 현재 step에서 제거된 enemy


    //----------------------------------------------------------------------------
    // Constructor.
    //

    StepInfo(int lifeLimit) {
        life = lifeLimit;
        aliveBulletHashMap = new HashMap<>();
        removedBulletHashMap = new HashMap<>();
        aliveEnemyHashMap = new HashMap<>();
        removedEnemyHashMap = new HashMap<>();
    }


    //-----------------------------------------------------------------------------
    // Public interface.
    //

    /**
     * 새로운 step이 시작될 때 마다 호출하여 사라진 bullet, enemy를 저장하는 hashMap 초기화
     */
    public void clear() {
        removedBulletHashMap = new HashMap<>();
        removedEnemyHashMap = new HashMap<>();
    }


    /**
     * bullet이 추가될 때 마다 호출 -> 현재 화면 상에 존재하는 bullet
     * @param bullet
     */
    public void addAliveBullet(Bullet bullet) {
        aliveBulletHashMap.put(bullet.getId(), bullet);
    }


    /**
     * 현재 화면 상에 존재하는 enemy 추가
     * @param enemy
     */
    public void addAliveEnemy(Enemy enemy) {
        aliveEnemyHashMap.put(enemy.getId(), enemy);
    }


    /**
     * 현재 step에서 제거된 bullet 추가 & 화면 상에 존재하는 bullet에서 제거
     * @param bullet
     */
    public void addRemovedBullet(Bullet bullet) {
        removedBulletHashMap.put(bullet.getId(), bullet);
        aliveBulletHashMap.remove(bullet.getId());
    }


    /**
     * 현재 step에서 제거된 enemy 추가 & 화면 상에 존재하는 enemy에서 제거
     * @param enemy
     */
    public void addRemovedEnemy(Enemy enemy) {
        removedEnemyHashMap.put(enemy.getId(), enemy);
        aliveEnemyHashMap.remove(enemy.getId());
    }


    /**
     * 생명 1개 감소
     */
    public void decreaseLife() {
        life--;
    }


    /**
     * 현재 남은 생명 확인
     * @return
     */
    public int getLife() {
        return life;
    }


    /**
     * 현재 화면 상에 존재하는 bullet 리턴
     * @return
     */
    public HashMap<Integer, Bullet> getAliveBulletHashMap() {
        return aliveBulletHashMap;
    }


    /**
     * 현재 화면 상에 존재하는 enemy 리턴
     * @return
     */
    public HashMap<Integer, Enemy> getAliveEnemyHashMap() {
        return aliveEnemyHashMap;
    }


    /**
     * 현재 step에서 제거된 bullet 리턴
     * @return
     */
    public HashMap<Integer, Bullet> getRemovedBulletHashMap() {
        return removedBulletHashMap;
    }


    /**
     * 현재 step에서 제거된 enemy 리턴
     * @return
     */
    public HashMap<Integer, Enemy> getRemovedEnemyHashMap() {
        return removedEnemyHashMap;
    }
}
