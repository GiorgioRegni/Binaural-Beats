package com.ihunda.android.binauralbeat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialFragment extends Fragment {
    TextView tvTutorialTitle,tvTutorialDesc;
    ImageView ivTutorial;
    public static TutorialFragment tutorialFragment;
    int position;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_tutorial_pager,null);
        initialization(view);
        return view;
    }

    public void initialization(View view)
    {
        tvTutorialTitle = (TextView)view.findViewById(R.id.text_row_tutorial_title);
        tvTutorialDesc = (TextView)view.findViewById(R.id.text_row_tutorial_description);
        ivTutorial = (ImageView)view.findViewById(R.id.image_row_tutorial_pager);

        Bundle b = getArguments();
        position = b.getInt("FRAGMENTPOSITION", 0);

        switch (position)
        {
            case 0:
                tvTutorialTitle.setText(getResources().getString(R.string.welcome));
                tvTutorialDesc.setText(getResources().getString(R.string.tutorial_1_text));
                ivTutorial.setImageResource(R.drawable.tutorial_1);
                break;
            case 1:
                tvTutorialTitle.setText(getResources().getString(R.string.tutorial_2_title));
                tvTutorialDesc.setText(getResources().getString(R.string.tutorial_2_text));
                ivTutorial.setImageResource(R.drawable.tutorial_2);
                break;
            case 2:
                tvTutorialTitle.setText(getResources().getString(R.string.tutorial_3_title));
                tvTutorialDesc.setText(getResources().getString(R.string.tutorial_3_text));
                ivTutorial.setImageResource(R.drawable.tutorial_2);
                break;
            case 3:
                tvTutorialTitle.setText(getResources().getString(R.string.tutorial_4_title));
                tvTutorialDesc.setText(getResources().getString(R.string.tutorial_4_text));
                ivTutorial.setImageResource(R.drawable.tutorial_2);
                break;
            case 4:
                tvTutorialTitle.setText(getResources().getString(R.string.tutorial_5_title));
                tvTutorialDesc.setText(getResources().getString(R.string.tutorial_5_text));
                ivTutorial.setImageResource(R.drawable.tutorial_2);
                break;
            case 5:
                tvTutorialTitle.setText(getResources().getString(R.string.tutorial_6_title));
                tvTutorialDesc.setText(getResources().getString(R.string.tutorial_6_text));
                ivTutorial.setImageResource(R.drawable.tutorial_1);
                break;
        }
    }

    public static TutorialFragment newInstance(int position) {
        tutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("FRAGMENTPOSITION", position);
        tutorialFragment.setArguments(bundle);
        return tutorialFragment;
    }

}
