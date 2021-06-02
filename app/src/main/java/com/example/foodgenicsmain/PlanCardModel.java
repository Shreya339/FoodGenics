package com.example.foodgenicsmain;

import java.io.Serializable;
import java.util.Date;

public class PlanCardModel implements Serializable {
    int cur_cal,tot_cal,cur_carbs,cur_protien,cur_fat,num_of_days;
    String card_title, created_date;

    public PlanCardModel(int cur_cal, int tot_cal, int cur_carbs, int cur_protien, int cur_fat, int num_of_days, String card_title, String created_date) {
        this.cur_cal = cur_cal;
        this.tot_cal = tot_cal;
        this.cur_carbs = cur_carbs;
        this.cur_protien = cur_protien;
        this.cur_fat = cur_fat;
        this.num_of_days = num_of_days;
        this.card_title = card_title;
        this.created_date = created_date;
    }

    public int getCur_cal() {
        return cur_cal;
    }

    public void setCur_cal(int cur_cal) {
        this.cur_cal = cur_cal;
    }

    public int getTot_cal() {
        return tot_cal;
    }

    public void setTot_cal(int tot_cal) {
        this.tot_cal = tot_cal;
    }

    public int getCur_carbs() {
        return cur_carbs;
    }

    public void setCur_carbs(int cur_carbs) {
        this.cur_carbs = cur_carbs;
    }

    public int getCur_protien() {
        return cur_protien;
    }

    public void setCur_protien(int cur_protien) {
        this.cur_protien = cur_protien;
    }

    public int getCur_fat() {
        return cur_fat;
    }

    public void setCur_fat(int cur_fat) {
        this.cur_fat = cur_fat;
    }

    public int getNum_of_days() {
        return num_of_days;
    }

    public void setNum_of_days(int num_of_days) {
        this.num_of_days = num_of_days;
    }

    public String  getCard_title() {
        return card_title;
    }

    public void setCard_title(String card_title) {
        this.card_title = card_title;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
