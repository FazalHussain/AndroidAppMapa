package com.expreso.androidapp.androidapp.Direcciones_GPS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

@SerializedName("todos")
@Expose
private List<Todo> todos = null;

public List<Todo> getTodos() {
return todos;
}

public void setTodos(List<Todo> todos) {
this.todos = todos;
}

}