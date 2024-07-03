package ww.smartexpress.app.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import ww.smartexpress.app.BuildConfig;
import ww.smartexpress.app.R;

public final class BindingUtils {
    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url) {
        if (url == null){
            view.setImageResource(R.drawable.smartexpress_splash_logo);
            return;
        }
        if(url.contains("https:")){
            Glide.with(view.getContext())
                    .load(url)
                    .into(view);
            return;
        }
        Glide.with(view.getContext())
                .load(BuildConfig.MEDIA_URL+ "/v1/file/download" + url)
                .error(R.drawable.smartexpress_splash_logo)
                .placeholder(R.drawable.smartexpress_splash_logo)
                .into(view);
    }

    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, int url) {
        view.setImageResource(url);
    }
}
