package br.com.recrutamentoandroid.model;

import java.util.List;

/**
 * Created by Vitor on 29/08/2015.
 */
public class Episode {

    private Integer season;

    private Integer number;

    private String title;

    private ShowIds ids;

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ShowIds getIds() {
        return ids;
    }

    public void setIds(ShowIds ids) {
        this.ids = ids;
    }
}
