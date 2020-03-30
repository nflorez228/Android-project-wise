package com.nicoft.bewise;

/**
 * Created by Nicolas on 08/10/2015.
 */
public class CampoDeGridView {
    private String title;
    private int image;
    private String tipo;
    private String URL;


    public CampoDeGridView(String title, int image,String tipo,String URL){
        this.image=image;
        this.title=title;
        this.tipo=tipo;
        this.URL=URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }



}
