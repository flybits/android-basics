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
import com.flybits.android.kernel.KernelScope;
import com.flybits.commons.library.api.FlybitsManager;
import com.flybits.commons.library.api.idps.AnonymousIDP;
import com.flybits.commons.library.api.idps.FlybitsIDP;
import com.flybits.commons.library.api.idps.IDP;
import com.flybits.commons.library.api.idps.SignedIDP;
import com.flybits.commons.library.api.results.callbacks.BasicResultCallback;
import com.flybits.commons.library.api.results.callbacks.ConnectionResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.samples.android.basics.MainActivity;
import com.flybits.samples.android.basics.R;
import com.flybits.samples.android.basics.interfaces.IConnection;

public class ConnectionFragment extends Fragment {

    private FlybitsManager manager;
    private static final String PROJECT_ID = "";

    private IConnection iConnection;
    private Button btnConnect, btnDisconnect;

    public static Fragment newInstance(){
        ConnectionFragment frag = new ConnectionFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Indicates that this is in debug mode ... logs will be recorded in the logcat
        FlybitsManager.setDebug();

        /************************************************************************
         * SETUP: Step 1 - Create a FlybitsManager with the various scopes
         ***********************************************************************/
        manager = new FlybitsManager.Builder(getContext())
                .setProjectId(PROJECT_ID)
                //Add Kernel Scope which is responsible for retrieving Content.
                .addScope(KernelScope.SCOPE)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_connect, container, false);

        btnConnect = v.findViewById(R.id.btnConnect);
        btnDisconnect = v.findViewById(R.id.btnDisconnect);
        Button btnIsConnected = v.findViewById(R.id.btnIsConnected);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /************************************************************************
                 * SETUP: Step 2 - Create an IDP from one of the various different types of IDPs.
                 ***********************************************************************/
                IDP idp;

                //Indicates that this connection is associated to a specific email. All data will be saved to a specific email.
                idp = new FlybitsIDP("SOME EMAIL", "SOME PASSWORD");

                //Indicates that this connection is associated to user based on their AccessToken which is essentially a signed user id.
                idp = new SignedIDP("SOME ACCESS TOKEN", "SOME SIGNATURE");

                //Indicates that this connection is to an anonymous user. All data is wiped when the app is deleted or the user changes devices.
                idp = new AnonymousIDP();

                /************************************************************************
                 * SETUP: Step 3 - Call connect with the newly created IDP
                 ***********************************************************************/
                manager.connect(idp, new ConnectionResultCallback() {
                    @Override
                    public void onConnected() {
                        connected();
                    }

                    @Override
                    public void notConnected() {}

                    @Override
                    public void onException(FlybitsException exception) {}
                });
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /************************************************************************
                 * SETUP: Step 4 - Call disconnect to sever the connection from the mobile device to Flybits
                 ***********************************************************************/
                manager.disconnect(new BasicResultCallback() {
                    @Override
                    public void onSuccess() {
                        disconnected();
                    }

                    @Override
                    public void onException(FlybitsException exception) { }
                });
            }
        });

        btnIsConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /************************************************************************
                 * SETUP: Optional Step - Check if the user is currently connected to Flybits
                 ***********************************************************************/
                FlybitsManager.isConnected(getContext(), true, new ConnectionResultCallback() {
                    @Override
                    public void onConnected() {
                        connected();
                    }

                    @Override
                    public void notConnected() {
                        disconnected();
                    }

                    @Override
                    public void onException(FlybitsException exception) { }
                });
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iConnection = (MainActivity) context;
    }

    private void connected(){
        btnConnect.setEnabled(false);
        btnDisconnect.setEnabled(true);
        iConnection.connected();
    }
    private void disconnected(){
        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
        iConnection.connected();
    }
}
