package com.hoangpro.dotplaysapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.hoangpro.dotplaysapp.base.BaseFragment;
import com.hoangpro.dotplaysapp.model.Post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.hoangpro.dotplaysapp.morefunc.Session.currentCat;

public class ViewCatFragment extends BaseFragment {
    private String TAG = "ViewCatFragment";

    @Override
    public int setLayout() {
        return R.layout.fragment_view_cat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private RecyclerView rvPost;
    private ProgressBar pgLoading,pgLoadMore;
    private List<Post> list;
    private PostListAdapter adapter;
    private boolean hasNextPage=false;
    private int currentPage = 1;
    private AppCompatActivity activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(currentCat.name);
        list = new ArrayList<>();
        adapter = new PostListAdapter(list, getActivity(), false);
        rvPost.setAdapter(adapter);
        rvPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        getData();
        rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition()==list.size()-1 && hasNextPage){
                    hasNextPage=false;
                    pgLoadMore.setVisibility(View.VISIBLE);
                    getNextPageData();
                }
            }
        });
    }

    private void getNextPageData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, currentCat.link + "page/" + currentPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Document document = Jsoup.parse(s);
                        if(document!=null){
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
                            adapter.notifyDataSetChanged();
                            pgLoadMore.setVisibility(View.GONE);
                            hasNextPage=document.getElementsByClass("next page-numbers").first()!=null;
                            currentPage++;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.seach_menu_toolbar, menu);
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, currentCat.link,
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
                            adapter.notifyDataSetChanged();
                            pgLoading.setVisibility(View.GONE);
                            hasNextPage=document.getElementsByClass("next page-numbers").first()!=null;
                            currentPage++;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void initView(View view) {
        rvPost = view.findViewById(R.id.rvPost);
        pgLoading = view.findViewById(R.id.pgLoading);
        pgLoadMore=view.findViewById(R.id.pgLoadMore);
    }
}
