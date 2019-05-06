package ki.pagetransformer.sharedelement.demo.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ki.pagetransformer.sharedelement.demo.MainActivity;
import ki.pagetransformer.sharedelement.demo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SmallPictureFragment extends Fragment {

    public ImageView smallCatImageView;
    public TextView smallCatLabel;

    public SmallPictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_small_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smallCatImageView = view.findViewById(R.id.smallPic_image_cat2);
        smallCatLabel = view.findViewById(R.id.smallPic_text_label2);

        MainActivity activity = (MainActivity) getActivity();


        smallCatImageView.setOnClickListener(v -> activity.viewPager.setCurrentItem(2));
    }
}
