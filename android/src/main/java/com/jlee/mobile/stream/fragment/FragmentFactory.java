package com.jlee.mobile.stream.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.jlee.mobile.stream.Constants;

public class FragmentFactory {
    public static Fragment createNewFragment(FragmentType type, String param1, String param2, int id) {
        NewStreamFragment fragment = new NewStreamFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARGUMENT_URI, param1);
        args.putString(Constants.ARGUMENT_ID, param2);
        args.putInt(Constants.ARGUMENT_TYPE, id);
        fragment.setArguments(args);

        return fragment;
    }

    public enum FragmentType {
        FragmentOne,
        FragmentTwo,
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
