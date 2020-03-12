package co.id.klikacara.object.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.items.AbstractItem;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.base.utils.stringhelper.StringHelper;
import co.id.klikacara.base.utils.viewhelper.ViewHelper;
import co.id.klikacara.object.BaseProduct;
import co.id.klikacara.object.builder.ProductBuilder;

public class ProductAdapter extends AbstractItem<ProductAdapter, ProductAdapter.ViewHolder> {

    private BaseProduct product;
    private boolean isChoosed = false;

    public ProductAdapter(BaseProduct product) {
        this.product = product;
    }

    public ProductAdapter(ProductBuilder product) {
        this.product = product.getBaseProduct();
        this.isChoosed = product.isChoosed();
    }

    public BaseProduct getProduct() {
        return product;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    @Override
    public int getType() {
        return R.id.product_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_product;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.productTitleTextView)
        AppCompatTextView productTitleTextView;
        @BindView(R.id.productPriceTextView)
        AppCompatTextView productPriceTextView;
        @BindView(R.id.productImageView)
        AppCompatImageView productImageView;
        @BindView(R.id.selectedImageView)
        AppCompatImageView selectedImageView;

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
        if (StringUtils.isNotBlank(product.getCover()))
            GlideUtils.setFotoWithUrl(holder.context, product.getCover(), holder.productImageView);
        else
            holder.productImageView.setImageResource(R.drawable.logo_klikacara);
        holder.productTitleTextView.setText(product.getName());
        holder.productPriceTextView.setText(StringHelper.getStringBuilderToString(
                StringHelper.getPriceInRp(product.getPrice()),
                "/",
                Objects.requireNonNull(product.getPaymentType()).getDescription()
        ));
        ViewHelper.Companion.handleVisibility(isChoosed, holder.selectedImageView);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.productTitleTextView.setText(null);
        holder.productPriceTextView.setText(null);
    }
}
