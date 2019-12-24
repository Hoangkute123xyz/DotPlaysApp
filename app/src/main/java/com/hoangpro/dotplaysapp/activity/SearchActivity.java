package com.hoangpro.dotplaysapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.adapter.PostListAdapter;
import com.hoangpro.dotplaysapp.base.BaseActivity;
import com.hoangpro.dotplaysapp.model.Post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private SearchView searchView;
    private String URL = "http://www.dotplays.com/?s=";
    private RecyclerView rvPost;
    private PostListAdapter adapter;
    List<Post> list;
    private ProgressBar pgLoading;
    private String TAG = "SearchActivity";
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        searchView.requestFocusFromTouch();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        list = new ArrayList<>();
        adapter = new PostListAdapter(list, this, false);
        rvPost.setAdapter(adapter);
        rvPost.setLayoutManager(new LinearLayoutManager(this));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getData(URL + toKeyWord(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getData(String url) {
        list.clear();
        adapter.notifyDataSetChanged();
        Log.e(TAG, url);
        pgLoading.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Document document = Jsoup.parse(s);
                        if (document != null) {
                            Elements elements = document.getElementsByClass("post-blogs-container-thumbnails");
                            for (Element element : elements) {
                                Element elmTime = element.getElementsByClass("entry-meta").first();
                                Element elmImg = element.getElementsByClass("blog-featured-thumbnail").first();
                                Element elmName = element.getElementsByTag("a").last();
                                Element elmDescrip = element.getElementsByClass("post-content").first();
                                if (elmDescrip != null && elmImg != null && elmName != null && elmTime != null) {
                                    String img = elmImg.attr("style");
                                    img = img.replace("background-image:url(", "").replace(")", "");
                                    Post post = new Post(elmName.text(), img, elmTime.text(), elmName.attr("href"), elmDescrip.text());
                                    list.add(post);
                                }
                            }
                            if (list.size() == 0) {
                                tvResult.setVisibility(View.VISIBLE);
                            } else {
                                tvResult.setVisibility(View.GONE);
                            }
                            Log.e(TAG, list.size() + "");
                            adapter.notifyDataSetChanged();
                            pgLoading.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private String toKeyWord(String query) {
        String result = "";
        for (String s : query.trim().split(" ")) {
            if (s != null || s.length() > 0) {
                result += s + "+";
            }
        }
        return result.substring(0, result.length() - 1);
    }

    private void initView() {
        searchView = findViewById(R.id.searchView);
        rvPost = findViewById(R.id.rvPost);
        pgLoading = findViewById(R.id.pgLoading);
        tvResult = (TextView) findViewById(R.id.tvResult);
    }
}
