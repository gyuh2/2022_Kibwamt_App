package com.example.ComeHome.DAO;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ComeHome.Interface.JsonPlaceHolderApi;
import com.example.ComeHome.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_home extends Fragment {
    ViewGroup viewGroup;
    private String url = "http://10.0.2.2:8081";

    //현재 사용자 위치 정보 변수
    private TextView local;         //실외 하단 위치
    private TextView local2;        //실내 하단 위치

    //실외 데이터 담을 변수 선언
    private TextView outPm10;       //미세먼지
    private TextView outPm25;       //초미세먼지
    private TextView outWeather;    //현재 날씨
    private TextView outRain;       //강수확률
    private TextView outHumid;      //습도
    private TextView outTemp;       //온도

    //실내 데이터 담을 변수 선언
    private TextView inPm10;        //미세먼지
    private TextView inPm25;        //초미세먼지
    private TextView inHumid;       //습도
    private TextView inTemp;        //온도

    //이미지뷰
    private ImageView cast;
    private ImageView dust;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_fragment,container,false);

        outPm10 = viewGroup.findViewById(R.id.outPM10);
        outPm25 = viewGroup.findViewById(R.id.outPM25);
        outWeather = viewGroup.findViewById(R.id.outNow);
        outRain = viewGroup.findViewById(R.id.outRain);
        outHumid = viewGroup.findViewById(R.id.outHumid);
        outTemp = viewGroup.findViewById(R.id.outTemp);

        inPm10 = viewGroup.findViewById(R.id.inPM10);
        inPm25 = viewGroup.findViewById(R.id.inPM25);
        inHumid = viewGroup.findViewById(R.id.inHumid);
        inTemp = viewGroup.findViewById(R.id.inTemp);

        local = viewGroup.findViewById(R.id.local);
        local2 = viewGroup.findViewById(R.id.local2);

        cast = viewGroup.findViewById(R.id.weatherCast);
        dust = viewGroup.findViewById(R.id.dustGrade);

        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {

                Gson gson = new GsonBuilder().setLenient().create();

                Retrofit retrofit1 = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit1.create(JsonPlaceHolderApi.class);
                Call<Map<String, String>> call = jsonPlaceHolderApi.getPosts("comehome");
                // id 임의로!
                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        //System.out.println(response.body().toString());
                        if (!response.isSuccessful()) {
                            String msg = "Error" + response.code();
                            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                            //textViewResult.setText("Code: " + response.code());
                            return;
                        }

                        Map<String, String> posts = new HashMap<>();

                        posts = response.body();
                        String content = "";
                        String content2 = "";

                        content += posts.get("temp") + "°C";
                        inTemp.setText(content);

                        content = "";
                        content += posts.get("humid") + "%";
                        inHumid.setText(content);

                        content = "";
                        content += posts.get("API_temp") + "°C";
                        outTemp.setText(content);

                        content = "";
                        content += posts.get("API_humid") + "%";
                        outHumid.setText(content);


                        content ="";
                        content+=posts.get("pop")+"%"; //강수확률
                        outRain.setText(content);

                        content="";

                        if(Integer.parseInt(posts.get("sky")) >= 0 && Integer.parseInt(posts.get("sky")) <= 5){ //하늘 상태 전운량
                            content+="맑음";
                            cast.setImageResource(R.drawable.sun);

                        }else if(Integer.parseInt(posts.get("sky")) >= 6 && Integer.parseInt(posts.get("sky")) <= 8){
                            content+="구름 많음";
                            cast.setImageResource(R.drawable.cloud);
                        }else if(Integer.parseInt(posts.get("sky")) >= 9 && Integer.parseInt(posts.get("sky")) <= 10){
                            content+="흐림";
                            cast.setImageResource(R.drawable.blur);
                        } else{
                            content+="날씨 등급 산정 중";
                        }

                        outWeather.setText(content);

                        content = "";
                        if (Integer.parseInt(posts.get("pmGrade")) == 1) {//실내 미세먼지 등급
                            content += "좋음";
                            content2+= posts.get("pm") + "㎍/㎥";
                            dust.setImageResource(R.drawable.good);
                        } else if (Integer.parseInt(posts.get("pmGrade")) == 2) {
                            content += "보통";
                            content2+= posts.get("pm") + "㎍/㎥";
                            dust.setImageResource(R.drawable.soso);
                        } else if (Integer.parseInt(posts.get("pmGrade")) == 3) {
                            content += "나쁨";
                            content2+= posts.get("pm") + "㎍/㎥";
                            dust.setImageResource(R.drawable.bad);
                        } else if (Integer.parseInt(posts.get("pmGrade")) == 4) {
                            content += "매우 나쁨";
                            content2+= posts.get("pm") + "㎍/㎥";
                            dust.setImageResource(R.drawable.very_bad);
                        } else {
                            content += "등급 산정 중";
                            content2+= posts.get("pm") + "㎍/㎥";
                            dust.setImageResource(R.drawable.bad);
                        }
                        inPm10.setText(content);
                        inPm25.setText(content2);

                        content = "";
                        content2 = "";
                        if (Integer.parseInt(posts.get("API_PMGrade")) == 1) {//실외 미세먼지 등급
                            content += "좋음";
                            content2+= posts.get("API_PM") + "㎍/㎥";


                        } else if (Integer.parseInt(posts.get("API_PMGrade")) == 2) {
                            content += "보통";
                            content2+= posts.get("API_PM") + "㎍/㎥";

                        } else if (Integer.parseInt(posts.get("API_PMGrade")) == 3) {
                            content += "나쁨";
                            content2+= posts.get("API_PM") + "㎍/㎥";
                            showAlertDialog();
                        } else if (Integer.parseInt(posts.get("API_PMGrade")) == 4) {
                            content += "매우 나쁨";
                            content2+= posts.get("pm") + "㎍/㎥";
                            showAlertDialog();
                        } else {
                            content += "등급 산정 중";
                            content2+= posts.get("API_PM") + "㎍/㎥";
                        }
                        outPm10.setText(content);
                        outPm25.setText(content2);

                        //사용자 구 얻어오는 구문
                        content = posts.get("address");
                        Log.d("result", "값 :"+content);
                        local.setText("서울특별시 "+content);
                        local2.setText("서울특별시 "+content);
                    }
                    //DB 읽어오는 구문 (실내외 온습도 및 미세먼지 정보)

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        outPm10.setText(t.getMessage());
                    }
                });
            }
        };
        timer.schedule(tt, 0, 3000000);
        //5분마다 갱신하도록 타이머 설정

        return viewGroup;
    }

    //팝업 알림창
    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("미세먼지 알림");
        builder.setMessage("미세먼지 등급이 나쁨 이상입니다.");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getActivity(), "창을 닫았습니다.", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

}