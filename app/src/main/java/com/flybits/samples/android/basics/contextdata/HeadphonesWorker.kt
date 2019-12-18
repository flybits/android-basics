package com.flybits.samples.android.basics.contextdata

import android.content.Context
import android.os.Bundle
import androidx.work.WorkerParameters
import com.flybits.context.models.ContextData
import com.flybits.context.services.FlybitsContextPluginsWorker

/************************************************************************
 * Custom Context Collection
 * Step 2a - Create a Service class that extends FlybitsContextPluginsWorker
 ***********************************************************************/
class HeadphonesWorker(context: Context, workerParameters: WorkerParameters) :
    FlybitsContextPluginsWorker(context, workerParameters) {

    override fun getData(): ContextData {
        /************************************************************************
         * Custom Context Collection
         * Step 2b - Implement logic for getting Context Values
         ***********************************************************************/
        return HeadphonesData(applicationContext)
    }

    override fun getRequiredPermissions(): Array<String> {
        return arrayOf()
    }

    override fun initialize(bundle: Bundle?) {}

    override fun isSupported(): Boolean {
        return true
    }

}