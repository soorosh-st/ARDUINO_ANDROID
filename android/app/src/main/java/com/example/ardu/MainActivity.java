package com.example.ardu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    APIinterface request;
    String url="https://farsam-co.com/";
    EditText gp;
    EditText st;
    Button send,homebt;
    CardView hm;
    CardView ard;
    ImageView gohome,goard,refresh;
    Button start;
    Button end;
    TextView tv;
    home h=new home();
    NotificationManager notificationManager;
    boolean a=false;

    boolean startbool=true;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        request =APICLIENT.getapiclient(url).create(APIinterface.class);
        refresh=findViewById(R.id.refresh);
        gp=findViewById(R.id.GPIO);
        st=findViewById(R.id.State);
        send=findViewById(R.id.Sendbtn);
        hm=findViewById(R.id.myhome);
        ard=findViewById(R.id.maincard);
        gohome=findViewById(R.id.homecard);
        goard=findViewById(R.id.arducard);
        tv=findViewById(R.id.homestatues);
        homebt=findViewById(R.id.homebtn);
        start= findViewById(R.id.starter);
        end=findViewById(R.id.closer);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getuser();
            }
        });
        Handler handler = new Handler();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onrefresh();
            }
        });
        createNotificationChannel();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startbool){
                    Startservice(view);
                    startbool=!startbool;
                }
                else {
                    Stopservice(view);
                    startbool=!startbool;
                }

            }
        });
        Runnable runnablecode = new Runnable() {
            @Override
            public void run() {
                request.gethome().enqueue(new Callback<home>() {
                    @Override
                    public void onResponse(Call<home> call, Response<home> response) {
                        h=response.body();

                        if (h.state.equals("1")) {
                            noti("door opened ... alarrrrrrrm");
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                            for (int i=0;i<5;i++)
                                mp.start();

                        }

                    }
                    @Override
                    public void onFailure(Call<home> call, Throwable t) {
                    }
                });
                handler.postDelayed(this,5000);
            }
        };
        handler.post(runnablecode);

        homebt.setOnClickListener(new View.OnClickListener() {///////////////////////////////////////
            @SuppressLint("SetTextI18n")
            @Override

            public void onClick(View view) {

                if (a==false) {
                    tv.setTextColor(getResources().getColor(R.color.safe));
                    tv.setText("home is safe");

                    a =true;
                    Call<home> call=request.sendhome(1);
                    call.enqueue(new Callback<home>() {
                        @Override
                        public void onResponse(Call<home> call, Response<home> response) {

                            if (response.body().getResponse().equals("succset")){
                                Toast.makeText(MainActivity.this, "sent", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getResponse().equals("wrong")){
                                Toast.makeText(MainActivity.this, "did not send", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<home> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.unsafe));
                    tv.setText("home is unsafe");
                    Call<home> call=request.sendhome(0);
                    call.enqueue(new Callback<home>() {
                        @Override
                        public void onResponse(Call<home> call, Response<home> response) {
                            if (response.body().getResponse().equals("succset")){
                                Toast.makeText(MainActivity.this, "sent", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getResponse().equals("wrong")){
                                Toast.makeText(MainActivity.this, "did not send", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<home> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    a=false;
                }


            }
        });
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm.setVisibility(View.VISIBLE);
                ard.setVisibility(View.GONE);

            }
        });
        goard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm.setVisibility(View.GONE);
                ard.setVisibility(View.VISIBLE);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnablecode);
            }
        });
    }
    private void onrefresh(){
        request.gethome().enqueue(new Callback<home>() {
            @Override
            public void onResponse(Call<home> call, Response<home> response) {
                h=response.body();

                if (h.state.equals("1")) {
                    noti("door opened ... alarrrrrrrm");
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                    for (int i=0;i<5;i++)
                        mp.start();

                }
                else {
                    Toast.makeText(MainActivity.this, "every thing is good :)", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<home> call, Throwable t) {
            }
        });
    }
    private void noti(String notitext){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "85")
                .setSmallIcon(R.drawable.ic_baseline_home_24)
                .setContentTitle("HOME DOOR")
                .setContentText(notitext)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(1, builder.build());

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("85", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void Startservice(View v){
        Intent intentservice=new Intent(this,Myservice.class);
        ContextCompat.startForegroundService(this,intentservice);
    }
    private void Stopservice(View v){
        Intent intentservice=new Intent(this,Myservice.class);
        stopService((intentservice));
    }
    private void getuser() {
        String gpt=gp.getText().toString();
        String stt=st.getText().toString();
        Call<ardu> call=request.sendardu(gpt,stt);

        call.enqueue(new Callback<ardu>() {
            @Override
            public void onResponse(Call<ardu> call, Response<ardu> response) {
                if (response.body().getResponse().equals("succset")){
                    Toast.makeText(MainActivity.this, "sent", Toast.LENGTH_SHORT).show();
                }
                else if (response.body().getResponse().equals("wrong")){
                    Toast.makeText(MainActivity.this, "did not send", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ardu> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}