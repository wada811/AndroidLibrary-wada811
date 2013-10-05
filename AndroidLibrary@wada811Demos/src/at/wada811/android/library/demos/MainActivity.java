package at.wada811.android.library.demos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import at.wada811.android.library.demos.ui.CircleImageActivity;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, CircleImageActivity.class));
        finish();
    }
}
