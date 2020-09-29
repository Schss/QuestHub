package com.example.testquesthub.Models;

public class Quest {
    private String ad, info, codeword;

    public Quest(){}

    public  Quest(String ad, String info, String codeword){
        this.ad = ad;
        this.info = info;
        this.codeword = codeword;
    }

    public String getAd(){
        return ad;
    }

    public void setAd(String ad){
        this.ad = ad;
    }

    public String getInfo(){
        return info;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public String getCodeword(){
        return codeword;
    }

    public void setCodeword(String codeword){
        this.codeword = codeword;
    }

}
