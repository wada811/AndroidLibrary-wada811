package at.wada811.android.library.demos;

import java.util.Comparator;
import java.util.List;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // サンプルActivityのリストを取得
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage(getPackageName());
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        ActivityListAdapter adapter = new ActivityListAdapter(this);
        for(ResolveInfo info : activities){
            adapter.add(info.activityInfo);
        }
        // パッケージ名でソート
        adapter.sort(new Comparator<ActivityInfo>(){
            public int compare(ActivityInfo lhs, ActivityInfo rhs){
                return lhs.name.compareTo(rhs.name);
            };
        });
        // ListFragmentを初期化
        FragmentManager fragmentManager = getSupportFragmentManager();
        ActivityListFragment activityListFragment = new ActivityListFragment();
        fragmentManager.beginTransaction().add(R.id.fragment, activityListFragment).commit();
        activityListFragment.setListAdapter(adapter);
    }
}
