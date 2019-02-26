package com.flybits.samples.android.basics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flybits.android.push.models.Push;
import com.flybits.android.push.models.results.PushResult;
import com.flybits.android.push.utils.PushQueryParameters;
import com.flybits.commons.library.api.results.callbacks.PagedResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.samples.android.basics.R;
import com.flybits.samples.android.basics.fragments.adapters.NotificationAdapter;

import java.util.ArrayList;

public class PushHistoryFragment extends Fragment {

    private static final int NUMBER_OF_ITEMS_TO_DISPLAY = 10;

    private SwipeRefreshLayout mySwipeRefreshLayout;
    private ArrayList<Push> pushNotifications;
    private NotificationAdapter adapter;
    private PushResult result;

    public static Fragment newInstance(){
        PushHistoryFragment frag = new PushHistoryFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_push_history, container, false);

        RecyclerView lstNotifications = v.findViewById(R.id.fragment_list_lstList);
        mySwipeRefreshLayout    = v.findViewById(R.id.layoutRefresh);
        lstNotifications.setHasFixedSize(false);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        lstNotifications.setLayoutManager(manager);

        pushNotifications   = new ArrayList<>();
        adapter = new NotificationAdapter(getActivity(), pushNotifications);
        lstNotifications.setAdapter(adapter);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getNotifications();
                    }
                }
        );
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNotifications();
    }

    private void getNotifications(){
        PushQueryParameters parameters = new PushQueryParameters.Builder()
                .setPaging(NUMBER_OF_ITEMS_TO_DISPLAY, 0)
                .setCaching(10)
                .build();

        result = Push.get(getActivity(), parameters, new PagedResultCallback<Push>() {
            @Override
            public void onSuccess(ArrayList<Push> items) {
                loadPush(items);
            }

            @Override
            public void onException(FlybitsException exception) {
                //SOMETHING WENT WRONG
            }

            @Override
            public void onLoadedAllItems() { }
        });
    }

    private void loadPush(ArrayList<Push> items){
        if (!isDetached()) {
            setNotifications(items);
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setNotifications(ArrayList<Push> pushes){
        if (pushes.size() > 0 ){
            pushNotifications.clear();
            pushNotifications.addAll(pushes);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
