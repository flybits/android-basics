package com.flybits.samples.android.basics.contextdata;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.flybits.context.models.ContextData;
import org.json.JSONException;
import org.json.JSONObject;

/************************************************************************
 * Custom Context Collection
 * Step 1a - Create a class that extends ContextData
 ***********************************************************************/
public class HeadphonesData extends ContextData implements Parcelable {

    private static final String YOUR_PROJECT_NAME = "LOOK IN CONTEXT PLUGINS TAB OF YOUR FLYBITS PROJECT FOR THIS VALUE";
    private boolean isConnected;

    public HeadphonesData(){}

    public HeadphonesData(Context context){
        super();
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        /************************************************************************
         * Custom Context Collection
         * Step 1b - Determine whether or not there is a headset connected to the device
         ***********************************************************************/
        if (am == null)
            isConnected = false;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            isConnected = am.isWiredHeadsetOn() || am.isBluetoothScoOn() || am.isBluetoothA2dpOn();
        } else {
            AudioDeviceInfo[] devices = am.getDevices(AudioManager.GET_DEVICES_OUTPUTS);

            for (int i = 0; i < devices.length; i++) {
                AudioDeviceInfo device = devices[i];

                if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET
                        || device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
                        || device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                    isConnected = true;
                }
            }
        }
        isConnected = false;
    }

    protected HeadphonesData(Parcel in){
        super();
        this.isConnected = in.readInt() == 1;
        setTime(in.readLong());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeadphonesData that = (HeadphonesData) o;
        return isConnected == that.isConnected;
    }

    @Override
    public void fromJson(String json) {
        try {
            JSONObject jsonObj  = new JSONObject(json);
            isConnected = jsonObj.getInt("isConnected") == 1;

        }catch (JSONException exception){}
    }

    @Override
    public String getPluginID() {
        return "ctx."+YOUR_PROJECT_NAME+".headset";
    }

    @Override
    public String toJson() {
        /************************************************************************
         * Custom Context Collection
         * Step 1c - Create JSON object to be sent to the server
         ***********************************************************************/

        JSONObject object = new JSONObject();
        try {
            object.put("isConnected", isConnected);
        }catch (JSONException exception){}

        return object.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isConnected ? 1 : 0);
        dest.writeLong(getTime());
    }

    public static final Creator<HeadphonesData> CREATOR = new Creator<HeadphonesData>() {
        public HeadphonesData createFromParcel(Parcel in) {
            return new HeadphonesData(in);
        }

        public HeadphonesData[] newArray(int size) {
            return new HeadphonesData[size];
        }
    };
}
