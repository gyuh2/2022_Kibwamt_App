package com.example.ComeHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.ComeHome.DAO.Fragment_control;
import com.example.ComeHome.DAO.Fragment_home;
import com.example.ComeHome.DAO.Fragment_settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment_control fragment1;
    Fragment_home fragment2;
    Fragment_settings fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //하단 메뉴바 선언
        bottomNavigationView = findViewById(R.id.btm_navi);
        //프래그먼트 생성
        fragment1 = new Fragment_control();
        fragment2 = new Fragment_home();
        fragment3 = new Fragment_settings();

        //뷰 세팅
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_frame, fragment2).commitAllowingStateLoss();
        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너 추가
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                    case R.id.tab1: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_frame, fragment2).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab2: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_frame, fragment1).commitAllowingStateLoss();
                        return true;
                    }

                    case R.id.tab3: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_frame, fragment3).commitAllowingStateLoss();
                        return true;
                    }

                    default:
                        return false;

                }
            }
        });

    }//end of onCreate() Method

}