package com.hoangpro.dotplaysapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.activity.MainActivity;
import com.hoangpro.dotplaysapp.activity.ViewPostActivity;
import com.hoangpro.dotplaysapp.base.BaseActivity;
import com.hoangpro.dotplaysapp.model.Cat;
import com.hoangpro.dotplaysapp.model.Post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.hoangpro.dotplaysapp.morefunc.Session.currentPost;

class PostHolder extends RecyclerView.ViewHolder {

    ImageView imgThumb;
    TextView tvTitle;
    TextView tvTime, tvFavorite;

    public PostHolder(@NonNull View itemView) {
        super(itemView);
        imgThumb = itemView.findViewById(R.id.imgThumb);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvFavorite = itemView.findViewById(R.id.tvFavorite);
    }
}

class CatHolder extends RecyclerView.ViewHolder {

    RecyclerView rvCat;
    CatHzAdapter adapter;
    List<Cat> list;

    public CatHolder(@NonNull View itemView) {
        super(itemView);
        rvCat = itemView.findViewById(R.id.rvCats);
    }
}

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Post> list;
    private Context context;
    private boolean hasCatList;

    public PostListAdapter(List<Post> list, Context context, boolean hasCatList) {
        this.list = list;
        this.context = context;
        this.hasCatList = hasCatList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new PostHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false));
        else
            return new CatHolder(LayoutInflater.from(context).inflate(R.layout.view_cat_hz, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CatHolder) {
            CatHolder catHolder = (CatHolder) holder;
            getCatList(catHolder);
        } else {
            PostHolder postHolder = (PostHolder) holder;
            final Post post = list.get(position);
            Glide.with(context).load(post.img).into(postHolder.imgThumb);
            postHolder.tvTitle.setText(post.name);
            postHolder.tvTime.setText(post.timePost);
            postHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity activity = (BaseActivity) context;
                    currentPost = post;
                    activity.openActivity(ViewPostActivity.class, false);
                }
            });
        }
    }

    private void getCatList(final CatHolder catHolder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dotplays.com/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Document document = Jsoup.parse(s);
                        if (document != null) {
                            Element parents = document.getElementsByClass("widget widget_categories").first();
                            Elements elements = parents.getElementsByTag("a");
                            catHolder.list = new ArrayList<>();
                            for (Element element : elements) {
                                if (element != null) {
                                    catHolder.list.add(new Cat(element.text(), element.attr("href")));
                                }
                            }
                            catHolder.adapter = new CatHzAdapter(catHolder.list, context);
                            catHolder.rvCat.setAdapter(catHolder.adapter);
                            catHolder.rvCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
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
    public int getItemViewType(int position) {
        if (hasCatList)
            return position == 0 ? 0 : 1;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
