package com.tangguo.tangguoxianjin.view.customcamera;

import android.hardware.Camera;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraUtils {




    @Nullable
    public static Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 比较两个int值的大小.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return lhs < rhs, -1; lhs = rhs, 0; lhs > rhs, 1;
     */
    public static int compare2(int lhs, int rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    /**
     * 判断是否是16：9的Size, 允许误差5%.
     *
     * @param size Size
     * @return 是否是16：9的Size
     */
    public static boolean isWide(Camera.Size size) {
        double ratio = ((double) size.width) / ((double) size.height);
        return ratio > 1.68 && ratio < 1.87;
    }

    /**
     * 从sizeArray中找到满足16:9比例，且不超过maxPicturePixels指定的像素数的最大Size.
     * 若找不到，则选择满足16:9比例的最大Size（像素数可能超过maxPicturePixels)，若仍找不到，返回最大Size。
     *
     * @param forTakingPicture 寻找拍照尺寸，而不是预览尺寸。
     *                         为true时，尺寸受到maxPicturePixels的限制；
     *                         false时，尺寸不超过1920x1080，否则相机带宽吃紧，这也是Camera2 API的要求.
     * @param sizeList         Camera.Parameters.getSupportedPreviewSizes()
     *                         或Camera.Parameters.getSupportedPictureSizes()返回的sizeList
     * @param maxPicturePixels 最大可接受的照片像素数
     */
    public static Camera.Size findBestSize(boolean forTakingPicture, List<Camera.Size> sizeList, long maxPicturePixels) {
        //满足16:9，但超过maxAcceptedPixels的过大Size
        List<Camera.Size> tooLargeSizes = new ArrayList<>();

        Collections.sort(sizeList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                return -compare2(lhs.width * lhs.height, rhs.width * rhs.height);
            }
        });

        for (Camera.Size size : sizeList) {
            //非16:9的尺寸无视
            if (!isWide(size)) continue;
            boolean notTooLarge;
            if (forTakingPicture) {
                //若是为了拍摄照片，则尺寸不要超过指定的maxPicturePixels.
                notTooLarge = ((long) size.width) * ((long) size.height) <= maxPicturePixels;
            } else {
                //若只是为了预览，则尺寸不要超过1920x1080，否则相机带宽吃紧，这也是Camera2 API的要求.
                notTooLarge = ((long) size.width) * ((long) size.height) <= 1920 * 1080;
            }
            if (!notTooLarge) {
                tooLargeSizes.add(size);
                continue;
            }
            return size;
        }
        if (tooLargeSizes.size() > 0) {
            return tooLargeSizes.get(0);
        } else {
            return sizeList.get(0);
        }


    }

    /**
     * 在FOCUS_MODE_AUTO模式下使用，触发一次自动对焦.
     *
     * @param camera Camera
     */
    public static void autoFocus(Camera camera) {
        try {
            camera.autoFocus(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
