package com.pea.du.tools.gridview;


public class ImageItem {
    public static final String ITEM_PATH = "PATH";
    public static final String ITEM_URL = "URL";

    private int id;
    private String path;
    private String url;
    private String title;

    public ImageItem(int id) {
        this.id = id;
    }

    public ImageItem(String pathORurl, String flag) {
        if (flag == ITEM_PATH)
            this.path = pathORurl;
        else
            this.url = pathORurl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}