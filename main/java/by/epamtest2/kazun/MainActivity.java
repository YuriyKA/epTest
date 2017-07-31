package by.epamtest2.kazun;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
//import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends ListActivity {

    private String[] descriptionArray;
    private String[] namesArray;
    private String[] urlsForImageArray;

    private String fileNameFI;
    private static String fileNameFIInf = "posts";
    private static String fileNameFIImg = "photos";

    // progress dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    // download from url
    private static final String file_urlInfo  = "https://jsonplaceholder.typicode.com/posts";
    private static final String file_urlImage = "https://jsonplaceholder.typicode.com/photos";

    private ArrayAdapter<String> mAdapter;

    private int length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //int i = initArray();
        //Toast.makeText(this, i, Toast.LENGTH_SHORT).show();

        fileNameFI = fileNameFIInf;
        new DownloadFileFromURL().execute(file_urlInfo);
        //Log.d("tempStr", tempStr);

        try {
            String title;
            String body;
            String strFromFile = readContent(fileNameFIInf);

            JSONArray jsonArray = new JSONArray(strFromFile);
            length = jsonArray.length();

            namesArray = new String[length];
            descriptionArray = new String[length];

            //Log.d("jsonArray.length() >> ",Integer.toString(length));

            for (int i = 0; i < length; i++){
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                title = jsonObject.getString("title");
                body = jsonObject.getString("body");
                namesArray[i] = title;
                descriptionArray[i] = body;
                //Log.d("jsonObject(title)", title);
                //Log.d("jsonObject(body)", body);
            }

        } catch (JSONException je) {  }

        fileNameFI = fileNameFIImg;
        new DownloadFileFromURL().execute(file_urlImage);

        try {
            String thumbnailUrl;
            String strFromFile = readContent(fileNameFIImg);
            Log.d("strFromFile >", strFromFile);
            JSONArray jsonArray = new JSONArray(strFromFile);
            Log.d("jsonArray.getString >", jsonArray.getString(0));

            urlsForImageArray = new String[length];

            for (int i = 0; i < length; i++){
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                thumbnailUrl = jsonObject.getString("thumbnailUrl");
                Log.d("jO (url) >> ", thumbnailUrl);
                urlsForImageArray[i] = thumbnailUrl;
            }

        } catch (JSONException je) { Log.d("JSONArray() > ", "Exception"); }


        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, namesArray);
        setListAdapter(mAdapter);

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //String itemText = l.getItemAtPosition(position).toString();
        String itemText = l.getItemAtPosition(position).toString()
                + "\n\n" + descriptionArray[position];

        Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
        intent.putExtra("itemText", itemText);
        startActivity(intent);
    }

    //String tempStr = "";

    public String readContent(String rcFilename) {
        //Integer pos = 0;
        String reString = "";
        String str;
        //String[] deStr;
        //AssetManager assetMng = this.getResources().getAssets();

        try {
            //InputStream e = assetMng.open("content.txt");
            InputStream e = new FileInputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/" + rcFilename);

            BufferedReader is = new BufferedReader(new InputStreamReader(e, "windows-1251"));

            Log.i("readContent", "Start reading");
            while(true) {
                //pos++;
                str = is.readLine();
                //tempStr = str;
                if(str == null) {
                    e.close();
                    is.close();
                    Log.i("readContent", "End reading");
                    return reString;
                } else {
                   // try{
                        reString = reString + str;
                        //JSONArray jsonArray = new JSONArray(str);
                        //Log.d("jsonArray", jsonArray.getString(1));
                        //JSONObject jsonObject = jsonArray.getJSONObject(5);
                        //Log.d("jsonObject", jsonObject.toString());

                   // } catch (JSONException je){}
                    /*deStr = str.split(":");
                    if (deStr[0].equals("item")) {
                        pos = Integer.parseInt(deStr[1]);
                        namesArray[pos] = str;
                    } else {
                       if (!(tempStr.equals("end."))){
                           if (descriptionArray[pos] == null){
                               descriptionArray[pos] = str + " ";
                           } else descriptionArray[pos] = descriptionArray[pos] + str + " ";
                       }
                    }*/
                }
            }
        }  catch (IOException var8) {var8.printStackTrace();}
        return reString;
    }


 /*   public int initArray (){
        String str;
        String[] deStr;
        int iRow = 0;
        AssetManager assetMng = getResources().getAssets();

        try {
            //InputStream e = assetMng.open("content.txt");
            InputStream e = new FileInputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/" + fileNameFI);

            BufferedReader is = new BufferedReader(new InputStreamReader(e, "windows-1251"));

            while(true) {

                str = is.readLine();
                if(str == null) {
                    e.close();
                    is.close();
                    return iRow;
                } else {
                    //deStr = str.split(":");
                    if (str.contains("id")) {
                        iRow++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iRow;
    }*/

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


     /**
     *Background Async Task to download file
     * */

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lengthOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/" + fileNameFI);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    //data to file
                    output.write(data, 0, count);
                }

                output.flush();

                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);

        }

    }
}

