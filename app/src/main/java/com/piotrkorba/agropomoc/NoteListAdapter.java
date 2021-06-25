package com.piotrkorba.agropomoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter for RecyclerView that displays a list of notes.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {
    private final LayoutInflater mInflater;
    private List<Note> mNotes;
    private Context mContext;
    public static final String NOTE_ID = "com.piotrkorba.agropomoc.extra.NOTE_ID";
    public static final int NOTE_DETAIL_REQUEST = 2;


    NoteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.notes_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (mNotes != null) {
            Note current = mNotes.get(position);
            long currencyInteger = current.getCurrencyInteger();
            long currencyDecimal = current.getCurrencyDecimal();
            if (currencyInteger < 0 || currencyDecimal < 0) {
                holder.noteItemViewCurrency.setTextColor(0xffea3a44);
                holder.noteItemViewImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down_48));
            } else {
                holder.noteItemViewCurrency.setTextColor(0xff26cb8c);
                holder.noteItemViewImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_up_48));
            }
            String currencyStr = String.format("%d.%2d", currencyInteger, Math.abs(currencyDecimal)).replace(" ", "0");
            currencyStr = currencyStr + " zł";
            holder.noteItemViewCurrency.setText(currencyStr);
            holder.noteItemViewTitle.setText(current.getTitle());
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
            holder.noteItemViewDate.setText(fmt.format(current.getDate()));
        }
    }

    /**
     * Associate list of notes with this adapter.
     * @param notes
     */
    void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mNotes != null)
            return mNotes.size();
        else return 0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView noteItemViewTitle;
        private TextView noteItemViewDate;
        private TextView noteItemViewCurrency;
        private ImageView noteItemViewImage;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteItemViewTitle = itemView.findViewById(R.id.noteItemTitleTextView);
            noteItemViewDate = itemView.findViewById(R.id.noteItemDateTextView);
            noteItemViewCurrency = itemView.findViewById(R.id.noteItemCurrencyTextView);
            noteItemViewImage = itemView.findViewById(R.id.noteItemImageView);
            itemView.setOnClickListener(this);
        }

        // Start Single Product Activity when element is clicked.
        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            Note element = mNotes.get(mPosition);
            Intent intent = new Intent(v.getContext(), NoteDetailActivity.class);
            intent.putExtra(NOTE_ID, element.getId());
            intent.putExtra(NotesActivity.EXTRA_NOTE, element);
            ((Activity) mContext).startActivityForResult(intent, NOTE_DETAIL_REQUEST);
        }
    }
}
