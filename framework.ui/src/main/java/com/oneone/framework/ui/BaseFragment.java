package com.oneone.framework.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.LoadingDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {

    public final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private Unbinder unbinder;
    protected Dialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Class<?> clazz = getClass();
        final LayoutResource layout = clazz.getAnnotation(LayoutResource.class);
        if (layout != null) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(layout.value(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
            return rootView;
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logger.info("onAttach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        logger.info("onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        logger.info("onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        logger.info("onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        logger.info("onStop");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logger.info("onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        logger.info("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
        logger.info("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logger.info("onDetach");
    }

    public boolean isActivityExist() {
        return getActivity() != null && !getActivity().isFinishing();
    }

    public void showLoading(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog == null) {
                    mLoadingDialog = LoadingDialog.createLoadingDialog(getContext(), msg);
                }

                mLoadingDialog.setCanceledOnTouchOutside(false);
                if (!mLoadingDialog.isShowing()) {
                    mLoadingDialog.show();
                }

            }
        });
    }

    public void hideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

}
