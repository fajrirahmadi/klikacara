package co.id.klikacara.object.adapter;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.object.Order;
import com.mikepenz.fastadapter.items.AbstractItem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class EventAdapter extends AbstractItem<EventAdapter, EventAdapter.ViewHolder> {

    private Order order;

    public EventAdapter(Order order) {
        this.order = order;
    }

    @Override
    public int getType() {
        return R.id.event_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_event;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menuImageView)
        AppCompatImageView menuImageView;
        @BindView(R.id.eventNameTextView)
        AppCompatTextView eventNameTextView;
        @BindView(R.id.eventPlaceTextView)
        AppCompatTextView eventPlaceTextView;

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
        if (StringUtils.isNotBlank(order.getPosterUrl()))
            GlideUtils.setFotoWithUrl(holder.context, order.getPosterUrl(), holder.menuImageView);
        holder.eventNameTextView.setText(order.getName());
        if (order.getProvince() != null)
            holder.eventPlaceTextView.setText(order.getProvince().getName());
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.menuImageView.setImageResource(0);
        holder.eventNameTextView.setText(null);
        holder.eventPlaceTextView.setText(null);
    }
}
