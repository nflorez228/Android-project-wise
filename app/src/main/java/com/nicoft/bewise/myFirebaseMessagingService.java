package com.nicoft.bewise;

/**
 * Created by Nicolas on 16/10/2016.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class myFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        DatabaseLoginHelper myDb2 = new DatabaseLoginHelper(this);

        DatabaseHelper myDb = new DatabaseHelper(this);



        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            JSONObject obj = new JSONObject(remoteMessage.getData());
            try {
                String notificar = obj.get("show").toString();
                String title = obj.get("title").toString();
                String messag = obj.get("body").toString();
                String casa = obj.get("casa").toString();


                if(notificar.equals("true")) {
                    Intent intent = new Intent(obj.get("click_action").toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    String idnodo = obj.get("idnodo").toString();
                    if(idnodo!=null || !idnodo.equals("")) {
                        intent.putExtra("Noti", casa + ":?idNode=" + idnodo);
                    }
                    else {
                        intent.putExtra("Noti", casa + ":/");
                    }

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif);
                    NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);
                    notifBuilder.setContentTitle(title);
                    notifBuilder.setContentText(messag);
                    notifBuilder.setSmallIcon(R.mipmap.ic_launcher);
                    notifBuilder.setSound(defaultSoundUri);
                    notifBuilder.setAutoCancel(true);
                    notifBuilder.setContentIntent(pendingIntent);
                    NotificationManager notmanag = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notmanag.notify(0, notifBuilder.build());
                }
                else
                {
                    if(title.equals("CAMBIARPASS"))
                    {
                        String newp = messag.split(";")[1];
                        String oldp = messag.split(";")[0];

                        String newpass = newp.split(":")[1];
                        String oldpass = oldp.split(":")[1];
                        Cursor res = myDb2.getAllData();

                            res.moveToNext();
                            String id = res.getString(0);
                            String username = res.getString(1);
                            String email = res.getString(2);
                            String rid = res.getString(5);

                            myDb2.updateData(id,username,email,newpass,rid);


                    }
                    else if(title.equals("CAMBIARNOMBRE"))
                    {
                        String newn = messag.split(";")[1];
                        String oldn = messag.split(";")[0];

                        String newname = newn.split(":")[1];
                        String oldname = oldn.split(":")[1];
                        Cursor res = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);
                        boolean notfinished = true;
                        while(notfinished) {
                            res.moveToNext();
                            String lugar = res.getString(1);
                            if(lugar.equalsIgnoreCase(casa))
                            {
                                myDb.updateData(DatabaseHelper.TABLE_NAME_1,res.getString(0),newname,res.getString(2),"http://"+newname+".en.bewise.com.co");
                                notfinished=false;
                                Intent intent = new Intent(obj.get("click_action").toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("Noti", "action");
                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                                Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif);
                                NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);
                                notifBuilder.setContentTitle("Cambio de nombre");
                                notifBuilder.setContentText("La casa: "+oldname+" ha cambiado de nombre a: "+newname);
                                notifBuilder.setSmallIcon(R.mipmap.ic_launcher);
                                notifBuilder.setSound(defaultSoundUri);
                                notifBuilder.setAutoCancel(true);
                                notifBuilder.setContentIntent(pendingIntent);
                                NotificationManager notmanag = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notmanag.notify(0, notifBuilder.build());
                            }

                        }
                    }
                    else if(title.equals("CAMBIARIMAGEN"))
                    {
                        Cursor res = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);
                        boolean notfinished = true;
                        while(notfinished) {
                            res.moveToNext();
                            String lugar = res.getString(1);
                            if(lugar.equalsIgnoreCase(casa))
                            {
                                notfinished=false;
                                Cursor res2 = myDb2.getAllData();
                                res2.moveToNext();

                                try {
                                    final Socket socket = IO.socket(res.getString(3));
                                    socket.connect();
                                    socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {

                                        }
                                    });
                                    socket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {

                                        }
                                    });
                                    JSONObject obj2 = new JSONObject("{username:\"" + res2.getString(2) + "\",password:\"" + res2.getString(3) + "\"}");
                                    socket.emit("authentication", obj2);
                                    //socket.emit("MOBILEIMG");
                                    socket.on("GETIMG",new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            String cun= (String) args[0];

                                        }
                                    });
                                    socket.on("authenticated", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            socket.emit("authenticated");

                                            //socket.emit("MOBILEIMG");
                                            /*socket.on("MOBILEIMGSET",new Emitter.Listener() {
                                                @Override
                                                public void call(Object... args) {
                                                    String cun= (String) args[0];

                                                }
                                            });*/
                                        }
                                    });
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    else if(title.equals("RESET"))
                    {
                        Cursor res = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);
                        boolean notfinished = true;
                        while(notfinished) {
                            res.moveToNext();
                            String lugar = res.getString(1);
                            if(lugar.equalsIgnoreCase(casa))
                            {
                                myDb.deleteData(DatabaseHelper.TABLE_NAME_1, res.getString(0));
                                notfinished=false;
                                Intent intent = new Intent(obj.get("click_action").toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("Noti", "action");
                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                                Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif);
                                NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);
                                notifBuilder.setContentTitle("Casa Borrada");
                                notifBuilder.setContentText("La casa: "+casa+" ha sido restaurada a sus valores de fabrica");
                                notifBuilder.setSmallIcon(R.mipmap.ic_launcher);
                                notifBuilder.setSound(defaultSoundUri);
                                notifBuilder.setAutoCancel(true);
                                notifBuilder.setContentIntent(pendingIntent);
                                NotificationManager notmanag = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notmanag.notify(0, notifBuilder.build());
                            }

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
            //sendNotification3(remoteMessage);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void sendNotification2(RemoteMessage message) {
        String title = message.getNotification().getTitle();
        String messag = message.getNotification().getBody();
        String click_action = message.getNotification().getClickAction();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);
        notifBuilder.setContentTitle(title);
        notifBuilder.setContentText(messag);
        notifBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notifBuilder.setSound(defaultSoundUri);
        notifBuilder.setAutoCancel(true);
        notifBuilder.setContentIntent(pendingIntent);
        NotificationManager notmanag = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notmanag.notify(0,notifBuilder.build());
    }
    private void sendNotification3(RemoteMessage message) {
        String title = message.getNotification().getTitle();
        String messag = message.getNotification().getBody();
        String click_action = message.getNotification().getClickAction();
        Intent intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);
        notifBuilder.setContentTitle(title);
        notifBuilder.setContentText(messag);
        notifBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notifBuilder.setSound(defaultSoundUri);
        notifBuilder.setAutoCancel(true);
        notifBuilder.setContentIntent(pendingIntent);
        NotificationManager notmanag = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notmanag.notify(0,notifBuilder.build());
    }


}
