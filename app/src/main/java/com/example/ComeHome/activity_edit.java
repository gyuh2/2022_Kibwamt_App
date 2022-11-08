package com.example.ComeHome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ComeHome.DAO.Fragment_home;
import com.example.ComeHome.DAO.Fragment_settings;
import com.example.ComeHome.DTO.Users;
import com.example.ComeHome.Interface.EditUserData;
import com.example.ComeHome.Interface.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_edit extends AppCompatActivity {

    private final String url = "http://10.0.2.2:8081";
    private TextView edit_id;
    private EditText edit_name;
    private EditText edit_current_passwd;
    private EditText edit_passwd;
    private EditText edit_passwd2;
    private EditText edit_address;
    private EditText edit_addressDetail;
    private AlertDialog dialog;
    private Button edit_yes;
    private String old_name, old_passwd, old_address, old_addressDetail; //변경 전 값 저장할 변수
    Fragment_home fragment_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        fragment_home = new Fragment_home();

        edit_id = findViewById(R.id.edit_id);
        edit_name = findViewById(R.id.edit_name);
        edit_current_passwd = findViewById(R.id.edit_old_passwd);
        edit_passwd = findViewById(R.id.edit_passwd);
        edit_passwd2 = findViewById(R.id.edit_passwd2);
        edit_address = findViewById(R.id.edit_address);
        edit_addressDetail = findViewById(R.id.edit_addressDetail);

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
                edit_id.append(content);

                content = "";
                content += posts.get("name");
                edit_name.setText(content);
                old_name = content;

                content = "";
                content += posts.get("passwd");
                old_passwd = content;

                content = "";
                content += posts.get("address");
                edit_address.setText(content);
                old_address = content;

                content = "";
                content += posts.get("addressDetail");
                edit_addressDetail.setText(content);
                old_addressDetail = content;
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                edit_id.setText(t.getMessage());
            }
        });
        //end of 사용자 정보 Get 구문

        //수정 버튼 클릭 시 수행
        edit_yes = findViewById(R.id.edit_yes);
        edit_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력한 값 읽어와서 담을 변수
                String id = edit_id.getText().toString();
                String input_name = edit_name.getText().toString();
                String input_current_passwd = edit_current_passwd.getText().toString();
                String input_passwd = edit_passwd.getText().toString();
                String input_passwd2 = edit_passwd2.getText().toString();
                String input_address = edit_address.getText().toString();
                String input_addressDetail = edit_addressDetail.getText().toString();

                //하나라도 빈 칸일 경우
                if(input_name.equals("") || input_passwd.equals("") || input_passwd2.equals("") ||
                        input_current_passwd.equals("") || input_address.equals("") || input_addressDetail.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_edit.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                } else {
                    //현재 비밀번호가 일치하는 경우, 수정 반영
                    if(input_current_passwd.equals(old_passwd))
                    {
                        userEdit(id, input_name, input_passwd, input_passwd2, input_address, input_addressDetail);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity_edit.this);
                        dialog = builder.setMessage("현재 비밀번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
                        dialog.show();
                        return;
                    }
                }
            }
        });
    }

    private void userEdit(String id, String input_name, String input_passwd, String input_passwd2, String input_address, String input_addressDetail) {
        Users users = new Users();

        if(input_passwd.equals(input_passwd2)){ //변경 비밀번호 일치 확인
            try{
                users.setId(id);
                users.setPasswd(input_passwd);
                users.setName(input_name);
                users.setAddress(input_address);
                users.setAddressDetail(input_addressDetail);

                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                EditUserData editUserData = retrofit2.create(EditUserData.class);
                Call<Boolean> call = editUserData.getEditResult(users);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Boolean body = response.body();
                        if (response.body() == true) {
                            Toast.makeText(getApplicationContext(), "회원정보 수정 성공!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(),activity_login.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "회원정보 수정 실패하였습니다.", Toast.LENGTH_LONG).show();
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
        }else{
            //비밀번호 미일치
            AlertDialog.Builder builder = new AlertDialog.Builder(activity_edit.this);
            dialog = builder.setMessage("변경 비밀번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
            dialog.show();
            return;
        }
    }

}