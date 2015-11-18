package com.soco.SoCoClient.common.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by David_WANG on 11/17/2015.
 */
public class IconAsyncDrawable extends BitmapDrawable {
        private final WeakReference<IconDownloadTask> IconDownloadTaskReference;

        public IconAsyncDrawable(Resources res, Bitmap bitmap,
                             IconDownloadTask iconDownloadTask) {
            super(res, bitmap);
            IconDownloadTaskReference =
                    new WeakReference<>(iconDownloadTask);
        }

        public IconDownloadTask getBitmapWorkerTask() {
            return IconDownloadTaskReference.get();
        }

}
