package co.id.klikacara.object.adapter;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.id.klikacara.R;
import co.id.klikacara.object.Participant;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class ParticipantAdapter extends AbstractItem<ParticipantAdapter, ParticipantAdapter.ViewHolder> {

    private Participant participant;

    public ParticipantAdapter(Participant participant) {
        this.participant = participant;
    }

    public Participant getParticipant() {
        return participant;
    }

    @Override
    public int getType() {
        return R.id.participant_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_user;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameTextView)
        AppCompatTextView nameTextView;
        @BindView(R.id.emailTextView)
        AppCompatTextView emailTextView;
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
        holder.nameTextView.setText(participant.getName());
        holder.emailTextView.setText(participant.getEmail());
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
        holder.nameTextView.setText(null);
        holder.emailTextView.setText(null);
    }
}
