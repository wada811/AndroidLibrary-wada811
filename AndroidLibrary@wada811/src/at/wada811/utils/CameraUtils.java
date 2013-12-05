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

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.os.Build;
import at.wada811.android.library.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class CameraUtils {

    private static final boolean DEBUG = false;

    /**
     * カメラ機能を持っているか
     * 
     * @param context
     * @return
     */
    public static boolean hasFeatureCamera(Context context){
        boolean hasCamera = false;
        if(PreferenceUtils.contains(context, PackageManager.FEATURE_CAMERA)){
            hasCamera = PreferenceUtils.getBoolean(context, PackageManager.FEATURE_CAMERA, hasCamera);
        }else{
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
            PreferenceUtils.putBoolean(context, PackageManager.FEATURE_CAMERA, hasCamera);
        }
        return hasCamera;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean isCameraDisabled(Context context){
        if(AndroidUtils.isMoreThanBuildVersion(Build.VERSION_CODES.ICE_CREAM_SANDWICH)){
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            return devicePolicyManager.getCameraDisabled(null);
        }else{
            return false;
        }
    }

    /**
     * オートフォーカス機能を持っているか
     * 
     * @param context
     * @return
     */
    public static boolean hasFeatureAutoFocus(Context context){
        boolean hasAutoFocus = false;
        if(PreferenceUtils.contains(context, PackageManager.FEATURE_CAMERA_AUTOFOCUS)){
            hasAutoFocus = PreferenceUtils.getBoolean(context, PackageManager.FEATURE_CAMERA_AUTOFOCUS, hasAutoFocus);
        }else{
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            hasAutoFocus = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
            PreferenceUtils.putBoolean(context, PackageManager.FEATURE_CAMERA_AUTOFOCUS, hasAutoFocus);
        }
        return hasAutoFocus;
    }

    /**
     * カメラ情報を取得する
     * 
     * @param nCameraId
     * @return
     */
    public static CameraInfo getCameraInfo(int nCameraId){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(nCameraId, info);
        return info;
    }

    /**
     * 背面カメラかどうかを返す
     * 
     * @param info
     * @return
     */
    public static boolean isBackCamera(CameraInfo info){
        return info.facing == CameraInfo.CAMERA_FACING_BACK;
    }

    /**
     * カメラの向きを取得する
     * 
     * @param activity
     * @param nCameraId
     * @return displayOrientation
     */
    public static int getCameraDisplayOrientation(Context context, CameraInfo info){
        if(AndroidUtils.isMoreThanBuildVersion(Build.VERSION_CODES.GINGERBREAD)){
            int degrees = DisplayUtils.getRotationDegrees(context);
            if(DEBUG){
                LogUtils.v("degrees: " + degrees);
            }
            int displayOrientation;
            if(CameraUtils.isBackCamera(info)){
                // アウトカメラ
                if(DEBUG){
                    LogUtils.v(String.valueOf(degrees));
                }
                if(DEBUG){
                    LogUtils.v(String.valueOf(info.orientation));
                }
                displayOrientation = ((360 - degrees) + info.orientation) % 360;
                if(DEBUG){
                    LogUtils.v(String.valueOf(displayOrientation));
                }
                displayOrientation = (360 + displayOrientation) % 360;
                if(DEBUG){
                    LogUtils.v(String.valueOf(displayOrientation));
                }
            }else{
                // インカメラ
                if(DEBUG){
                    LogUtils.v(String.valueOf(degrees));
                }
                if(DEBUG){
                    LogUtils.v(String.valueOf(info.orientation));
                }
                displayOrientation = (360 - degrees - info.orientation) % 360;
                if(DEBUG){
                    LogUtils.v(String.valueOf(displayOrientation));
                }
                displayOrientation = (360 + displayOrientation) % 360;
                if(DEBUG){
                    LogUtils.v(String.valueOf(displayOrientation));
                }
            }
            return displayOrientation;
        }
        return 90;
    }

    /**
     * 画像の向きを取得する
     * 
     * @param cameraId
     * @param displayOrientation
     * @return pictureOrientaion
     */
    public static int getPictureOrientaion(CameraInfo info, int displayOrientation){
        int pictureOrientaion;
        if(CameraUtils.isBackCamera(info)){
            pictureOrientaion = displayOrientation;
        }else{
            pictureOrientaion = (360 - displayOrientation) % 360;
        }
        return pictureOrientaion;
    }

    public static Size getPictureSize(Context context, Camera camera){
        final List<Size> sizes = camera.getParameters().getSupportedPictureSizes();
        final double ASPECT_TOLERANCE = 0.07;
        final boolean isPortrait = DisplayUtils.isPortrait(context);
        final int width = DisplayUtils.getWidth(context);
        final int height = DisplayUtils.getHeight(context);
        if(DEBUG){
            LogUtils.v("width: " + width);
        }
        if(DEBUG){
            LogUtils.v("height: " + height);
        }
        final double targetRatio = isPortrait ? (double)height / width : (double)width / height;
        final int targetHeight = isPortrait ? width : height;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        if(DEBUG){
            LogUtils.v("targetRatio: " + targetRatio);
        }
        for(Size size : sizes){
            double pictureRatio = isPortrait ? (double)size.height / size.width : (double)size.width / size.height;
            pictureRatio = (double)size.width / size.height;
            if(DEBUG){
                LogUtils.v("size.width: " + size.width);
            }
            if(DEBUG){
                LogUtils.v("size.height: " + size.height);
            }
            if(DEBUG){
                LogUtils.v("pictureRatio: " + pictureRatio);
            }
            if(Math.abs(pictureRatio - targetRatio) > ASPECT_TOLERANCE){
                continue;
            }
            if(Math.abs(size.height - targetHeight) < minDiff){
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if(optimalSize == null){
            minDiff = Double.MAX_VALUE;
            for(Size size : sizes){
                if(Math.abs(size.height - targetHeight) < minDiff){
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        if(DEBUG){
            LogUtils.v("optimalSize.width: " + optimalSize.width);
        }
        if(DEBUG){
            LogUtils.v("optimalSize.height: " + optimalSize.height);
        }
        return optimalSize;
    }

    /**
     * 撮影サイズをセットする
     * 
     * @param camera
     * @param pictureSize
     */
    public static void setPictureSize(Camera camera, Size pictureSize){
        Parameters params = camera.getParameters();
        params.setPictureSize(pictureSize.width, pictureSize.height);
        camera.setParameters(params);
    }

    /**
     * Returns the best preview size
     * 
     * @param context
     * @param camera
     */
    public static Size getOptimalPreviewSize(Context context, Camera camera){
        final List<Size> sizes = camera.getParameters().getSupportedPreviewSizes();
        final double ASPECT_TOLERANCE = 0.07;
        final boolean isPortrait = DisplayUtils.isPortrait(context);
        final Size pictureSize = camera.getParameters().getPictureSize();
        final int width = pictureSize.width;
        final int height = pictureSize.height;
        if(DEBUG){
            LogUtils.v("width: " + width);
        }
        if(DEBUG){
            LogUtils.v("height: " + height);
        }
        final double targetRatio = isPortrait ? (double)height / width : (double)width / height;
        final int targetHeight = isPortrait ? width : height;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        if(DEBUG){
            LogUtils.v("targetRatio: " + targetRatio);
        }
        for(Size size : sizes){
            double previewRatio = isPortrait ? (double)size.height / size.width : (double)size.width / size.height;
            previewRatio = (double)size.width / size.height;
            if(DEBUG){
                LogUtils.v("size.width: " + size.width);
            }
            if(DEBUG){
                LogUtils.v("size.height: " + size.height);
            }
            if(DEBUG){
                LogUtils.v("previewRatio: " + previewRatio);
            }
            if(Math.abs(previewRatio - targetRatio) > ASPECT_TOLERANCE){
                continue;
            }
            if(Math.abs(size.height - targetHeight) < minDiff){
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if(optimalSize == null){
            minDiff = Double.MAX_VALUE;
            for(Size size : sizes){
                if(Math.abs(size.height - targetHeight) < minDiff){
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        if(DEBUG){
            LogUtils.v("optimalSize.width: " + optimalSize.width);
        }
        if(DEBUG){
            LogUtils.v("optimalSize.height: " + optimalSize.height);
        }
        return optimalSize;
    }

    /**
     * プレビュー画面のサイズをセットする
     * 
     * @param camera
     * @param previewSize
     */
    public static void setPreviewSize(Camera camera, Size previewSize){
        Parameters params = camera.getParameters();
        params.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(params);
    }

    /**
     * 画面サイズとプレビューサイズ、それらのアスペクト比を保存する
     * 
     * @param context
     * @param camera
     * @return scaledPreviewSize
     */
    public static Size getScaledPreviewSize(Context context, Camera camera){
        Size previewSize = camera.getParameters().getPreviewSize();
        boolean isPortrait = DisplayUtils.isPortrait(context);
        final int displayWidth = DisplayUtils.getWidth(context);
        final int displayHeight = DisplayUtils.getHeight(context);
        if(DEBUG){
            LogUtils.v("displayWidth: " + displayWidth);
        }
        if(DEBUG){
            LogUtils.v("displayHeight: " + displayHeight);
        }
        PreferenceUtils.putInt(context, context.getString(R.string.keyDisplayWidth), displayWidth);
        PreferenceUtils.putInt(context, context.getString(R.string.keyDisplayHeight), displayHeight);
        final int previewWidth = isPortrait ? previewSize.height : previewSize.width;
        final int previewHeight = isPortrait ? previewSize.width : previewSize.height;
        if(DEBUG){
            LogUtils.v("previewWidth: " + previewWidth);
        }
        if(DEBUG){
            LogUtils.v("previewHeight: " + previewHeight);
        }
        PreferenceUtils.putInt(context, context.getString(R.string.keyPreviewWidth), previewWidth);
        PreferenceUtils.putInt(context, context.getString(R.string.keyPreviewHeight), previewHeight);
        double displayRatio = displayHeight > displayWidth ? (double)displayHeight / displayWidth : (double)displayWidth / displayHeight;
        double previewRatio = previewHeight > previewWidth ? (double)previewHeight / previewWidth : (double)previewWidth / previewHeight;
        if(DEBUG){
            LogUtils.v("displayRatio: " + displayRatio);
        }
        if(DEBUG){
            LogUtils.v("previewRatio: " + previewRatio);
        }
        PreferenceUtils.putFloat(context, context.getString(R.string.keyDisplayRatio), (float)displayRatio);
        PreferenceUtils.putFloat(context, context.getString(R.string.keyPreviewRatio), (float)previewRatio);
        int scaledChildWidth = displayWidth;
        int scaledChildHeight = displayHeight;
        if(displayRatio <= previewRatio){
            if(isPortrait){
                if(DEBUG){
                    LogUtils.v("Portrait");
                }
                scaledChildWidth = (displayHeight * previewWidth) / previewHeight;
                scaledChildHeight = (scaledChildWidth * previewHeight) / previewWidth;
            }else{
                if(DEBUG){
                    LogUtils.v("Landscape");
                }
                scaledChildHeight = (displayWidth * previewHeight) / previewWidth;
                scaledChildWidth = (scaledChildHeight * previewWidth) / previewHeight;
            }
        }else{
            if(isPortrait){
                if(DEBUG){
                    LogUtils.v("Portrait");
                }
                scaledChildHeight = (displayWidth * previewHeight) / previewWidth;
                scaledChildWidth = (scaledChildHeight * previewWidth) / previewHeight;
            }else{
                if(DEBUG){
                    LogUtils.v("Landscape");
                }
                scaledChildWidth = (displayHeight * previewWidth) / previewHeight;
                scaledChildHeight = (scaledChildWidth * previewHeight) / previewWidth;
            }
        }
        if(DEBUG){
            LogUtils.v("scaledChildWidth: " + scaledChildWidth);
        }
        if(DEBUG){
            LogUtils.v("scaledChildHeight: " + scaledChildHeight);
        }
        PreferenceUtils.putInt(context, context.getString(R.string.keyScaledChildWidth), scaledChildWidth);
        PreferenceUtils.putInt(context, context.getString(R.string.keyScaledChildHeight), scaledChildHeight);
        int scaledWidth = scaledChildWidth;
        int scaledHeight = scaledChildHeight;
        if(displayRatio <= previewRatio){
            if(isPortrait){
                if(DEBUG){
                    LogUtils.v("Portrait");
                }
                scaledHeight = (displayWidth * previewHeight) / previewWidth;
                scaledWidth = (scaledHeight * previewWidth) / previewHeight;
            }else{
                if(DEBUG){
                    LogUtils.v("Landscape");
                }
                scaledWidth = (displayHeight * previewWidth) / previewHeight;
                scaledHeight = (scaledWidth * previewHeight) / previewWidth;
            }
        }else{
            if(isPortrait){
                if(DEBUG){
                    LogUtils.v("Portrait");
//                scaledWidth = (displayHeight * previewWidth) / previewHeight;
//                scaledHeight = (scaledWidth * previewHeight) / previewWidth;
                }
            }else{
                if(DEBUG){
                    LogUtils.v("Landscape");
//                scaledHeight = (displayWidth * previewHeight) / previewWidth;
//                scaledWidth = (scaledHeight * previewWidth) / previewHeight;
                }
            }
        }
        if(DEBUG){
            LogUtils.v("scaledWidth: " + scaledWidth);
        }
        if(DEBUG){
            LogUtils.v("scaledHeight: " + scaledHeight);
        }
        PreferenceUtils.putInt(context, context.getString(R.string.keyScaledWidth), scaledWidth);
        PreferenceUtils.putInt(context, context.getString(R.string.keyScaledHeight), scaledHeight);
        return camera.new Size(scaledWidth, scaledHeight);
    }

    /**
     * カメラパラメータをJSONに変換する
     * 
     * @param params
     * @return
     */
    public static String toJson(Camera.Parameters params){
        String[] data = params.flatten().split(";");
        JSONObject json = new JSONObject();
        for(String element : data){
            String[] splits = element.split("=");
            String key = splits[0];
            if(DEBUG){
                LogUtils.v(key);
            }
            String value = splits.length > 1 ? splits[1] : "";
            if(DEBUG){
                LogUtils.v(value);
            }
            try{
                json.put(key, value);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return json.toString();
    }

    /**
     * プロファイルをJSONに変換する
     * 
     * @param params
     * @return
     */
    public static String toJson(CamcorderProfile camcorderProfile){
        JSONObject json = new JSONObject();
        try{
            json.put("audioBitRate: ", camcorderProfile.audioBitRate);
            json.put("audioChannels: ", camcorderProfile.audioChannels);
            json.put("audioCodec: ", camcorderProfile.audioCodec);
            json.put("audioSampleRate: ", camcorderProfile.audioSampleRate);
            json.put("duration: ", camcorderProfile.duration);
            json.put("fileFormat: ", camcorderProfile.fileFormat);
            json.put("quality: ", camcorderProfile.quality);
            json.put("videoBitRate: ", camcorderProfile.videoBitRate);
            json.put("videoCodec: ", camcorderProfile.videoCodec);
            json.put("videoFrameHeight: ", camcorderProfile.videoFrameHeight);
            json.put("videoFrameRate: ", camcorderProfile.videoFrameRate);
            json.put("videoFrameWidth: ", camcorderProfile.videoFrameWidth);
        }catch(JSONException e){
            e.printStackTrace();
        }
        if(DEBUG){
            LogUtils.v("camcorderProfile.audioBitRate: " + camcorderProfile.audioBitRate);
            LogUtils.v("camcorderProfile.audioChannels: " + camcorderProfile.audioChannels);
            LogUtils.v("camcorderProfile.audioCodec: " + camcorderProfile.audioCodec);
            LogUtils.v("camcorderProfile.audioSampleRate: " + camcorderProfile.audioSampleRate);
            LogUtils.v("camcorderProfile.duration: " + camcorderProfile.duration);
            LogUtils.v("camcorderProfile.fileFormat: " + camcorderProfile.fileFormat);
            LogUtils.v("camcorderProfile.quality: " + camcorderProfile.quality);
            LogUtils.v("camcorderProfile.videoBitRate: " + camcorderProfile.videoBitRate);
            LogUtils.v("camcorderProfile.videoCodec: " + camcorderProfile.videoCodec);
            LogUtils.v("camcorderProfile.videoFrameHeight: " + camcorderProfile.videoFrameHeight);
            LogUtils.v("camcorderProfile.videoFrameRate: " + camcorderProfile.videoFrameRate);
            LogUtils.v("camcorderProfile.videoFrameWidth: " + camcorderProfile.videoFrameWidth);
        }
        return json.toString();
    }

    /**
     * シャッター音を消せるか
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean canDisableShutterSound(CameraInfo info){
        boolean canDisableShutterSound = false;
        if(AndroidUtils.isMoreThanBuildVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)){
            canDisableShutterSound = info.canDisableShutterSound;
        }
        return canDisableShutterSound;
    }

    /**
     * 顔検出が使えるか
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean isFaceDetectionSupported(Camera camera){
        boolean isFaceDetectionSupported = false;
        if(AndroidUtils.isMoreThanBuildVersion(Build.VERSION_CODES.ICE_CREAM_SANDWICH)){
            int maxNumDetectedFaces = camera.getParameters().getMaxNumDetectedFaces();
            isFaceDetectionSupported = maxNumDetectedFaces > 0;
        }
        return isFaceDetectionSupported;
    }

}
