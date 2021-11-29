package com.softdroid.puzzlev2;

import static android.os.Build.*;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

@RequiresApi(api = VERSION_CODES.O)
public class MediaPlayerService extends Service {

    MediaPlayer mediaPlayer;
    int option;
    String CHANNEL_ID = "1";
    Uri patchSound;
    String patchSoundString;

    public IBinder onBind(Intent intent) {

        return null;
    }

    //método que se inicia al llamar a startService() y pasarle un intent
    //En el se crea la instancia mediaplayer y se pausa o activa dependiendo si está sonando o no
    public int onStartCommand(Intent intent, int flags, int startId) {



        //en al variable option se recog el valor del int option procedente del menu y que se envia
        //mediante el intent
        option = intent.getIntExtra ("option", option);
        //patchSound = intent.getStringExtra ("patchSound");
        Log.d("tag", String.valueOf ( option ) );
        /*
        Si el valor es 1 se inicia el mediaplayer, si es cualquier otro valor se pausa o se reanuda segun el estado del objeto
        PERO SI EL VALOR ES 3 SE PARA EL REPPRODUCTOR. Ahora mismo ese intent con el valor 3 se manda
        desde la congrats activity, si se intenta parar y crear el canal de notificacion y mostrar
        la notificación, la aplicación peta
         */


        if (option == 1) {
            mediaPlayer = MediaPlayer.create ( this, R.raw.game1 );
            mediaPlayer.setLooping ( true );

        }else if (option == 3) {
            mediaPlayer.stop();

        }else if (option == 4) {
            mediaPlayer.pause();

        }else if (option == 7) {
            onDestroy();

        }
        else if (option == 5) {
            mediaPlayer.start();

        }else if (option == 6) {

            patchSoundString = intent.getStringExtra ("cancacion");
            Log.d("patch string", String.valueOf ( patchSoundString ) );
            patchSound = Uri.parse(patchSoundString);
            mediaPlayer = MediaPlayer.create ( this, patchSound);
            mediaPlayer.setLooping ( true );

               }else {
            if (mediaPlayer.isPlaying ())
                mediaPlayer.pause();
            else
                mediaPlayer.start();
        }
        return START_STICKY;
    }

    //metodo destructor que para el objeto mediaplayer
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(Integer.parseInt("channel"));
            String description = getString(Integer.parseInt("decription"));
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void displayNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder ( this );
        builder
                .setContentTitle ( "Test" )
                .setContentText ( "Test" )
                .setSmallIcon ( R.mipmap.ic_launcher )
                .setVisibility ( NotificationCompat.VISIBILITY_PUBLIC );
    }

}
