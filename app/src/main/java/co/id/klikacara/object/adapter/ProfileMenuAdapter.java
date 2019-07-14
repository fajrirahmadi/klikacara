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

public class ProfileMenuAdapter extends AbstractItem<ProfileMenuAdapter, ProfileMenuAdapter.ViewHolder> {

    private int icon;
    private String label;
    private int code;

    public ProfileMenuAdapter(int icon, String label, int code) {
        this.icon = icon;
        this.label = label;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public int getType() {
        return R.id.profile_menu_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_profile_menu;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.labelMenuTextView)
        AppCompatTextView labelMenuTextView;
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
        holder.labelMenuTextView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        holder.labelMenuTextView.setText(label);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.labelMenuTextView.setText(null);
    }
}
