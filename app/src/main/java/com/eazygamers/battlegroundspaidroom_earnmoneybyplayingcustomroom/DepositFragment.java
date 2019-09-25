package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);
         Button paymentProcess = view.findViewById(R.id.paymentProcess);
         Button convert=view.findViewById(R.id.convert);
         convert.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getActivity(),ConvertActivity.class));
                 Objects.requireNonNull(getActivity()).finish();
             }
         });
        paymentProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(getActivity(), PaymentActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}
