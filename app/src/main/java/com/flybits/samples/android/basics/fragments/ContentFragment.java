package com.flybits.samples.android.basics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.flybits.android.kernel.ContentAnalytics;
import com.flybits.android.kernel.models.Content;
import com.flybits.android.kernel.utilities.ContentParameters;
import com.flybits.commons.library.api.results.callbacks.PagedResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.commons.library.models.internal.Pagination;
import com.flybits.samples.android.basics.R;
import com.flybits.samples.android.basics.contentdata.MenuOption;

import java.util.ArrayList;

public class ContentFragment extends Fragment {

    public static final String MENU_ITEM_TYPE = "menuitem";
    public static final String TAG = "_CONTENT_FRAGMENT";

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
                /************************************************************************
                 * SETUP: Step 1a - Get only the Content that is relevant to me.
                 ***********************************************************************/
                ContentParameters parameters = new ContentParameters.Builder()
                        .setPaging(10, 0)
                        .setTemplateType(MENU_ITEM_TYPE)
                        .build();

                Content.get(getContext(), parameters, new PagedResultCallback<Content>() {
                    @Override
                    public void onSuccess(ArrayList<Content> items, Pagination pagination) {
                        /************************************************************************
                         * SETUP: Step 2 - Look at com.flybits.samples.android.basics.contentdata.MenuOption class.
                         *        Step 3 - Parse Content into your Content Data class -
                         ***********************************************************************/
                        for (Content item : items){
                            //Make the item is of the type you want (Get this from your Content Template in the Dev Portal
                            if(item.getType().equals(MENU_ITEM_TYPE)){
                                try {
                                    MenuOption menuItem = item.getData(getContext(), MenuOption.class);
                                    Log.d(TAG, "Item Name: " + menuItem.name);
                                    //Do Something with your new Item - like add it to a RecyclerView
                                }catch (FlybitsException e){
                                    //You MenuOption does not form the same structure as the Content Template that you created in your Project. Please confirm you Content Data class.
                                }
                            }
                        }
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
                /************************************************************************
                 * SETUP: Step 1b - Get only the Content that is closest to me (this does not need to be in an experience)
                 ***********************************************************************/
                ContentParameters parameters = new ContentParameters.Builder()
                        .setPaging(10, 0)
                        .setLocation("address", 43.656, -79.656, 100)
                        .build();

                Content.get(getContext(), parameters, new PagedResultCallback<Content>() {
                    @Override
                    public void onSuccess(ArrayList<Content> items, Pagination pagination) {
                        /************************************************************************
                         * Once you retrieve data you can record analytics on that Content data like views and clicks
                         ***********************************************************************/
                        ContentAnalytics analytics = new ContentAnalytics(getContext());
                        for (Content item : items) {
                            analytics.trackViewed(item, System.currentTimeMillis());
                        }
                        //For clicks use trackEngaged(content, System.currentTimeMillis());
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
