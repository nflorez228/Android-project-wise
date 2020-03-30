package com.nicoft.bewise;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_ChooserHouse.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_ChooserHouse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ChooserHouse extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Name";
    private static final String ARG_PARAM2 = "Descr";
    private static final String ARG_PARAM3 = "URL";
    private static final String ARG_PARAM4 = "icon";
    private static final String ARG_PARAM5 = "EURL";
    // TODO: Rename and change types of parameters
    private String title;
    private String text;
    private String URL;
    private String EURL;
    private int icon;

    private Fragment_ChooserHouse.OnFragmentInteractionListener mListener;

    public Fragment_ChooserHouse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCH.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_ChooserHouse newInstance(String param1, String param2, String param3, int param4, String param5) {
        Fragment_ChooserHouse fragment = new Fragment_ChooserHouse();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putInt(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            text = getArguments().getString(ARG_PARAM2);
            URL = getArguments().getString(ARG_PARAM3);
            icon = getArguments().getInt(ARG_PARAM4);
            EURL = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chooserhouse,container,false);

        Button configurar = (Button) view.findViewById(R.id.ConfigureHouseButton);
        configurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PpalActivity.class);
                intent.putExtra("URL", URL);
                intent.putExtra("NAME", title);
                intent.putExtra("EURL", EURL);
                startActivity(intent);
            }
        });

        Button go = (Button) view.findViewById(R.id.GoHouseButton);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PpalActivity.class);
                intent.putExtra("URL", URL);
                intent.putExtra("NAME", title);
                startActivity(intent);
            }
        });

        TextView tvTitle = (TextView)view.findViewById(R.id.HouseTitle);
        tvTitle.setText(title.toUpperCase());
        TextView tvText = (TextView)view.findViewById(R.id.HouseText);
        tvText.setText(text);

        ImageView ivHouse = (ImageView)view.findViewById(R.id.house_photo);
        Drawable iconH = getResources().getDrawable(icon);
        ivHouse.setImageDrawable(iconH);

        ImageButton ibcameraHouse = (ImageButton)view.findViewById(R.id.cameraHouseButton);
        ibcameraHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Go to take picture",Toast.LENGTH_SHORT).show();
                takepic();
            }
        });

        ImageView ivHelpHouse = (ImageView)view.findViewById(R.id.HelpHouseButton);
        ivHelpHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Go to help",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private Uri mCapturedImageURI;
    public void takepic()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getActivity().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
            takePictureIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, 1);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Fragment_ChooserHouse.OnFragmentInteractionListener) {
            mListener = (Fragment_ChooserHouse.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
