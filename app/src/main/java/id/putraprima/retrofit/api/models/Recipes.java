package id.putraprima.retrofit.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipes<Recipe> {

    @SerializedName("data")
    @Expose
    private List<Recipe> data = null;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Recipes(List<Recipe> data) {
        this.data = data;
    }

    public Recipes() {
    }

    public Recipes(List<Recipe> data, Links links, Meta meta) {
        this.data = data;
        this.links = links;
        this.meta = meta;
    }

    public List<Recipe> getData() {
        return data;
    }

    public void setData(List<Recipe> data) {
        this.data = data;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}