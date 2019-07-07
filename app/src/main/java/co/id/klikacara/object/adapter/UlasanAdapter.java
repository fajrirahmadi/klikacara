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
import co.id.klikacara.object.Ulasan;
import com.mikepenz.fastadapter.items.AbstractItem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class UlasanAdapter extends AbstractItem<UlasanAdapter, UlasanAdapter.ViewHolder> {

    private Ulasan ulasan;

    public UlasanAdapter(Ulasan ulasan) {
        this.ulasan = ulasan;
    }

    @Override
    public int getType() {
        return R.id.ulasan_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_ulasan;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userImageView)
        AppCompatImageView userImageView;
        @BindView(R.id.nameTextView)
        AppCompatTextView nameTextView;
        @BindView(R.id.roleTextView)
        AppCompatTextView roleTextView;
        @BindView(R.id.ulasanTextView)
        AppCompatTextView ulasanTextView;
        @BindView(R.id.line)
        View line;

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
        if (StringUtils.isNotBlank(ulasan.getUrl()))
            GlideUtils.setFotoCircleFromStorage(holder.context, ulasan.getUrl(), holder.userImageView);
        else
            holder.userImageView.setImageResource(R.drawable.logo_klikacara);
        holder.nameTextView.setText(ulasan.getName());
        holder.roleTextView.setText(Objects.requireNonNull(ulasan.getRole()).getDescription());
        holder.ulasanTextView.setText(ulasan.getUlasan());
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.userImageView.setImageResource(0);
        holder.nameTextView.setText(null);
        holder.roleTextView.setText(null);
        holder.ulasanTextView.setText(null);
    }
}
