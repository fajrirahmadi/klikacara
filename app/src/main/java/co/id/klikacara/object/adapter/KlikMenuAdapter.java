package co.id.klikacara.object.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.object.KlikMenu;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class KlikMenuAdapter extends AbstractItem<KlikMenuAdapter, KlikMenuAdapter.ViewHolder> {

    private KlikMenu klikMenu;

    public KlikMenuAdapter(KlikMenu klikMenu) {
        this.klikMenu = klikMenu;
    }

    public KlikMenu getKlikMenu() {
        return klikMenu;
    }

    @Override
    public int getType() {
        return R.id.klik_menu_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_klik_menu;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.menuImageView)
        AppCompatImageView menuImageView;

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
        GlideUtils.setFotoWithUrl(holder.context, klikMenu.getUrl(), holder.menuImageView);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.menuImageView.setImageResource(0);
    }
}
