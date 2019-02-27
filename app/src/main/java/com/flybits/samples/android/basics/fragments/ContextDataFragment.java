package com.flybits.samples.android.basics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flybits.commons.library.logging.Logger;
import com.flybits.context.ContextManager;
import com.flybits.context.ReservedContextPlugin;
import com.flybits.context.plugins.FlybitsContextPlugin;
import com.flybits.samples.android.basics.R;
import com.flybits.samples.android.basics.contextdata.HeadphonesService;
import com.flybits.samples.android.basics.fragments.adapters.ContextDataAdapter;
import com.flybits.samples.android.basics.interfaces.IContextDataChange;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ContextDataFragment extends Fragment implements IContextDataChange {

    private static final long UPDATE_TIME = 1;
    private ArrayList<String> contextData;

    public static Fragment newInstance(){
        ContextDataFragment frag = new ContextDataFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_list_without_swipe, container, false);

        RecyclerView lstContextData = v.findViewById(R.id.fragment_list_lst_single);
        lstContextData.setHasFixedSize(false);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        lstContextData.setLayoutManager(manager);

        contextData   = setData();
        ContextDataAdapter adapter = new ContextDataAdapter(getActivity(), contextData, this);
        lstContextData.setAdapter(adapter);

        return v;
    }

    private ArrayList<String> setData(){
        ArrayList<String> items = new ArrayList<>();

        items.add(ReservedContextPlugin.ACTIVITY.getKey());
        items.add(ReservedContextPlugin.BATTERY.getKey());
        items.add(ReservedContextPlugin.CARRIER.getKey());
        items.add(ReservedContextPlugin.LANGUAGE.getKey());
        items.add(ReservedContextPlugin.NETWORK_CONNECTIVITY.getKey());
        items.add("custom");

        return items;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onContextChange(int position, boolean isOn) {

        String item = contextData.get(position);

        if (!item.equalsIgnoreCase("custom")) {

            FlybitsContextPlugin plugin = new FlybitsContextPlugin.Builder(ReservedContextPlugin.fromKey(item))
                    .setRefreshTime(UPDATE_TIME, UPDATE_TIME, TimeUnit.MINUTES)
                    .build();

            if (isOn) {
                ContextManager.start(getContext(), plugin);
            }else{
                ContextManager.stop(getContext(), plugin);
            }
        }else{
            if (isOn) {
                GcmNetworkManager mGcmNetworkManager = GcmNetworkManager.getInstance(getActivity());
                PeriodicTask.Builder task = new PeriodicTask.Builder()
                        .setTag(HeadphonesService.class.getSimpleName())
                        .setPeriod(TimeUnit.MINUTES.toSeconds(UPDATE_TIME))
                        .setFlex(TimeUnit.MINUTES.toSeconds(UPDATE_TIME))
                        .setUpdateCurrent(true)
                        .setPersisted(true)
                        .setRequiredNetwork(PeriodicTask.NETWORK_STATE_ANY)
                        .setService(HeadphonesService.class);
                mGcmNetworkManager.schedule(task.build());
            }else{
                GcmNetworkManager mGcmNetworkManager = GcmNetworkManager.getInstance(getActivity());
                try {
                    mGcmNetworkManager.cancelTask(HeadphonesService.class.getSimpleName(), HeadphonesService.class);
                } catch (IllegalArgumentException ex) {
                    Logger.exception("FlybitsContextPlugin.stop", ex);
                }
            }
        }
    }
}
