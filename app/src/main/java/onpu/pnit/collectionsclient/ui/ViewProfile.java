package onpu.pnit.collectionsclient.ui;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.r0adkll.slidr.Slidr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;


public class ViewProfile extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        getSupportActionBar().setTitle(R.string.nav_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Slidr.attach(this);




    }



//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        TextView fullName = view.findViewById(R.id.tv_full_name);
//
//        AsyncTask asyncTask = new AsyncTask() {
//
//            @Override
//            protected Object doInBackground(Object[] objects) {
//
//                OkHttpClient client = new OkHttpClient();
//
//                Request request = new Request.Builder()
//                        .url("https://collections-blue.herokuapp.com/users")
//                        .build();
//
//                Response response = null;
//
//                try {
//                    response = client.newCall(request).execute();
//                    return response.body().string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                fullName.setText(o.toString());
//            }
//
//        };
//    }
}
