package com.example.ComeHome;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ComeHome.Interface.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_login extends AppCompatActivity {

    private String url = "http://10.0.2.2:8081";
    //Users 테이블에 저장된 id, password 값 저장할 변수
    String user_id;
    String user_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //계정 생성 버튼 - 화면 이동
        Button joinMember = (Button) findViewById(R.id.join_qid);
        joinMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),activity_join.class);
                startActivity(intent);
            }
        });

        //일단 임시로 만든 홈 넘어가는 onclick (로그인 : 회원 인증 버튼 누를 시 수행)
        Button loginMember = (Button) findViewById(R.id.login);

        loginMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용자 입력 텍스트 값
                EditText login_id = findViewById(R.id.login_id);
                String input_id = login_id.getText().toString();
                EditText login_pw = findViewById(R.id.login_pw);
                String input_pw = login_pw.getText().toString();
                String len_id = input_id.trim();
                String len_pw = input_pw.trim();

                //사용자 정보 가져오는 구문
                Gson gson = new GsonBuilder().setLenient().create();

                Retrofit retrofit1 = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                UserData userData = retrofit1.create(UserData.class);
                Call<Map<String, String>> call = userData.getPosts(input_id);
                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (!response.isSuccessful()) {
                            String msg = "Error" + response.code();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            return;
                        }

                        Map<String, String> posts = new HashMap<>();

                        posts = response.body();
                        String content = "";

                        content += posts.get("id");
                        user_id = content;

                        content = "";
                        content += posts.get("passwd");
                        user_pw = content;

                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {

                    }
                });
                //end of 사용자 정보 Get 구문

                if(len_id.getBytes().length <= 0 || len_pw.getBytes().length <= 0){
                    String msg = "모두 입력해주세요.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }else{
                    if(input_id.equals(user_id) && (input_pw.equals(user_pw))){
                        //화면 이동 구문
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }else{
                        String msg = "비밀번호 또는 아이디가 일치하지 않습니다.";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

}