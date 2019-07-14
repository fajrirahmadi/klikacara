package co.id.klikacara.object.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.object.KlikMenu;
import co.id.klikacara.object.MasterData;
import co.id.klikacara.object.MitraType;
import co.id.klikacara.object.PaymentType;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class TextAdapter extends AbstractItem<TextAdapter, TextAdapter.ViewHolder> {

    private String value;
    private MitraType mitraType;
    private MasterData masterData;
    private KlikMenu klikMenu;
    private PaymentType paymentType;

    public TextAdapter(String value) {
        this.value = value;
    }

    public TextAdapter(MitraType mitraType) {
        this(mitraType.getDescription());
        this.mitraType = mitraType;
    }

    public TextAdapter(MasterData masterData) {
        this(masterData.getName());
        this.masterData = masterData;
    }

    public TextAdapter(KlikMenu klikMenu) {
        this(klikMenu.getName());
        this.klikMenu = klikMenu;
    }

    public TextAdapter(PaymentType paymentType) {
        this(paymentType.getDescription());
        this.paymentType = paymentType;
    }

    public MitraType getMitraType() {
        return mitraType;
    }

    public MasterData getMasterData() {
        return masterData;
    }

    public KlikMenu getKlikMenu() {
        return klikMenu;
    }

    public String getValue() {
        return value;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    @Override
    public int getType() {
        return R.id.text_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_text;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        holder.valueTextView.setText(value);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.valueTextView.setText(null);
    }
}
