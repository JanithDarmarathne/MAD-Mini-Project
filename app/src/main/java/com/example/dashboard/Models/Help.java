package com.example.dashboard.Models;

public class Help {
    public String HelpName;
    public String HelpContent;
    public String pushKey;

    public Help(){

    }

    public Help(String name,String content,String key){
        HelpName = name;
        HelpContent = content;
        pushKey = key;
    }
}
