package com.skitelDev.taskmanager.bottomDialogFragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skitelDev.taskmanager.R;

public class BottomDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {
    private ItemClickListener mListener;
    EditText newTaskText;
    EditText desc;
    Button addDescription;
    public static BottomDialogFragment newInstance() {
        return new BottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet, container,
                false);
        view.findViewById(R.id.savebutton).setOnClickListener(this);
        newTaskText = view.findViewById(R.id.newTaskTextField);
        desc = view.findViewById(R.id.desc);
        addDescription = view.findViewById(R.id.addDescription);
        addDescription.setOnClickListener(view1 -> desc.setVisibility(View.VISIBLE));
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        if (getActivity() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                Rect displayFrame = new Rect();
                decorView.getWindowVisibleDisplayFrame(displayFrame);
                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int heightDifference = height - displayFrame.bottom+70;
                if (heightDifference != 0) {
                    if (view.getPaddingBottom() != heightDifference) {
                        view.setPadding(0, 0, 0, heightDifference);
                    }
                } else {
                    if (view.getPaddingBottom() != 0) {
                        view.setPadding(0, 0, 0, 0);
                    }
                }
            });
        }
        getDialog().setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
            if (bottomSheetInternal == null) return;
            newTaskText.setText("");
            desc.setVisibility(View.GONE);
            BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ItemClickListener) context;

    }

    @Override
    public void onClick(View view) {
        mListener.onItemClick(newTaskText.getText().toString(), desc.getText().toString());
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(String newTaskText, String desc);

    }
}
