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
public class BigPictureFragment extends Fragment {

    public ImageView bigCatImageView;
    public TextView bigCatLabel;
    public BigPictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_big_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        super.onViewCreated(view, savedInstanceState);

        bigCatImageView = view.findViewById(R.id.bigPic_image_cat);
        bigCatLabel = view.findViewById(R.id.bigPic_text_label);

        bigCatImageView.setOnClickListener(v -> activity.viewPager.setCurrentItem(1));

    }
}
