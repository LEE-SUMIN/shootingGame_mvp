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
import com.example.shootinggame_mvp.Model.StepInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

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

        setDisplayVariables();
        float displayRatio = realDisplayHeight / realDisplayWidth;
        presenter.setVirtualCoordinates(displayRatio);

        setUpUI();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                presenter.setAngle(progress);
                rotateCannonView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addBullet();
            }
        });


    }

    //----------------------------------------------------------------------------
    // Implements Presenter.
    //


    @Override
    public void start() {
        start.setVisibility(View.GONE);
        // game 시작 상태로 변경, 게임 변수 초기화
        presenter.setStart(lifeLimit, bulletLimit);
        // 게임 진행
        presenter.startStepTimerTask();
    }


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
                    // 나머지는 안 보이도록
                    else {
                        lifeViews[i].setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    @Override
    public void setBulletViews(HashMap<Integer, Bullet> alivedBulletHashMap, HashMap<Integer, Bullet> removedBulletHashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Bullet b : alivedBulletHashMap.values()) {
                    int id = b.getId();
                    if(!bulletViews.containsKey(id)) {
                        bulletViews.put(id, addBulletImageView());
                    }
                    ImageView bulletImage = bulletViews.get(id);
                    bulletImage.setX(virtualPositionToRealPosition_X(b.getX()));
                    bulletImage.setY(virtualPositionToRealPosition_Y(b.getY()));
                }

                for(Bullet b : removedBulletHashMap.values()) {
                    int id = b.getId();
                    bulletViews.get(id).setVisibility(View.GONE);
                    bulletViews.remove(id);
                }
            }
        });
    }

    @Override
    public void setEnemyViews(HashMap<Integer, Enemy> alivedEnemyHashMap, HashMap<Integer, Enemy> removedEnemyHashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Enemy e : alivedEnemyHashMap.values()) {
                    int id = e.getId();
                    if(!enemyViews.containsKey(id)) {
                        enemyViews.put(id, addEnemyImageView());
                    }
                    ImageView enemyImage = enemyViews.get(id);
                    enemyImage.setX(virtualPositionToRealPosition_X(e.getX()));
                    enemyImage.setY(virtualPositionToRealPosition_Y(e.getY()));
                }

                for(Enemy e : removedEnemyHashMap.values()) {
                    int id = e.getId();
                    enemyViews.get(id).setVisibility(View.GONE);
                    enemyViews.remove(id);
                }
            }
        });
    }


    @Override
    public void readyForRestart() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearViews();
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


    public void setUpUI() {
        infoLayout = (LinearLayout) findViewById(R.id.info);
        start = (Button) findViewById(R.id.start);
        spaceship = (ImageView) findViewById(R.id.spaceship);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnShoot = (Button) findViewById(R.id.btnShoot);
        skyLayout = (FrameLayout) findViewById(R.id.sky_layout);

        setUpDynamicUI();
    }


    public void setUpDynamicUI() {
        // 동적 ImageView 관리
        setUpLifeViewList();
        enemyViews = new HashMap<>();
        bulletViews = new HashMap<>();
    }


    private void setUpLifeViewList() {
        lifeViews = new ImageView[lifeLimit];
        for(int i = 0; i < lifeLimit; i++) {
            lifeViews[i] = addLifeImageView();
        }
    }


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

    private void clearViews() {
        for(ImageView view : bulletViews.values()) {
            view.setVisibility(View.GONE);
        }
        bulletViews.clear();
        for(ImageView view : enemyViews.values()) {
            view.setVisibility(View.GONE);
        }
        enemyViews.clear();

    }
}