package id.putraprima.retrofit.api.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.putraprima.retrofit.R;

public class Recipe extends AbstractItem<Recipe, Recipe.ViewHolder> {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nama_resep")
    @Expose
    private String namaResep;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("bahan")
    @Expose
    private String bahan;
    @SerializedName("langkah_pembuatan")
    @Expose
    private String langkahPembuatan;
    @SerializedName("foto")
    @Expose
    private String foto;

    public Recipe() {
    }

    public Recipe(Integer id, String namaResep, String deskripsi, String bahan, String langkahPembuatan, String foto) {
        this.id = id;
        this.namaResep = namaResep;
        this.deskripsi = deskripsi;
        this.bahan = bahan;
        this.langkahPembuatan = langkahPembuatan;
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaResep() {
        return namaResep;
    }

    public void setNamaResep(String namaResep) {
        this.namaResep = namaResep;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getLangkahPembuatan() {
        return langkahPembuatan;
    }

    public void setLangkahPembuatan(String langkahPembuatan) {
        this.langkahPembuatan = langkahPembuatan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.rv_resep;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_recipe;
    }

    public class ViewHolder extends FastAdapter.ViewHolder<Recipe> {
        ImageView imgResep;
        TextView tvResep, tvDeskripsi;
        public ViewHolder(View itemView) {
            super(itemView);
            imgResep = itemView.findViewById(R.id.iv_resep);
            tvResep = itemView.findViewById(R.id.tv_resep);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi);
        }

        @Override
        public void bindView(Recipe item, List<Object> payloads) {
            Picasso.get().load("https://mobile.putraprima.id/uploads/" + item.foto).into(imgResep);
            tvResep.setText(item.namaResep);
            tvDeskripsi.setText(item.deskripsi);
        }

        @Override
        public void unbindView(Recipe item) {
            imgResep.setImageBitmap(null);
            tvResep.setText(null);
            tvDeskripsi.setText(null);
        }

    }
}