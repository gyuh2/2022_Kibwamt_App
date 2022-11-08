package com.example.ComeHome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ComeHome.DTO.ControlDataInfo;
import com.example.ComeHome.DTO.Users;
import com.example.ComeHome.Interface.EditUserData;
import com.example.ComeHome.Interface.SmarthomeControlApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_smarthome_change extends AppCompatActivity {

    private final String url = "http://10.0.2.2:8081";
    //ControlDataInfo controlDataInfo = new ControlDataInfo();

    private String windowUp = "windowUp";
    private String heater = "heater";
    private String ac = "ac";
    private String airCleaner = "airCleaner";
    private String airOut = "airOut";
    private String door = "door";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarthome_change);

        Button useWindow = findViewById(R.id.useWindow);
        Button unusedWindow = findViewById(R.id.unusedWindow);
        //창문
        Button userHeater = findViewById(R.id.userHeater);
        Button unusedHeater = findViewById(R.id.unusedHeater);
        //난방기
        Button useAc = findViewById(R.id.useAc);
        Button unusedAc = findViewById(R.id.unusedAc);
        //냉방기
        Button userAirCleaner = findViewById(R.id.userAirCleaner);
        Button unusedAirCleaner = findViewById(R.id.unusedAirCleaner);
        //공기청정기
        Button useAirOut = findViewById(R.id.useAirOut);
        Button unusedAirOut = findViewById(R.id.unusedAirOut);
        //환기청정기
        Button userDoor = findViewById(R.id.userDoor);
        Button unusedDoor = findViewById(R.id.unusedDoor);
        //도어락

        useWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDevice(windowUp);
            }
        });
        unusedWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDevice(windowUp);
            }
        });

        userHeater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDevice(heater);
            }
        });
        unusedHeater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDevice(heater);
            }
        });

        useAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDevice(ac);
            }
        });
        unusedAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDevice(ac);
            }
        });

        userAirCleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDevice(airCleaner);
            }
        });
        unusedAirCleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDevice(airCleaner);
            }
        });

        useAirOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDevice(airOut);
            }
        });
        unusedAirOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDevice(airOut);
            }
        });

        userDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDevice(door);
            }
        });
        unusedDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDevice(door);
            }
        });

    }

    //기기 사용 등록 메소드
    private void AddDevice(String device) {
        try{
            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Map<String, String> controlDevices = new HashMap<>();
            controlDevices.put("method", "add");
            controlDevices.put("device", device);

            System.out.println("map 저장된 값 출력 : "+controlDevices);

            SmarthomeControlApi smarthomeControlApi = retrofit2.create(SmarthomeControlApi.class);

            Call<Boolean> call = smarthomeControlApi.getControlResult(controlDevices);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.body() == true) {
                        Toast.makeText(getApplicationContext(), "기기 사용 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "기기 사용 등록에 실패하였습니다.", Toast.LENGTH_LONG).show();
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
    //기기 사용 등록 해제 메소드
    private void DeleteDevice(String device) {
        try {
            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Map<String, String> controlDevices = new HashMap<>();
            controlDevices.put("method", "delete");
            controlDevices.put("device", device);

            System.out.println("map 저장된 값 출력 : "+controlDevices);

            SmarthomeControlApi smarthomeControlApi = retrofit2.create(SmarthomeControlApi.class);

            Call<Boolean> call = smarthomeControlApi.getControlResult(controlDevices);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.body() == true) {
                        Toast.makeText(getApplicationContext(), "기기 사용 해제가 완료되었습니다.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "기기 사용 해제에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}