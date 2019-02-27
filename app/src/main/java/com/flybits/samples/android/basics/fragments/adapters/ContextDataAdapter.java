package com.flybits.samples.android.basics.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.flybits.samples.android.basics.R;
import com.flybits.samples.android.basics.interfaces.IContextDataChange;

import java.util.ArrayList;

public class ContextDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DEFAULT    = 200;

    private ArrayList<String> items;
    private LayoutInflater mInflater;
    private IContextDataChange callback;

    public ContextDataAdapter(Context context, ArrayList<String> contextData, IContextDataChange callback) {
        mInflater = LayoutInflater.from(context);
        items = contextData;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        final View view = mInflater.inflate(R.layout.item_context_data, viewGroup, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderParent, int position) {

        if(viewHolderParent instanceof ViewHolderItem){
            String item = items.get(position);
            ViewHolderItem viewHolder = (ViewHolderItem) viewHolderParent;

            viewHolder.txtTitle.setText(item);
            viewHolder.btnStart.setTag(position);
            viewHolder.btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onContextChange((int) view.getTag(), true);
                }
            });

            viewHolder.btnStop.setTag(position);
            viewHolder.btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onContextChange((int) view.getTag(), false);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return DEFAULT;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView txtTitle;
        Button btnStart;
        Button btnStop;

        public ViewHolderItem(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.item_context_title);
            btnStart = view.findViewById(R.id.item_context_start);
            btnStop = view.findViewById(R.id.item_context_stop);
        }
    }
}
