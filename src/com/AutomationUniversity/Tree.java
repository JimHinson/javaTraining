package com.AutomationUniversity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Tree implements Serializable {
        /**
         * The data is serialized and stored in this file.
         */
        private String saveLocation = "/tmp/todos.db";
        
        /**
         * The tree always has a root
         */
//        private TodoApp.Container root = new TodoApp.Container("");
        private Container root = new Container("root");
        public Tree(String saveLocation){
            this.saveLocation = saveLocation;
        }
        
        public Tree findAndAddTodo(ToDoAbstract todo, int id){
            if (id == 0){
                root.add(todo);
            } else {
                root.findAndAddContainer(todo, id);
            }
        
            return this;
        }
        
        public Tree findAndDeleteTodo(int id){
            if (id == 0){
                System.out.printf("Can't delete the root");
                return this;
            } else {
                root.findAndDeleteTodo(id);
            }
        
            return this;
        }
        
        public String toString(){
            return root.toString();
        }
        
        public String printTree(){
            return recursivePrint(root, 0);
        }
        
        private String recursivePrint(ToDoAbstract node, int level){
            String result = "";
            String indent = "";
            for(int i = 0; i < level; i++){
                indent += "   ";
            }
        
            result += indent + "-" + node.getClass().getSimpleName() + "[id]=" + node.id + "\n";
            result += indent + "(todo): " + node.title + "\n";
        
            if (node.getClass() == Container.class){
                for (Map.Entry<Integer, ToDoAbstract> n : ((Container) node).todos.entrySet()){
                    result += recursivePrint(n.getValue(), level + 1);
                }
            }
            return result;
        }
        
        public Tree load() throws IOException, ClassNotFoundException {
            Path path = Paths.get(saveLocation);
            byte[] array = Files.readAllBytes(path);
            ByteArrayInputStream bis = new ByteArrayInputStream(array);
            ObjectInput in = null;
        
            try {
                in = new ObjectInputStream(bis);
                return (Tree) in.readObject();
            } finally {
                try {
                    bis.close();
                } catch (IOException ex) {
                    // ignore close exception
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    // ignore close exception
                }
            }
        }
        
        public void save() throws IOException {
            OutputStream file = new FileOutputStream(saveLocation);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(this);
                file.write(bos.toByteArray());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ex) {
                    // ignore close exception
                }
                try {
                    bos.close();
                } catch (IOException ex) {
                    /**
                     * TODO: Don't like ignoring exceptions
                     */
                    // ignore close exception
                }
            }
            file.close();
        }
    }