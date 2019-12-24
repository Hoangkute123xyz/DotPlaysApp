package com.hoangpro.dotplaysapp.model;

public class Post {
    public String name,img,timePost,link,description;

    public Post(String name, String img, String timePost, String link, String description) {
        this.name = name;
        this.img = img;
        this.timePost = timePost;
        this.link = link;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Post{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
