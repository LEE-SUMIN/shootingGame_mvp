package com.example.shootinggame_mvp;

import com.example.shootinggame_mvp.Model.Bullet;
import com.example.shootinggame_mvp.Model.Enemy;
import java.util.HashMap;
import java.util.TimerTask;

public interface Contract {
    interface View {
        /**
         * 남은 생명 개수 만큼 하트 ImageView 보여주기
         * @param life
         */
        void setLifeViews(int life);


        /**
         * 화면 상에 존재하는 Bullet ImageView 보여주고, 사라진 Bullet ImageView 제거
         * @param alivedBulletList
         * @param removedBulletList
         */
        void setBulletViews(HashMap<Integer, Bullet> alivedBulletList, HashMap<Integer, Bullet> removedBulletList);


        /**
         * 화면 상에 존재하는 Enemy ImageView 보여주고, 사라진 Enemy ImageView 제거
         * @param alivedEnemyList
         * @param removedEnemyList
         */
        void setEnemyViews(HashMap<Integer, Enemy> alivedEnemyList, HashMap<Integer, Enemy> removedEnemyList);


        /**
         * 게임 종료 시, 존재하던 Enemy, Bullet ImageView를 모두 제거하고 재시작 화면으로 전환
         */
        void readyForRestart();
    }

    interface Presenter {
        /**
         * 가로 길이 100을 기준으로 화면 비율에 맞게 가상 좌표계 설정
         * @param displayRatio
         */
        void setVirtualCoordinates(float displayRatio);


        /**
         * 게임 시작 준비(생명 개수, 화면 상에 존재할 수 있는 최대 bullet 개수 변수 설정)
         * @param lifeLimit
         * @param bulletLimit
         */
        void setStart(int lifeLimit, int bulletLimit);


        /**
         * 게임 전환 step 시작
         * @return
         */
        TimerTask startStepTimerTask();


        /**
         * SeekBar 조정 시 호출되며, Cannon 각도 설정
         * @param angle
         */
        void setAngle(int angle);


        /**
         * shoot 버튼 클릭 시 호출되며, bullet 추가
         */
        void addBullet();
    }
}
