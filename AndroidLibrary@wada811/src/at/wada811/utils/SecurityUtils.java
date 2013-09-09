/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.wada811.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import at.wada811.android.library.BuildConfig;
import at.wada811.android.library.R;

public class SecurityUtils {

    /**
     * 正しく署名されているかチェックする
     * 
     * @param context
     * @return
     */
    public static boolean checkSigneture(Context context){
        PackageManager pm = context.getPackageManager();
        try{
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            // 通常[0]のみ
            for(Signature signature : packageInfo.signatures){
                // TODO ログを確認して署名を res/values/security.xml に記載する                
                LogUtils.d(signature.toCharsString());
                if(BuildConfig.DEBUG){
                    if(context.getString(R.string.debugKey).equals(signature.toCharsString())){
                        return true;
                    }
                }else{
                    if(context.getString(R.string.releaseKey).equals(signature.toCharsString())){
                        return true;
                    }
                }
            }
        }catch(NameNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 特定のパッケージがパーミッションを保持しているかチェックする
     * 
     * @param context
     * @param packageName
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String packageName, String permission){
        PackageManager pm = context.getPackageManager();
        return pm.checkPermission(permission, packageName) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * パーミッションが改竄されていないかチェックする
     * 
     * @param context
     * @param permissions
     *        想定するパーミッション
     * @return
     */
    public static List<String> diffPermissions(Context context, String[] permissions){
        return diffPermissions(context, new ArrayList<String>(Arrays.asList(permissions)));
    }

    /**
     * パーミッションが改竄されていないかチェックする
     * 
     * @param context
     * @param permissions
     *        想定するパーミッション
     * @return
     */
    private static List<String> diffPermissions(Context context, List<String> permissions){
        if((context == null) || (permissions == null)){
            return new ArrayList<String>();
        }
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if(requestedPermissions != null){
                for(String permission : requestedPermissions){
                    if(permissions.contains(permission)){
                        permissions.remove(permission);
                    }else{
                        permissions.add(permission);
                    }
                }
            }
            return permissions;
        }catch(NameNotFoundException e){
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

}
