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
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.context.ContextManager;
import com.flybits.context.ReservedContextPlugin;
import com.flybits.context.plugins.FlybitsContextPlugin;
import com.flybits.samples.android.basics.R;
import com.flybits.samples.android.basics.contextdata.HeadphonesData;
import com.flybits.samples.android.basics.contextdata.HeadphonesService;
import com.flybits.samples.android.basics.fragments.adapters.ContextDataAdapter;
import com.flybits.samples.android.basics.interfaces.IContextDataChange;

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

        RecyclerView lstContextData = v.findViewById(R.id.list);
        lstContextData.setHasFixedSize(false);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        lstContextData.setLayoutManager(manager);

        contextData   = setData();
        ContextDataAdapter adapter = new ContextDataAdapter(getActivity(), contextData, this);
        lstContextData.setAdapter(adapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onContextChange(int position, boolean isOn) {

        String item = contextData.get(position);

        if (!item.equalsIgnoreCase("custom")) {

            /************************************************************************
             * Context Collection
             * Step 1 (AndroidManifest.xml)
             * Step 2 - Defining FlybitsContextPlugin to Start collecting
             ***********************************************************************/
            FlybitsContextPlugin plugin = new FlybitsContextPlugin.Builder(ReservedContextPlugin.fromKey(item))
                    .setRefreshTime(UPDATE_TIME, UPDATE_TIME, TimeUnit.MINUTES)
                    .build();

            if (isOn) {
                /************************************************************************
                 * Context Collection
                 * Step 3 - Starting the collection process
                 ***********************************************************************/
                ContextManager.start(getContext(), plugin);
            }else{
                /************************************************************************
                 * Context Collection
                 * Step 4 - Stopping the collection process
                 ***********************************************************************/
                ContextManager.stop(getContext(), plugin);
            }
        }else{

            FlybitsContextPlugin pluginHeadphones = new FlybitsContextPlugin.Builder(HeadphonesService.class)
                    .setRefreshTime(UPDATE_TIME, UPDATE_TIME, TimeUnit.MINUTES)
                    .build();

            if (isOn) {
                /************************************************************************
                 * Custom Context Collection
                 * Step 1 (HeadphonesData.java class)
                 * Step 2 (HeadphonesService.java class)
                 * Step 3 (AndroidManifest.xml)
                 * Step 4 - Defining GCM Task to start collecting
                 ***********************************************************************/
                ContextManager.start(getContext(), pluginHeadphones);
            }else{
                /************************************************************************
                 * Custom Context Collection
                 * Step 5 - Stopping the custom collection process
                 ***********************************************************************/
                ContextManager.stop(getContext(), pluginHeadphones);
            }
        }
    }

    private void manuallySendContext(){

        HeadphonesData data = new HeadphonesData(getContext());
        data.updateNow(getContext(), new BasicResultCallback() {
            @Override
            public void onSuccess() {
                //Successfully sent Context Value to server
            }

            @Override
            public void onException(FlybitsException exception) {
                //Something went wrong. Check the exception.
            }
        });
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
}
