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
import co.id.klikacara.base.utils.viewhelper.ViewHelper;
import com.mikepenz.fastadapter.items.AbstractItem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ProductImageAdapter extends AbstractItem<ProductImageAdapter, ProductImageAdapter.ViewHolder> {

    private String path;
    private Boolean isFromRemote = false;
    private Boolean isLastIndex = false;

    public ProductImageAdapter(String path) {
        this.path = path;
    }

    public ProductImageAdapter(String path, Boolean isFromRemote) {
        this(path);
        this.isFromRemote = isFromRemote;
    }

    public ProductImageAdapter(String path, Boolean isFromRemote, Boolean isLastIndex) {
        this(path, isFromRemote);
        this.isLastIndex = isLastIndex;
    }

    public void setFromRemote(Boolean fromRemote) {
        isFromRemote = fromRemote;
    }

    public Boolean getFromRemote() {
        return isFromRemote;
    }

    public String getPath() {
        return path;
    }

    public void setLastIndex(Boolean lastIndex) {
        isLastIndex = lastIndex;
    }

    @Override
    public int getType() {
        return R.id.product_image_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_product_image;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.productImageView)
        AppCompatImageView productImageView;

        @BindView(R.id.deleteImageButton)
        AppCompatImageView deleteImageButton;

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
        if (StringUtils.isNotBlank(path))
            GlideUtils.setFotoWithUrl(holder.context, path, holder.productImageView);
        else
            holder.productImageView.setImageResource(R.drawable.ic_add_image);
        if (isLastIndex)
            ViewHelper.Companion.hideView(holder.deleteImageButton);
        else
            ViewHelper.Companion.showView(holder.deleteImageButton);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.productImageView.setImageResource(0);
    }
}
