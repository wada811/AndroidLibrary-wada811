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

public class GeoUtils {

    public static final double WGS84_A = 6378137.000;
    public static final double WGS84_E2 = 0.00669437999019758;
    public static final double WGS84_MNUM = 6335439.32729246;

    /**
     * ヒュベニの公式 d = √( (dy * m)^2 + (dx * n * cos(my))^2 ) から２地点間の距離を算出する
     * 
     * @param lat1 地点1の緯度
     * @param lng1 地点1の軽度
     * @param lat2 地点2の緯度
     * @param lng2 地点2の軽度
     * @see <a href="http://yamadarake.jp/trdi/report000001.html">二地点の緯度・経度からその距離を計算する（日本は山だらけ〜）</a>
     * @licenses MIT License
     */
    public static double calcDistance(double lat1, double lng1, double lat2, double lng2){
        double my = Math.toRadians((lat1 + lat2) / 2.0);
        double dy = Math.toRadians(lat1 - lat2);
        double dx = Math.toRadians(lng1 - lng2);

        double sin = Math.sin(my);
        double w = Math.sqrt(1.0 - WGS84_E2 * sin * sin);
        double m = WGS84_MNUM / (w * w * w);
        double n = WGS84_A / w;

        double dym = dy * m;
        double dxncos = dx * n * Math.cos(my);

        return Math.sqrt(dym * dym + dxncos * dxncos);
    }

    /**
     * ミリ秒形式から度分秒形式に変換する
     * 
     * ミリ秒→秒: /1000
     * 秒→分: /60
     * 分→度: /60
     * 
     * @param milliseconds ミリ秒形式の緯度・経度
     * @return 度分秒形式の緯度・経度
     */
    public static double toDegree(double milliseconds){
        return milliseconds / 3600000;
    }

    /**
     * 度分秒形式からミリ秒形式に変換する
     * 
     * 度→分: *60
     * 分→秒: *60
     * 秒→ミリ秒: *1000
     * 
     * @param degree 度分秒形式の緯度・経度
     * @return ミリ秒形式の緯度・経度
     */
    public static double toMillisecond(double degree){
        return degree * 3600000;
    }

}
