package com.jlee.mobile.stream.ui.fragment;

import android.net.Uri;

public class FragmentFactory {
    public enum FragmentType {
        Viewer,
        Creator,
    }

    public static BaseFragment createNewFragment(FragmentType type)
            throws IllegalArgumentException {
        BaseFragment fragment;
        switch (type) {
            case Viewer:
                fragment = new ViewStreamFragment();
                break;
            case Creator:
                fragment = new NewStreamFragment();
                break;
            default:
                throw new IllegalArgumentException("invalid fragment type");
        }

//        Bundle args = new Bundle();
//        args.putString(Constants.ARGUMENT_URI, param1);
//        args.putString(Constants.ARGUMENT_ID, param2);
//        args.putInt(Constants.ARGUMENT_TYPE, id);
//        fragment.setArguments(args);

        return fragment;
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
