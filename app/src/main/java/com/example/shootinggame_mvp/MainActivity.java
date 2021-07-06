package com.example.shootinggame_mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.shootinggame_mvp.Model.Bullet;
import com.example.shootinggame_mvp.Model.Enemy;
import com.example.shootinggame_mvp.Model.Game;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements Contract.View {
    //----------------------------------------------------------------------------
    // Constant definitions.
    //
    private final int lifeLimit = 3;
    private final int bulletLimit = 5;

    //----------------------------------------------------------------------------
    // Static UI References.
    //

    private LinearLayout infoLayout;
    private Button start;
    private SeekBar seekBar;
    private Button btnShoot;
    private ImageView spaceship;
    private FrameLayout skyLayout;

    //----------------------------------------------------------------------------
    // Dynamic UI References.
    //

    private ImageView[] lifeViews;
    private HashMap<Integer, ImageView> enemyViews;
    private HashMap<Integer, ImageView> bulletViews;

    private int realDisplayHeight;
    private int realDisplayWidth;


    //----------------------------------------------------------------------------
    // MVP.
    //

    MainPresenter presenter;


    //----------------------------------------------------------------------------
    // Life cycle.
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        
        // 실제 화면 크기 변수 세팅
        setDisplayVariables();
        
        // 가상 좌표계 세팅 : 가로 크기 100을 기준으로 화면 비율에 맞게 설정한다.
        float displayRatio = realDisplayHeight / realDisplayWidth;
        presenter.setVirtualCoordinates(displayRatio);
        
        // UI 세팅
        setUpUI();
        

        // start 버튼 클릭 -> 게임 시작
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.GONE);
                
                // (1) game 시작 상태로 변경, 게임 변수 초기화
                presenter.setStart(lifeLimit, bulletLimit);
                
                // (2) 게임 진행
                presenter.startStepTimerTask();
            }
        });


        // seekBar 조정 -> Cannon 각도 조정
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // (1) Cannon에 각도 값 설정
                presenter.setAngle(progress);
                // (2) Cannon ImageView 회전
                rotateCannonView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // shoot 버튼 클릭 -> Bullet 생성
        btnShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bullet 추가
                presenter.addBullet();
            }
        });
    }
    



    //----------------------------------------------------------------------------
    // Implements Contract.View
    //

    /**
     * 남은 생명 개수 만큼 하트 ImageView 보여주기
     * @param life : 남은 생명 개수
     */
    @Override
    public void setLifeViews(int life) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < lifeViews.length; i++) {
                    // 남은 생명 개수 만큼 보이게
                    if(i < life) {
                        lifeViews[i].setVisibility(View.VISIBLE);
                    }
                    
                    // 나머지는 안 보이게
                    else {
                        lifeViews[i].setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    /**
     * 화면 상에 존재하는 Bullet ImageView 보여주고, 사라진 Bullet ImageView 제거
     * @param alivedBulletHashMap : 화면 상에 존재하는 Bullet
     * @param removedBulletHashMap : 사라진 Bullet
     */
    @Override
    public void setBulletViews(HashMap<Integer, Bullet> alivedBulletHashMap, HashMap<Integer, Bullet> removedBulletHashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 해당 step에서 화면 상에 존재하는 Bullet의 ImageView 좌표 설정
                for(Bullet b : alivedBulletHashMap.values()) {
                    int id = b.getId();
                    
                    // 이번 step에 새로 생성된 bullet -> ImageView 생성
                    if(!bulletViews.containsKey(id)) {
                        bulletViews.put(id, addBulletImageView());
                    }
                    
                    ImageView bulletImage = bulletViews.get(id);
                    bulletImage.setX(virtualPositionToRealPosition_X(b.getX()));
                    bulletImage.setY(virtualPositionToRealPosition_Y(b.getY()));
                }
                
                // 해당 step에서 사라진 Bullet의 ImageView 제거
                for(Bullet b : removedBulletHashMap.values()) {
                    int id = b.getId();
                    bulletViews.get(id).setVisibility(View.GONE);
                    bulletViews.remove(id);
                }
            }
        });
    }


    /**
     * 화면 상에 존재하는 Enemy ImageView 보여주고, 사라진 Enemy ImageView 제거
     * @param alivedEnemyHashMap : 화면 상에 존재하는 Enemy
     * @param removedEnemyHashMap : 사라진 Enemy
     */
    @Override
    public void setEnemyViews(HashMap<Integer, Enemy> alivedEnemyHashMap, HashMap<Integer, Enemy> removedEnemyHashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 해당 step에서 화면 상에 존재하는 Enemy의 ImageView 좌표 설정
                for(Enemy e : alivedEnemyHashMap.values()) {
                    int id = e.getId();
                    
                    // 이번 step에 새로 생성된 enemy -> ImageView 생성
                    if(!enemyViews.containsKey(id)) {
                        enemyViews.put(id, addEnemyImageView());
                    }
                    
                    ImageView enemyImage = enemyViews.get(id);
                    enemyImage.setX(virtualPositionToRealPosition_X(e.getX()));
                    enemyImage.setY(virtualPositionToRealPosition_Y(e.getY()));
                }
                
                // 해당 step에서 사라진 Enemy의 ImageView 제거
                for(Enemy e : removedEnemyHashMap.values()) {
                    int id = e.getId();
                    enemyViews.get(id).setVisibility(View.GONE);
                    enemyViews.remove(id);
                }
            }
        });
    }


    /**
     * 게임 종료 시, 존재하던 Enemy와 Bullet ImageView를 모두 제거하고 재시작 화면으로 전환
     */
    @Override
    public void readyForRestart() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 화면 상에 존재하던 모든 bullet, enemy ImageView 제거
                clearViews();
                
                // 재시작 버튼 생성
                start.setText("restart");
                start.setVisibility(View.VISIBLE);
            }
        });
    }



    //----------------------------------------------------------------------
    // Internal support methods.
    //

    /**
     * 화면 가로 & 세로 크기 변수 세팅
     */
    private void setDisplayVariables() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        realDisplayWidth = size.x;
        realDisplayHeight = (int) (size.y * 0.75);
    }


    /**
     * UI 세팅
     */
    private void setUpUI() {
        infoLayout = (LinearLayout) findViewById(R.id.info);
        start = (Button) findViewById(R.id.start);
        spaceship = (ImageView) findViewById(R.id.spaceship);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnShoot = (Button) findViewById(R.id.btnShoot);
        skyLayout = (FrameLayout) findViewById(R.id.sky_layout);

        setUpDynamicUI();
    }


    /**
     * 동적 ImageView를 관리하기 위한 자료구조 세팅
     */
    private void setUpDynamicUI() {
        // 동적 ImageView 관리
        setUpLifeViewList();
        enemyViews = new HashMap<>();
        bulletViews = new HashMap<>();
    }


    /**
     * 초기 설정된 생명 개수 만큼 life ImageView를 관리할 배열 정의
     */
    private void setUpLifeViewList() {
        lifeViews = new ImageView[lifeLimit];
        for(int i = 0; i < lifeLimit; i++) {
            lifeViews[i] = addLifeImageView();
        }
    }


    /**
     * cannon ImageView 회전
     * @param progress : seekBar 각도
     */
    private void rotateCannonView(int progress) {
        //cannon 이미지뷰 회전
        spaceship.setRotation(progress - 90);
    }


    /**
     * 생명 ImageView 생성
     * @return : 생성된 ImageView
     */
    private ImageView addLifeImageView() {
        int virtualLifeSize = 7;
        int lifeWidth = (int) (realDisplayWidth / Game.virtualWidth * virtualLifeSize);
        int lifeHeight = (int) (realDisplayHeight / Game.virtualHeight * virtualLifeSize);

        ImageView lifeImage = new ImageView(getApplicationContext());
        lifeImage.setImageResource(R.drawable.heart);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(lifeWidth, lifeHeight);

        int padding = (int) (realDisplayWidth / Game.virtualWidth);
        lifeImage.setPadding(padding, padding, padding, padding);

        infoLayout.addView(lifeImage, params);
        return lifeImage;
    }


    /**
     * Bullet ImageView 생성
     * @return : 생성된 ImageView
     */
    private ImageView addBulletImageView() {
        int bulletWidth = (int) (realDisplayWidth / Game.virtualWidth * Bullet.width);
        int bulletHeight = (int) (realDisplayHeight / Game.virtualHeight * Bullet.height);

        ImageView bulletImage = new ImageView(getApplicationContext());
        bulletImage.setImageResource(R.drawable.bullet);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bulletWidth, bulletHeight);

        skyLayout.addView(bulletImage, params);
        return bulletImage;
    }


    /**
     * Enemy ImageView 생성
     * @return : 생성된 ImageView
     */
    private ImageView addEnemyImageView() {
        int enemyWidth = (int) (realDisplayWidth / Game.virtualWidth * Enemy.width);
        int enemyHeight = (int) (realDisplayHeight / Game.virtualHeight * Enemy.height);

        ImageView enemyImage = new ImageView(getApplicationContext());
        enemyImage.setImageResource(R.drawable.monster);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(enemyWidth, enemyHeight);

        skyLayout.addView(enemyImage, params);
        return enemyImage;
    }


    /**
     * 가상 X좌표 -> 실제 X좌표로 변환
     * @param virtualX : 가상 X 좌표
     * @return : 변환된 실제 X 좌표
     */
    private float virtualPositionToRealPosition_X(float virtualX) {
        return (realDisplayWidth / Game.virtualWidth) * virtualX;
    }


    /**
     * 가상 Y좌표 -> 실제 Y좌표로 변환
     * @param virtualY : 가상 Y 좌표
     * @return : 변환된 실제 Y 좌표
     */
    private float virtualPositionToRealPosition_Y(float virtualY) {
        return (realDisplayHeight / Game.virtualHeight) * virtualY;
    }


    /**
     * 화면 상에 존재하는 모든 bullet, enemy ImageView 제거 -> 게임 재시작 시 호출
     */
    private void clearViews() {
        // 화면 상에 존재하는 bullet ImageView 제거
        for(ImageView view : bulletViews.values()) {
            view.setVisibility(View.GONE);
        }
        bulletViews.clear();
        
        // 화면 상에 존재하는 enemy ImageView 제거
        for(ImageView view : enemyViews.values()) {
            view.setVisibility(View.GONE);
        }
        enemyViews.clear();

    }
}