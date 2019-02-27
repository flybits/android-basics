package com.flybits.samples.android.basics.contextdata;

import android.os.Bundle;
import android.util.Log;
import com.flybits.context.models.ContextData;
import com.flybits.context.services.FlybitsContextPluginService;

/************************************************************************
 * Custom Context Collection
 * Step 2a - Create a Service class that extends FlybitsContextPluginService
 ***********************************************************************/
public class HeadphonesService extends FlybitsContextPluginService {

    private final static String _TAG = "PluginHeadphones";

    @Override
    public ContextData getData() {
        /************************************************************************
         * Custom Context Collection
         * Step 2b - Implement logic for getting Context Values
         ***********************************************************************/
        Log.d(_TAG, "Getting HeadphonePluggedInService Data");
        return new HeadphonesData(getContext());
    }

    @Override
    public String[] getRequiredPermissions() {
        return new String[0];
    }

    @Override
    public void initialize(Bundle bundle) {
        Log.d(_TAG, "Activated: HeadphonePluggedInService");
    }

    @Override
    public boolean isSupported() {
        return true;
    }

}
