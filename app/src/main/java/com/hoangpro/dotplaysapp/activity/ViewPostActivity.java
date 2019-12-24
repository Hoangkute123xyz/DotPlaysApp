package com.hoangpro.dotplaysapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hoangpro.dotplaysapp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.hoangpro.dotplaysapp.morefunc.Session.currentPost;

public class ViewPostActivity extends AppCompatActivity {

    private String TAG = "ViewPostActivity";
    private String css = "<html lang=\"en\"><head><link rel=\"stylesheet\" id=\"wp-block-library-css\" href=\"http://www.dotplays.com/wp-includes/css/dist/block-library/style.min.css?ver=5.2.4\" type=\"text/css\" media=\"all\"> " +
            "<link rel=\"stylesheet\" id=\"parent-style-css\" href=\"http://www.dotplays.com/wp-content/themes/feather-magazine/style.css?ver=5.2.4\" type=\"text/css\" media=\"all\"> " +
            "<link rel=\"stylesheet\" id=\"feather-magazine-style-css\" href=\"http://www.dotplays.com/wp-content/themes/newsly-magazine/style.css?ver=5.2.4\" type=\"text/css\" media=\"all\">" +
            "<meta charset=\"UTF-8\"></head><body>";
    private String body = "</body></html>";
    private WebView webView;
    private ProgressBar pgLoading;
    private Toolbar toolbar;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        initView();
        setSupportActionBar(toolbar);
        String title = currentPost.name;
        title = title.substring(title.indexOf("]") + 1).trim();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getContent();
        tvTime.setText(currentPost.timePost);
    }

    private void initView() {
        webView = findViewById(R.id.webView);
        pgLoading = findViewById(R.id.pgLoading);
        toolbar = findViewById(R.id.toolbar);
        tvTime = findViewById(R.id.tvTime);
    }

    private void getContent() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, currentPost.link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Document document = Jsoup.parse(s);
                        if (document != null) {
                            Element element = document.getElementById("content");
                            element.getElementsByClass("dotpl-before-content").first().remove();
                            element.getElementsByClass("dotpl-after-content").first().remove();
                            webView.loadData(css + element.html() + body, "text/html", "utf-8");
                            Log.e(TAG, element.html());
                        }
                        pgLoading.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
