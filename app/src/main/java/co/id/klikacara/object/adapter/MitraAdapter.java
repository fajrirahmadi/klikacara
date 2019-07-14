package co.id.klikacara.object.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.BuildConfig;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.base.utils.stringhelper.StringHelper;
import co.id.klikacara.object.authentication.User;
import com.mikepenz.fastadapter.items.AbstractItem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MitraAdapter extends AbstractItem<MitraAdapter, MitraAdapter.ViewHolder> {

    private User user;

    public MitraAdapter(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int getType() {
        return R.id.mitra_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_image_circle;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mitraImageView)
        AppCompatImageView mitraImageView;
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
        if (StringUtils.isNotBlank(user.getUrl()))
            GlideUtils.setFotoCircleFromStorage(holder.context,
                    StringHelper.getStringBuilderToString(BuildConfig.userDb, "/", user.getUrl()),
                    holder.mitraImageView);
        else
            holder.mitraImageView.setImageResource(R.drawable.logo_klikacara);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.mitraImageView.setImageResource(0);
    }
}
