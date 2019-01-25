package com.flybits.samples.android.basics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.flybits.android.kernel.models.Content;
import com.flybits.android.kernel.utilities.ContentParameters;
import com.flybits.commons.library.api.results.callbacks.PagedResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.samples.android.basics.R;

import java.util.ArrayList;

public class ContentFragment extends Fragment {

    public static Fragment newInstance(){
        ContentFragment frag = new ContentFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content, container, false);

        Button btnGetRelevant = v.findViewById(R.id.btnGetRelevant);
        Button btnGetContent = v.findViewById(R.id.btnGetContent);

        btnGetRelevant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentParameters parameters = new ContentParameters.Builder()
                        .setPaging(10, 0)
                        .build();

                Content.get(getContext(), parameters, new PagedResultCallback<Content>() {
                    @Override
                    public void onSuccess(ArrayList<Content> items) {

                    }

                    @Override
                    public void onException(FlybitsException exception) {

                    }

                    @Override
                    public void onLoadedAllItems() {

                    }
                });
            }
        });

        btnGetContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentParameters parameters = new ContentParameters.Builder()
                        .setPaging(10, 0)
                        .setLocation("address", 43.656, -79.656, 100)
                        .build();

                Content.get(getContext(), parameters, new PagedResultCallback<Content>() {
                    @Override
                    public void onSuccess(ArrayList<Content> items) {

                    }

                    @Override
                    public void onException(FlybitsException exception) {

                    }

                    @Override
                    public void onLoadedAllItems() {

                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
