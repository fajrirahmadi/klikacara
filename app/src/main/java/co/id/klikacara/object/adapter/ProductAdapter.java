package co.id.klikacara.object.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.base.utils.stringhelper.StringHelper;
import co.id.klikacara.object.BaseProduct;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductAdapter extends AbstractItem<ProductAdapter, ProductAdapter.ViewHolder> {

    private BaseProduct product;

    public ProductAdapter(BaseProduct product) {
        this.product = product;
    }

    public BaseProduct getProduct() {
        return product;
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
        if (product.getUrl().keySet().size() > 0) {
            List<String> listProductPicture = new ArrayList<>(product.getUrl().keySet());
            GlideUtils.setFotoWithUrl(holder.context, listProductPicture.get(0), holder.productImageView);
        } else
            holder.productImageView.setImageResource(R.drawable.logo_klikacara);
        holder.productTitleTextView.setText(product.getName());
        holder.productPriceTextView.setText(StringHelper.getStringBuilderToString(
                StringHelper.getPriceInRp(product.getPrice()),
                "/",
                Objects.requireNonNull(product.getPaymentType()).getDescription()
        ));
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.productTitleTextView.setText(null);
        holder.productPriceTextView.setText(null);
    }
}
