package com.flybits.samples.android.basics.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.flybits.android.push.models.Push;
import com.flybits.samples.android.basics.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DEFAULT    = 200;

    private ArrayList<Push> notifications;
    private LayoutInflater mInflater;
    private DateFormat dateFormatter;

    public NotificationAdapter(Context context, ArrayList<Push> pushNotifications) {
        mInflater       = LayoutInflater.from(context);
        notifications   = pushNotifications;
        dateFormatter   = new SimpleDateFormat("EEE MMM FF yyyy' at 'h:mm aaa", Locale.CANADA);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        final View view = mInflater.inflate(R.layout.item_notification, viewGroup, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderParent, int position) {

        if(viewHolderParent instanceof ViewHolderItem){
            Push notification = notifications.get(position);
            ViewHolderItem viewHolder = (ViewHolderItem) viewHolderParent;

            viewHolder.txtTitle.setText(notification.getTitle());
            viewHolder.txtMessage.setText(notification.getMessage());
            viewHolder.txtDate.setText(getPrettyTime(notification.getTimestamp()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return DEFAULT;
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtMessage;
        TextView txtDate;

        public ViewHolderItem(View view) {
            super(view);
            txtTitle    = view.findViewById(R.id.item_notification_title);
            txtMessage  = view.findViewById(R.id.item_notification_message);
            txtDate     = view.findViewById(R.id.item_notification_time);
        }
    }

    private String getPrettyTime(long receivedTime){

        long currentTime    = System.currentTimeMillis() / 1000;
        long subtractedTime = currentTime - receivedTime;

        if (subtractedTime<60){
            return "less than 1 minute ago";
        }else if (subtractedTime<3600){

            String minuteText = (subtractedTime/60 == 1) ? "minute" : "minutes";
            return (subtractedTime/60) + " " + minuteText + " ago";

        }else if (subtractedTime<86400){

            String hourText = (subtractedTime/3600 == 1) ? "hour" : "hours";
            return (subtractedTime/3600) + " " + hourText + " ago";

        }else if (subtractedTime<604800){

            String daysText = (subtractedTime/86400 == 1) ? "day" : "days";
            return (subtractedTime/86400) + " " + daysText + " ago";
        }

        return dateFormatter.format(new Date(receivedTime*1000));
    }

}
