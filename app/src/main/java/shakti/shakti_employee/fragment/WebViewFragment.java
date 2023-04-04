package shakti.shakti_employee.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.SapUrl;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    ConnectivityManager connectivityManager;
    boolean connected = false;
    WebView browser;
    private OnFragmentInteractionListener mListener;
    private DashboardActivity dashboardActivity;
    private LoggedInUser userModel;
    private Context mContext;


    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setDashBoard(DashboardActivity dashBoard) {
        this.dashboardActivity = dashBoard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext = getActivity();
        userModel = new LoggedInUser(mContext);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


//        getActivity().setTitle("Pending Tasks "+"("+ ""+dataHelper.getPendinTaskCount() +")");

        View v = inflater.inflate(R.layout.fragment_webview, container, false);


        if (isOnline()) {
//            callWebPage();

            browser = (WebView) v.findViewById(R.id.webView_portal);
            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setGeolocationEnabled(true);
            browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            browser.getSettings().setBuiltInZoomControls(true);
            browser.getSettings().setDomStorageEnabled(true);

            browser.setWebChromeClient(new WebChromeClient() {
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    callback.invoke(origin, true, false);
                }


            });

//        browser.loadUrl("http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_web/dashboard.htm");
//        browser.loadUrl("https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zhr_emp_app_web/dashboard.htm");
            browser.loadUrl(SapUrl.WEB_VIEW + "?id=" + userModel.uid);


            browser.setWebViewClient(new WebViewClientDemo());
            browser.setVerticalScrollBarEnabled(true);
            browser.setHorizontalScrollBarEnabled(true);

            browser.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {


                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return v;
    }


    public boolean isOnline() {
        try {


            connectivityManager = (ConnectivityManager) getActivity().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            try {
                Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                connected = (returnVal == 0);
                return connected;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;


        } catch (Exception e) {
            Log.v("connectivity", e.toString());
        }
        return connected;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void callWebPage() {

//        browser = (WebView) .findViewById(R.id.webView_portal);
//        browser.getSettings().setJavaScriptEnabled(true);
//        browser.getSettings().setGeolocationEnabled(true);
//        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        browser.getSettings().setBuiltInZoomControls(true);
//        browser.getSettings().setDomStorageEnabled(true);
//
//        browser.setWebChromeClient(new WebChromeClient() {
//            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                callback.invoke(origin, true, false);
//            }
//
//
//
//        });
//
////        browser.loadUrl("https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zservice_centre/video_4.htm");
//        browser.loadUrl(SapUrl.WEB_VIEW);
//
//
//
//        browser.setWebViewClient(new WebViewClientDemo(  ));
//        browser.setVerticalScrollBarEnabled(true);
//        browser.setHorizontalScrollBarEnabled(true);
//
//        browser.setDownloadListener(new DownloadListener() {
//            public void onDownloadStart(String url, String userAgent,
//                                        String contentDisposition, String mimetype,
//                                        long contentLength) {
//
//
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//
//            }
//        });


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class WebViewClientDemo extends WebViewClient {


//        @Override
//        public void onPageFinished(WebView view, String url)
//        {
////            // TODO Auto-generated method stub
////            super.onPageFinished(view, url);
//
//
//            if (progressBar.isShowing()) {
//                progressBar.dismiss();
//            }
//
//        }

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            loadError();
        }

        private void loadError() {
            String html = "<html><body><table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
                    + "<tr>"
                    + "<td><div align=\"center\"><font color=\"#8b8b8c\" size=\"4pt\">Your device don't have active internet connection</font></div></td>"
                    + "</tr>" + "</table><html><body>";
            System.out.println("html " + html);

            String base64 = android.util.Base64.encodeToString(html.getBytes(),
                    android.util.Base64.DEFAULT);
            browser.loadData(base64, "text/html; charset=utf-8", "base64");
            System.out.println("loaded html");
        }

    }


}
