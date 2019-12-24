package com.hoangpro.dotplaysapp.model;

public class DotPlay {
    public String title;
    public String img;

    public DotPlay(String title, String img) {
        this.title = title;
        this.img = img;
    }

    @Override
    public String toString() {
        return "DotPlay{" +
                "title='" + title + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
