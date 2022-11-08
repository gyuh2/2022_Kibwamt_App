package com.example.ComeHome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ComeHome.DAO.Fragment_home;
import com.example.ComeHome.DTO.Users;
import com.example.ComeHome.Interface.DeleteUserApi;
import com.example.ComeHome.Interface.EditUserData;
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

public class activity_delete extends AppCompatActivity {

    private final String url = "http://10.0.2.2:8081";
    private TextView del_id;
    private TextView del_name;
    private  EditText del_passwd;
    private Button del_yes;
    private AlertDialog dialog;
    Users users = new Users();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        del_id = findViewById(R.id.del_id);
        del_name = findViewById(R.id.del_name);
        del_passwd = findViewById(R.id.del_passwd);

        //사용자 정보 가져오는 구문
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserData userData = retrofit1.create(UserData.class);
        //추후 세션 관리 적용 : 사용자 아이디 정보 가져오기
        Call<Map<String, String>> call = userData.getPosts("comehome");
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
                del_id.append(content);

                content = "";
                content += posts.get("name");
                del_name.setText(content);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                del_id.setText(t.getMessage());
            }
        });
        //end of 사용자 정보 Get 구문
        
        //탈퇴 버튼 클릭 시 수행
        del_yes = findViewById(R.id.delete_yes);
        del_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_del_id = del_id.getText().toString();
                String input_del_passwd = del_passwd.getText().toString();

                if(input_del_passwd.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_delete.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                } else {
                    userDelete(input_del_id, input_del_passwd);
                }
            }
        });
    }

    private void userDelete(String input_del_id, String input_del_passwd) {

        try{
            users.setId(input_del_id);
            users.setPasswd(input_del_passwd);

            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DeleteUserApi deleteUserApi = retrofit2.create(DeleteUserApi.class);
            Call<Boolean> call = deleteUserApi.getDeleteResult(users);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String result = String.valueOf(response.body());
                    Boolean body = response.body();
                    if(body == true){
                        Toast.makeText(getApplicationContext(), "회원 탈퇴되었습니다.\n이용해주셔서 감사합니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(),activity_login.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}