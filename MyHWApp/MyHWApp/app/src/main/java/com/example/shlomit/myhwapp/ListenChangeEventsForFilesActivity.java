package com.example.shlomit.myhwapp;

/**
 * Created by shlomit on 12/03/2018.
 */
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.ListenerToken;
import com.google.android.gms.drive.events.OnChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;

/**
 * An activity that listens to change events on a user-picked file.
 */
public class ListenChangeEventsForFilesActivity extends BaseDemoActivity {
    private static final String TAG = "ListenChangeEvents";

    /*
     * Toggles file change event listening.
     */
    private Button mActionButton;

    /**
     * Displays the change event on the screen.
     */
    private TextView mLogTextView;

    /**
     * Represents the file picked by the user.
     */
    private DriveId mSelectedFileId;

    /**
     * Identifies our change listener so it can be unsubscribed.
     */
    private ListenerToken mChangeListenerToken;

    /**
     * Keeps the status whether change events are being listened to or not.
     */
    private boolean mIsSubscribed = false;

    /**
     * Timer to force periodic tickles of the watched file
     */
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeevents);

        mLogTextView = findViewById(R.id.textViewLog);
        mActionButton = findViewById(R.id.buttonAction);
        mActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }

    @Override
    protected void onDriveClientReady() {
        pickFolder()
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveId>() {
                            @Override
                            public void onSuccess(DriveId driveId) {
                                mSelectedFileId = driveId;
                                refresh();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "No file selected", e);
                        showMessage("file_not_selected");
                        finish();
                    }
                });
    }

    /**
     * Refreshes the status of UI elements. Enables/disables subscription button
     * depending on whether there is file picked by the user.
     */
    private void refresh() {
        if (mSelectedFileId == null) {
            mActionButton.setEnabled(false);
        } else {
            mActionButton.setEnabled(true);
        }

        if (!mIsSubscribed) {
            mActionButton.setText("button_subscribe");
        } else {
            mActionButton.setText("button_unsubscribe");
        }
    }

    /**
     * Toggles the subscription status. If there is no selected file, returns
     * immediately.
     */
    private void toggle() {
        if (mSelectedFileId == null) {
            return;
        }
        stopTimer();
        DriveFolder file = mSelectedFileId.asDriveFolder();


        Log.d("select",mSelectedFileId.toString());

        if (!mIsSubscribed) {
            Log.d(TAG, "Starting to listen to the file changes.");
            mIsSubscribed = true;
            mCountDownTimer = new TickleTimer(30000000 /* 300 min total */,
                    10000 /* tick every 10 second */);
            mCountDownTimer.start();
            // [START add_change_listener]
            getDriveResourceClient()
                    .addChangeListener(file, changeListener)
                    .addOnSuccessListener(this, new OnSuccessListener<ListenerToken>() {
                        @Override
                        public void onSuccess(ListenerToken listenerToken) {
                            mChangeListenerToken = listenerToken;
                        }
                    });
            // [END add_change_listener]
        } else {
            Log.d(TAG, "Stopping to listen to the file changes.");
            mIsSubscribed = false;
            // [START remove_change_listener]
            getDriveResourceClient().removeChangeListener(mChangeListenerToken);
            // [END remove_change_listener]
        }
        refresh();
    }

    private void stopTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    // [START change_listener]
    /**
     * A listener to handle file change events.
     */
    final private OnChangeListener changeListener = new OnChangeListener() {
        @Override
        public void onChange(ChangeEvent event) {
            //mLogTextView.append("change_event " + event +"\n" );
           if(event.hasBeenDeleted()){
               Log.d("hasBeenDeleted", event.getType()+"");
           }
            if(event.hasContentChanged()){
                Log.d("hasContentChanged", event.getType()+"");
            }
            if(event.hasMetadataChanged()){
                Log.d("hasMetadataChanged", event.getType()+"");
            }

            Log.d("change_event", event  +" \n");

        }
    };
    // [END change_listener]

    private class TickleTimer extends CountDownTimer {
        TickleTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            MetadataChangeSet metadata =
                    new MetadataChangeSet.Builder().setLastViewedByMeDate(new Date()).build();
            getDriveResourceClient()
                    .updateMetadata(mSelectedFileId.asDriveResource(), metadata)
                    .addOnSuccessListener(ListenChangeEventsForFilesActivity.this,
                            new OnSuccessListener<Metadata>() {
                                @Override
                                public void onSuccess(Metadata metadata) {
                                    Log.d("filename",metadata.getTitle());
                                    Log.d("getModifiedDate", metadata.getModifiedDate().toString());
                                    Log.d("getDescription", metadata.getDescription() + "");
                                    Log.d("getLastViewedByMeDate", metadata.getLastViewedByMeDate().toString());
                                    Log.d("getFileSize", metadata.getFileSize()+"");
                                    Log.d("isEditable", metadata.isEditable()+"");

                                }
                            })
                    .addOnFailureListener(
                            ListenChangeEventsForFilesActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Unable to update metadata", e);
                                }
                            });
        }

        @Override
        public void onFinish() {
            showMessage("tickle_finished");
        }
    }
}