package at.wada811.android.library.demos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ExpandableListView;
import at.wada811.android.library.demos.ActivityListFragment.ExpandableListListener;
import at.wada811.android.library.demos.ActivityListFragment.ExpandableListListenerProvider;
import at.wada811.android.library.demos.broadcastreceiver.DateTimeChangeReceiver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class MainActivity extends FragmentActivity implements ExpandableListListenerProvider {

    private ActivityListAdapter mActivityListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSampleList();
        DateTimeChangeReceiver.registerDateChangeReceiver(this);
    }

    private void setupSampleList(){
        // サンプルActivityのリストを取得
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage(getPackageName());
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // resolveInfos から activityInfos を取得
        List<ActivityInfo> activityInfos = new ArrayList<ActivityInfo>();
        for(ResolveInfo resolveInfo : resolveInfos){
            activityInfos.add(resolveInfo.activityInfo);
        }
        // パッケージ名でソート
        Collections.sort(activityInfos, new Comparator<ActivityInfo>(){
            @Override
            public int compare(ActivityInfo lhs, ActivityInfo rhs){
                return lhs.name.compareTo(rhs.name);
            }
        });
        // packages の生成
        TreeSet<String> packages = new TreeSet<String>();
        // activities の生成
        HashMap<String, ArrayList<ActivityInfo>> activities = new HashMap<String, ArrayList<ActivityInfo>>();
        for(ActivityInfo activityInfo : activityInfos){
            String[] splitsPackageName = activityInfo.name.split("\\.");
            String packageName = splitsPackageName[splitsPackageName.length - 2];
            packages.add(packageName);
            ArrayList<ActivityInfo> activityList = activities.get(packageName);
            if(activityList == null){
                activityList = new ArrayList<ActivityInfo>();
            }
            activityList.add(activityInfo);
            activities.put(packageName, activityList);
        }
        mActivityListAdapter = new ActivityListAdapter(this, new ArrayList<String>(packages), activities);
        // ListFragmentを初期化
        FragmentManager fragmentManager = getSupportFragmentManager();
        ActivityListFragment activityListFragment = ActivityListFragment.newInstance("Activity Not Found!");
        fragmentManager.beginTransaction().replace(R.id.fragment, activityListFragment).commit();
        activityListFragment.setListAdapter(mActivityListAdapter);
    }

    @Override
    public ExpandableListListener getExpandableListListener(){
        return new ExpandableListListener(){
            @Override
            public void onGroupExpand(int groupPosition){

            }

            @Override
            public void onGroupCollapse(int groupPosition){

            }

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id){
                return false;
            }

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id){
                ActivityInfo activityInfo = mActivityListAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent();
                intent.setClassName(activityInfo.packageName, activityInfo.name);
                startActivity(intent);
                return false;
            }
        };
    }
}
