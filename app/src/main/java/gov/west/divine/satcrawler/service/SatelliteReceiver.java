package gov.west.divine.satcrawler.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import gov.west.divine.satcrawler.MainActivity;
import gov.west.divine.satcrawler.R;
import gov.west.divine.satcrawler.dao.SatellitesDatabaseHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SatelliteReceiver extends BroadcastReceiver {
    private SatBeamsCrawlerService satBeamsCrawlerService;
    private LyngSatCrawlerService lyngSatCrawlerService;

    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(() -> {
            if (isOnline()){
                this.satBeamsCrawlerService = new SatBeamsCrawlerService(SatellitesDatabaseHelper.getInstance(context));
                boolean satBeamHasReport = satBeamsCrawlerService.hasNewReports();

                this.lyngSatCrawlerService = new LyngSatCrawlerService(SatellitesDatabaseHelper.getInstance(context));
                boolean lyngSatHasReport = lyngSatCrawlerService.hasNewReports();

                if (satBeamHasReport || lyngSatHasReport){
                    showNotification("You have new reports", context);
                }
            }
        }).start();
    }

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    public void showNotification(String text, Context context){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SatelliteService");

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Satellite Crawler Service")
                .setAutoCancel(true)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(0, notification);
    }
}
