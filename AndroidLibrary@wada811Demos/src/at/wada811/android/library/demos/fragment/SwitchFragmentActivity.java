package at.wada811.android.library.demos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.wada811.android.library.demos.R;

public class SwitchFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        ((Button)findViewById(R.id.replace)).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Fragment fragment;
                Fragment prevFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                if(prevFragment instanceof SwitchShowFragment){
                    fragment = SwitchHideFragment.newInstance();
                }else{
                    fragment = SwitchShowFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            }
        });
        ((Button)findViewById(R.id.add_remove)).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Fragment prevFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                if(prevFragment == null || prevFragment instanceof SwitchHideFragment){
                    Fragment fragment = SwitchShowFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
                }else{
                    getSupportFragmentManager().beginTransaction().remove(prevFragment).commit();
                }
            }
        });
    }
}
