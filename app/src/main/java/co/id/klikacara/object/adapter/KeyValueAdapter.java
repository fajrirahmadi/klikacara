package co.id.klikacara.object.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class KeyValueAdapter extends AbstractItem<KeyValueAdapter, KeyValueAdapter.ViewHolder> {

    private String key;
    private String value;

    public KeyValueAdapter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int getType() {
        return R.id.key_value_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_key_value;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.keyTextView)
        AppCompatTextView keyTextView;
        @BindView(R.id.valueTextView)
        AppCompatTextView valueTextView;
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
        holder.keyTextView.setText(key);
        holder.valueTextView.setText(value);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.keyTextView.setText(null);
        holder.valueTextView.setText(null);
    }
}
