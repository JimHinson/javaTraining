package com.AutomationUniversity;



import java.util.HashMap;
import java.util.Map;

public class Container  {
    public Map<Integer, ToDoAbstract> todos = new HashMap<>();

    public Container(String title){
        super(title);
    }

    public Container Add(ToDoAbstract todo){
        todos.put(todo.id, todo);
        return this;
    }

    public Container delete(int id){
        todos.remove(id);
        return this;
    }

    public Container findAndAddContainer(ToDoAbstract todo, int id){
        if (this.id != id){
            for(Map.Entry<Integer, ToDoAbstract> n : todos.entrySet()){
                if (n.getValue().getClass() == Container.class){
                    ((Container)n.getValue()).findAndAddContainer(todo, id);
                }
            }
        } else {
            Add(todo);
        }

        return this;
    }

    public Container findAndDeleteTodo(int id) {
        if (todos.containsKey(id)) {
            todos.remove(id);
        } else {
            for (Map.Entry<Integer, ToDoAbstract> n : todos.entrySet()) {
                if (n.getValue().getClass() == Container.class) {
                    ((Container) n.getValue()).findAndDeleteTodo(id);
                }
            }
        }

        return this;
    }

    public String toString(){
        return todos.toString();
    }
}

