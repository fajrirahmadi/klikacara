package co.id.klikacara.object.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.base.utils.imagehelper.GlideUtils;
import co.id.klikacara.base.utils.stringhelper.StringHelper;
import co.id.klikacara.object.Bank;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class BankAdapter extends AbstractItem<BankAdapter, BankAdapter.ViewHolder> {

    private Bank bank;
    private boolean selected = false;

    public BankAdapter(Bank bank, Boolean selected) {
        this.bank = bank;
        this.selected = selected;
    }

    public Bank getBank() {
        return bank;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getType() {
        return R.id.bank_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_bank;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logoBankImageView)
        AppCompatImageView logoImageView;
        @BindView(R.id.bankTitleTextView)
        AppCompatTextView titleTextView;
        @BindView(R.id.bankDescriptionTextView)
        AppCompatTextView descriptionTextView;
        @BindView(R.id.areaBank)
        RelativeLayout areaBank;
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
        if (selected) {
            holder.areaBank.setBackground(holder.context.getResources().getDrawable(R.drawable.base_border_primary));
        } else {
            holder.areaBank.setBackgroundColor(holder.context.getResources().getColor(R.color.white));
        }
        GlideUtils.setFotoWithUrl(holder.context, bank.getUrl(), holder.logoImageView);
        holder.titleTextView.setText(bank.getBankName());
        holder.descriptionTextView.setText(StringHelper.getStringBuilderToString(bank.getNomorRekening(), " a/n ", bank.getNamaRekening()));
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.logoImageView.setImageResource(0);
        holder.titleTextView.setText(null);
        holder.descriptionTextView.setText(null);
    }
}
