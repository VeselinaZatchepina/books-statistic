package com.github.veselinazatchepina.bookstatistics.preference.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.veselinazatchepina.bookstatistics.MyApplication;
import com.github.veselinazatchepina.bookstatistics.R;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WriteToDeveloperFragment extends Fragment {

    @BindView(R.id.input_text_for_developer)
    EditText mEditTextForInput;
    @BindView(R.id.send_button)
    Button sendButton;
    private Unbinder unbinder;

    public WriteToDeveloperFragment() {
    }

    public static WriteToDeveloperFragment newInstance() {
        return new WriteToDeveloperFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_write_to_developer, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = mEditTextForInput.getText().toString();
                if (!inputText.equals("")) {
                    String mail = "mailto:veselinazatchepina@gmail.com" +
                            "?subject=" + Uri.encode("Feedback") +
                            "&body=" + Uri.encode(inputText);
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse(mail));
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
