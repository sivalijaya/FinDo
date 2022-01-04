package com.example.findo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        RelativeLayout btn_check_order = view.findViewById(R.id.checkOrder);
        btn_check_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo change intent to check order
                Intent intent = new Intent(getActivity(), CheckOrderActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btn_home = view.findViewById(R.id.findo_logo);

        if (getActivity().getClass().toString().equals(HomeActivity.class.toString())) {
            btn_home.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.findo_logo_small));
        } else {
            btn_home.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_baseline_arrow_back_36));
        }

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getActivity().getClass().toString().equals(HomeActivity.class.toString())) {
                    getActivity().finish();
                }
            }
        });

        //listener when editText clicked search/enter
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                        intent.putExtra("searchValue", et_search.getText().toString());
                        et_search.setText("");
                        Log.d("intent", "onEditorAction: " + intent.getExtras().toString());
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {

                }
                return false;
            }
        });

    }
}