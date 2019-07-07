package co.id.klikacara.object.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.base.utils.stringhelper.StringHelper;
import co.id.klikacara.base.utils.timehelper.TimeUtils;
import co.id.klikacara.object.Order;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderListAdapter extends AbstractItem<OrderListAdapter, OrderListAdapter.ViewHolder> {

    private Order order;

    public OrderListAdapter(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public int getType() {
        return R.id.order_list_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_order;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderImageView)
        AppCompatImageView orderImageView;
        @BindView(R.id.orderItemNameTextView)
        AppCompatTextView orderItemNameTextView;
        @BindView(R.id.orderDateTextView)
        AppCompatTextView orderDateTextView;
        @BindView(R.id.orderStatusTextView)
        AppCompatTextView orderStatusTextView;
        @BindView(R.id.amountTextView)
        AppCompatTextView amountTextView;
        @BindView(R.id.detailButton)
        public AppCompatButton detailButton;

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
        if (Objects.requireNonNull(order.getProduct()).getUrl().keySet().size() > 0) {
            List<String> listProductPicture = new ArrayList<>(order.getProduct().getUrl().keySet());
            GlideUtils.setFotoWithUrl(holder.context, listProductPicture.get(0), holder.orderImageView);
        } else
            holder.orderImageView.setImageResource(R.drawable.logo_klikacara);
        holder.orderItemNameTextView.setText(order.getName());
        holder.orderStatusTextView.setText(Objects.requireNonNull(order.getPaymentStatus()).getDescription());
        holder.amountTextView.setText(StringHelper.getPriceInRp(order.getAmount()));
        if (order.getEndDate() != 0) {
            holder.orderDateTextView.setText(StringHelper.getStringBuilderToString(
                    TimeUtils.getDateFormated(TimeUtils.DATE_WITH_MONTH_FORMAT, order.getStartDate()),
                    " - ",
                    TimeUtils.getDateFormated(TimeUtils.DATE_WITH_MONTH_FORMAT, order.getEndDate())
            ));
        } else {
            holder.orderDateTextView.setText(
                    TimeUtils.getDateFormated(TimeUtils.DATE_WITH_MONTH_FORMAT, order.getStartDate())
            );
        }
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.orderImageView.setImageResource(0);
        holder.orderItemNameTextView.setText(null);
        holder.orderStatusTextView.setText(null);
        holder.amountTextView.setText(null);
        holder.orderDateTextView.setText(null);
    }
}
