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

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IntentUtils {

    /**
     * Intent可能なActivityを返す
     *
     * @param context
     * @param intent
     * @return
     */
    public static List<ResolveInfo> queryIntentActivities(Context context, Intent intent){
        PackageManager packageManager = context.getPackageManager();
        return packageManager.queryIntentActivities(intent, 0);
    }

    /**
     * Intent可能かを返す
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean canIntent(Context context, Intent intent){
        List<ResolveInfo> activities = IntentUtils.queryIntentActivities(context, intent);
        return activities != null && !activities.isEmpty();
    }

    /**
     * PendingIntent for OneShot Notification
     *
     * @param context
     * @return
     */
    public static PendingIntent createOneShotPendingIntent(Context context){
        int requestCode = 0; // Private request code for the sender (currently not used).
        Intent intent = new Intent();
        return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    /**
     * ファイルをViewerアプリで開くIntentを作成する
     *
     * @param filePath
     * @param mimeType
     * @return intent
     */
    public static Intent createFileViewIntent(String filePath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), MediaUtils.getMimeType(filePath));
        return intent;
    }

    /**
     * ファイルをViewerアプリで開くPendingIntentを作成する
     *
     * @param context
     * @param filePath
     * @param mimeType
     * @return pendingIntent
     */
    public static PendingIntent createFileViewPendingIntent(Context context, String filePath){
        int requestCode = 0; // Private request code for the sender (currently not used).
        Intent intent = IntentUtils.createFileViewIntent(filePath);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * メールを送信するIntentを作成する
     *
     * @param mailto
     * @param subject
     * @param body
     */
    public static Intent createSendMailIntent(String mailto, String subject, String body){
        return IntentUtils.createSendMailIntent(new String[]{
            mailto
        }, new String[]{}, new String[]{}, subject, body);
    }

    /**
     * メールを送信するIntentを作成する
     *
     * @param mailto
     * @param subject
     * @param body
     */
    public static Intent createSendMailIntent(String[] mailto, String subject, String body){
        return IntentUtils.createSendMailIntent(mailto, new String[]{}, new String[]{}, subject, body);
    }

    /**
     * メールを送信するIntentを作成する
     *
     * @param mailto
     * @param cc
     * @param bcc
     * @param subject
     * @param body
     */
    public static Intent createSendMailIntent(String[] mailto, String[] cc, String[] bcc, String subject, String body){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_EMAIL, mailto);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.putExtra(Intent.EXTRA_BCC, bcc);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }

    /**
     * メールIntentにファイルを添付する
     *
     * @param intent
     * @param filePath
     * @return
     */
    public static Intent addFile(Intent intent, String filePath){
        return IntentUtils.addFile(intent, new File(filePath));
    }

    /**
     * メールIntentにファイルを添付する
     *
     * @param intent
     * @param file
     * @return
     */
    public static Intent addFile(Intent intent, File file){
        return IntentUtils.addFile(intent, Uri.fromFile(file), MediaUtils.getMimeType(file));
    }

    /**
     * メールIntentにファイルを添付する
     *
     * @param intent
     * @param uri
     * @param mimeType
     * @return
     */
    public static Intent addFile(Intent intent, Uri uri, String mimeType){
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        return intent;
    }

    /**
     * メールIntentに複数ファイルを送信する
     *
     * @param intent
     * @param filePaths
     * @param mimeType
     * @return
     */
    public static Intent addFiles(Intent intent, ArrayList<String> filePaths, String mimeType){
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType(mimeType);
        ArrayList<Uri> uris = new ArrayList<Uri>(filePaths.size());
        for(String filePath : filePaths){
            uris.add(Uri.fromFile(new File(filePath)));
        }
        intent.putExtra(Intent.EXTRA_STREAM, uris);
        return intent;
    }

    /**
     * Gmail送信Intentを作成する
     *
     * @param intent
     * @return
     */
    public static Intent createGmailIntent(Intent baseIntent){
        Intent intent = new Intent(baseIntent);
        intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        return intent;
    }

    /**
     * ブラウザを開くIntentを作成する
     *
     * @param url
     */
    public static Intent createOpenBrowserIntent(String url){
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    /**
     * ダイアルを開くIntentを作成する
     *
     * @param context
     * @param tel
     */
    public static Intent createOpenDialIntent(String tel){
        return new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + tel));
    }

    /**
     * 電話をかけるIntentを作成する
     *
     * @param tel
     */
    public static Intent createCallIntent(String tel){
        return new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
    }

    /**
     * 電話をかけるIntentを作成する
     *
     * @param context
     * @param tel
     */
    public static Intent createDailIntent(String tel){
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
    }

    /**
     * 設定を開くIntentを作成する
     */
    public static Intent createOpenSettingsIntent(){
        return new Intent("android.settings.SETTINGS");
    }

    /**
     * アンインストール画面を開くIntentを作成する
     *
     * @param packageName
     */
    public static Intent createOpenUninstallIntent(String packageName){
        return new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", packageName, null));
    }

    /**
     * マーケットを開くIntentを作成する
     *
     * @param packageName
     */
    public static Intent createOpenMarketIntent(String packageName){
        return new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
    }

    /**
     * マーケットを検索するIntentを作成する
     *
     * @param query
     */
    public static Intent createSearchMarketIntent(String query){
        return new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + query));
    }

    /**
     * テキストを送るIntentを作成する
     */
    public static Intent createSendTextIntent(String text){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    /**
     * 画像を送るIntentを作成する
     */
    public static Intent createSendImageIntent(String filePath){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(MediaUtils.getMimeType(filePath));
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
        return intent;
    }

    public static String dump(Intent intent) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("action", intent.getAction());
        if(intent.getCategories() != null){
            JSONArray categories = new JSONArray();
            for(String category : intent.getCategories()){
                categories.put(category);
            }
            json.put("category", categories);
        }
        json.put("type", intent.getType());
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            JSONObject extras = new JSONObject();
            for(String key : bundle.keySet()){
                extras.put(key, bundle.get(key));
            }
            json.put("extras", extras);
        }
        return json.toString(4);
    }
}
