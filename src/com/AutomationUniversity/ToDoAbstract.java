package com.AutomationUniversity;

import com.AutomationUniversity.ToDo;

import java.io.Serializable;
import java.util.UUID;

public abstract class ToDoAbstract implements ToDo, Serializable {
    protected int id;
    protected String title;

    public ToDoAbstract(String title){
        this.id = UUID.randomUUID().hashCode();
        this.title = title;
    }

    public String toString(){
        return "{ type: " + getClass().getSimpleName() +
                ", id: " + id +
                ", title: " + title + "}";
    }

    public Container getValue() {
        return new Container(this.title);
    }
}