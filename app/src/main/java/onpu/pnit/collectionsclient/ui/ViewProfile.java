package onpu.pnit.collectionsclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.r0adkll.slidr.Slidr;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.User;


public class ViewProfile extends AppCompatActivity {

    public static final int EDIT_USER_REQUEST = 1;
    @BindView(R.id.my_username)
    TextView username;
    @BindView(R.id.my_place_of_living)
    TextView placeOfLiving;
    @BindView(R.id.my_info)
    TextView infoAboutUser;
    @BindView(R.id.items_value)
    TextView items_value;
    @BindView(R.id.collections_value)
    TextView collections_value;
    private User user;
    int REQUEST_CODE = 0;
    @OnClick(R.id.edit_button)
    public void handleClick(View view){
        Intent i = new Intent(this,UserEditActivity.class);
        startActivityForResult(i,REQUEST_CODE);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        getSupportActionBar().setTitle(R.string.nav_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        ButterKnife.bind(this);

       // initViewModel();

    }


  /*  private void initViewModel(){
        user = ViewModelProviders.of(this).get();
        int user_id = getIntent().getIntExtra();
    }*/



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
