package onpu.pnit.collectionsclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

           ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
           NetworkInfo.State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

           if(wifiState == NetworkInfo.State.CONNECTED || mobileState == NetworkInfo.State.CONNECTED){
               Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
           }
//
       }
    }
}
