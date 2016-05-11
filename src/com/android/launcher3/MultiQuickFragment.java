package com.android.launcher3;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.CardView;

import com.android.launcher3.R;
import com.android.launcher3.Utilities;

public class MultiQuickFragment extends Fragment {

    private boolean isActivityCreated = false;

    private CardView mIdCardView;
    private ImageView mIconImageView;
    private TextView mIdTextView;
    private TextView mNameTextView;
    private TextView mVipTextView;

    private UserManager mUserManager;

    private BroadcastReceiver mUserChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_USER_REMOVED)
                    || intent.getAction().equals(Intent.ACTION_USER_INFO_CHANGED)) {
                loadUserIcon();
                loadUniqueID();
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isActivityCreated) {
            if (isVisibleToUser) {
                // onPageStart();
            } else {
                // onPageEnd();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getActivity();
        mUserManager = (UserManager) context.getSystemService(Context.USER_SERVICE);

        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_REMOVED);
        filter.addAction(Intent.ACTION_USER_INFO_CHANGED);
        context.registerReceiver(mUserChangeReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiquick_fragment, container, false);
        mIdCardView = (CardView) view.findViewById(R.id.id_cardview);
        mIconImageView = (ImageView) view.findViewById(R.id.icon);
        mIdTextView = (TextView) view.findViewById(R.id.id);
        mNameTextView = (TextView) view.findViewById(R.id.name);
        mVipTextView = (TextView) view.findViewById(R.id.vip);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPlace();
        setTextTTF();
        loadUserIcon();
        loadUniqueID();
        loadUserInfo();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isActivityCreated = true;
    }

    private void initViewPlace() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mIdCardView.getLayoutParams();
        params.topMargin = Utilities.getStatusBarHeight(getContext());
    }

    private void setTextTTF() {
        Typeface mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/486.ttf");
        mIdTextView.setTypeface(mTypeface);
    }

    private void loadUserIcon() {
        int myUserId = UserHandle.myUserId();
        Bitmap b = mUserManager.getUserIcon(myUserId);
        if (b != null) mIconImageView.setImageDrawable(Utilities.encircle(b, getContext()));
    }

    private void loadUniqueID() {
        mIdTextView.setText(Build.getUniqueID(getContext()));
    }

    private void loadUserInfo() {
        mNameTextView.setText("Bacon");
        mVipTextView.setText("Ordinary users");
    }

}
