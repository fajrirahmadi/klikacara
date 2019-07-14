package co.id.klikacara.object.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.object.Banner;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class BannerAdapter extends AbstractItem<BannerAdapter, BannerAdapter.ViewHolder> {

    private Banner banner;
    private String imageUrl;

    public BannerAdapter(Banner banner) {
        this.banner = banner;
        this.imageUrl = banner.getUrl();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BannerAdapter(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int getType() {
        return R.id.banner_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_image;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bannerImageView)
        AppCompatImageView bannerImageView;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }

    @Override
    public void bindView(@NonNull ViewHolder holder,
                         @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);
        GlideUtils.setFotoWithUrl(holder.context, imageUrl, holder.bannerImageView);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.bannerImageView.setImageResource(0);
    }
}
