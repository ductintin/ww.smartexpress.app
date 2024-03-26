package ww.smartexpress.app.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import ww.smartexpress.app.BuildConfig;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.Category;
import ww.smartexpress.app.ui.fragment.home.adapter.CategoryAdapter;

public final class BindingUtils {
    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url) {
        if(url != null && !TextUtils.isEmpty(url)){
            Glide.with(view.getContext())
                    .load(BuildConfig.MEDIA_URL+ "/v1/file/download" + url)
                    .error(R.drawable.car)
                    //.placeholder(R.drawable.car)
                    .into(view);
        }
    }

    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, int url) {
        view.setImageResource(url);
    }
}
