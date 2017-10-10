package com.example.blurryface.audioapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;
    private static Button playButton;
    private static Button stopButton;
    private static Button recordButton;
    private boolean isRecording = false;
    private static String audioPath;
    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = (Button)findViewById(R.id.playButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        recordButton = (Button)findViewById(R.id.recordButton);
        audioPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myaudio.3gpp";
        if(!hasMicrophone()){
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
            recordButton.setEnabled(false);
        }else {
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
            recordButton.setEnabled(true);
        }
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_REQUEST_CODE);
        requestPermission(Manifest.permission.RECORD_AUDIO,RECORD_REQUEST_CODE);
    }
    //checking if phone has a microphone
    public boolean hasMicrophone()
    {
        PackageManager packageManager = this.getPackageManager();
        boolean result = packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        return result;
    }
    //record button
    public void recordAudio(View view) throws IOException
    {
        try {
        isRecording=true;
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioPath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //stop button
    public void stopAudio(View view){
        stopButton.setEnabled(false);
        playButton.setEnabled(true);
        if(isRecording){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
        else {
            recordButton.setEnabled(true);
            mediaRecorder.release();
            mediaRecorder=null;
        }
    }
    //play button
    public void playAudio(View view) throws IOException{
        stopButton.setEnabled(true);
        recordButton.setEnabled(false);
        playButton.setEnabled(false);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioPath);
        mediaPlayer.start();
    }
    //method for permissions
    protected void requestPermission(String permissionType,int requestCode)
    {
        int permission = ContextCompat.checkSelfPermission(this,permissionType);
        if(permission==PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this,new String[]{permissionType},requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RECORD_REQUEST_CODE:
                //if user denied permission
                if(grantResults.length==0||grantResults[0]==PackageManager.PERMISSION_DENIED){
                    recordButton.setEnabled(false);
                    Toast.makeText(this,"Permission for recording is required",Toast.LENGTH_LONG).show();
                }
                break;
            case STORAGE_REQUEST_CODE:
                //if user denys permission
                if(grantResults.length==0||grantResults[0]==PackageManager.PERMISSION_DENIED){
                    recordButton.setEnabled(false);
                    Toast.makeText(this,"Permission for recording is required",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
