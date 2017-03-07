package com.support.android.designlibdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.korama.model.Post;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import static com.support.android.designlibdemo.R.id.a;

public class PrintHttpResultTest extends AppCompatActivity {


    static TextView txt;
    public static String t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_http_result_test);

        txt = (TextView)findViewById(a);
        final String[] a = {""};







        System.out.println("ffffffffffffffffffffff");

        GetDataTask ddt = new GetDataTask();
        ddt.execute();

    }

    public void setTxt(String s){

        txt.setText(s);


    }

    class GetDataTask extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(PrintHttpResultTest.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("I am getting your JSON");
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://korama.net").newBuilder();
            urlBuilder.addQueryParameter("json", "get_recent_posts");
            urlBuilder.addQueryParameter("page", "303");
            urlBuilder.addQueryParameter("count", "5");
            String url = urlBuilder.build().toString();


            Request request = new Request.Builder()
                    .url(url)
                    .build();
            String result="f";

            try {
                Response r =client.newCall(request).execute();
                result = r.body().string();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray articles = jsonObject.getJSONArray("posts");
                Util.posts = new LinkedList<>();
                for(int i=0 ; i<articles.length();i++){
                    JSONObject article = new JSONObject(articles.get(i).toString());
                    Post p =new Post();
                    p.setTitle(article.getString("title"));
                    p.setImage_url(article.getString("thumbnail"));

                    Util.posts.add(p);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            txt.setText(aVoid);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            dialog.dismiss();

        }
    }

}
