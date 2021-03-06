package com.firebase.temperaturenotification;

import android.media.AsyncPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendNotificationsActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editTextTitle, editTextBody;
    private String TAG = "SendNotificationsActivityTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);

        final User user = (User) getIntent().getSerializableExtra("user");
        textView = findViewById(R.id.textViewUser);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextBody = findViewById(R.id.editTextBody);

        textView.setText("Sending to :"+user.email);
        findViewById(R.id.buttoSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendNotification(user);
            }
        });


    }

    private void SendNotification(User user){
        String title = editTextTitle.getText().toString().trim();
        String body = editTextBody.getText().toString().trim();

        if(title.isEmpty()){
            editTextTitle.setError("Title required");
            editTextTitle.requestFocus();
            return;
        }
        if(body.isEmpty()){
            editTextBody.setError("Body required");
            editTextBody.requestFocus();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://temperature-notification0.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.sendNotification(user.token,title,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(SendNotificationsActivity.this,response.body().string(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Notification sent");
                }catch (IOException e){
                    Log.d(TAG,"IOException");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"Notification failed");
            }
        });


    }
}
