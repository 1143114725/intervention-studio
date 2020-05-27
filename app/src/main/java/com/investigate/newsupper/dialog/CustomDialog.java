package com.investigate.newsupper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.investigate.newsupper.R;

public class CustomDialog extends Dialog {

    private Window mWindow;

    private View mRootView;

    public CustomDialog(Context context, int layoutId) {
        super(context, R.style.style_clear);
        mWindow = getWindow();

        mRootView = getLayoutInflater().inflate(layoutId, null);
        setContentView(mRootView);

        setGravity(Gravity.CENTER);

//        // 默认宽高
        WindowManager.LayoutParams lp = getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        setAttributes(lp);
    }

    protected WindowManager.LayoutParams getAttributes() {
        return mWindow.getAttributes();
    }

    protected void setAttributes(WindowManager.LayoutParams lp) {
        mWindow.setAttributes(lp);
    }

    public void setGravity(int gravity) {
        mWindow.setGravity(gravity);
    }

    public void setAnimations(int resId) {
        mWindow.setWindowAnimations(resId);
    }

    public void setViewOnClickListener(int viewId, View.OnClickListener l) {
        if (mRootView != null) {
            View view = mRootView.findViewById(viewId);
            if (view != null) {
                view.setOnClickListener(l);
            }
        }
    }

    public void setText(int viewId, int resId) {
        if (mRootView != null) {
            View view = mRootView.findViewById(viewId);
            if (view instanceof TextView) {
                ((TextView) view).setText(resId);
            }
        }
    }

    public View getView(int viewId) {
        View view = null;
        if (mRootView != null) {
            view = mRootView.findViewById(viewId);
            
        }
        return view;
    }

    public void setImageResource(int viewId, int resId) {
        if (mRootView != null) {
            View view = mRootView.findViewById(viewId);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(resId);
            }
        }
    }

    public void setTextSize(int viewId, int size) {
        if (mRootView != null) {
            View view = mRootView.findViewById(viewId);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(size);
            }
        }
    }

    public static class Builder {
        private CustomDialog mDialog;
        private Builder() {
        }

        public static Builder create(Context context, int layoutId) {
            Builder b = new Builder();
            b.mDialog = new CustomDialog(context, layoutId);
            return b;
        }

        public Builder setOnClickListener(int viewId, View.OnClickListener l) {
            if (mDialog == null) {
                return this;
            }
            mDialog.setViewOnClickListener(viewId, l);
            return this;
        }

        public Builder setText(int viewId, int resId) {
            if (mDialog == null) {
                return this;
            }
            mDialog.setText(viewId, resId);
            return this;
        }

        public Builder setTextSize(int viewId, int size) {
            if (mDialog == null) {
                return this;
            }
            mDialog.setTextSize(viewId, size);
            return this;
        }

        public Builder setImageResource(int viewId, int resId) {
            if (mDialog == null) {
                return this;
            }
            mDialog.setImageResource(viewId, resId);
            return this;
        }
       
        public Builder setCanceledOnTouchOutside(boolean cancel) {
            if (mDialog == null) {
                return this;
            }
            mDialog.setCanceledOnTouchOutside(cancel);
            return this;
        }

        public Builder setLayoutParams(int width, int height) {
            if (mDialog == null) {
                return this;
            }
            // 默认宽高
            WindowManager.LayoutParams lp = mDialog.getAttributes();
            lp.width = width;
            lp.height = height;
            mDialog.setAttributes(lp);
            return this;
        }

        public Builder setGravity(int gravity) {
            if (mDialog == null) {
                return this;
            }
            mDialog.setGravity(gravity);
            return this;
        }

        public Builder setAnimations(int resId) {
            if (mDialog == null) {
                return this;
            }
            mDialog.setAnimations(resId);
            return this;
        }

        public View getView(int viewId) {
            if (mDialog == null)
                return null;

            return mDialog.getView(viewId);
        }

        public CustomDialog show() {
            if (mDialog != null) {
                mDialog.show();
            }
            return mDialog;
        }

        public void dismiss() {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }
    }
}
