/**
 import java.io.BufferedReader;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.FileOutputStream;
 import java.text.ParseException;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.ObjectInput;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutput;
 import java.io.ObjectOutputStream;
 import java.io.OutputStream;
 import java.io.Serializable;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.Paths;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.UUID;
 ​
 public class TodoApp {
 ​
 public static void main(String[] ignored) throws ParseException, IOException, ClassNotFoundException {
 System.out.println("-------------------");
 System.out.println("This is a simple and easy to use todo app.");
 System.out.println("Type 'help' to know how to use this app.");
 System.out.println("-------------------");
 ​
 Tree tree = new Tree("/tmp/todos.tree.db");
 ​
 System.out.println("What do you want to do? See 'help' for a list of available options.\n");
 while(true){
 System.out.printf("> ");
 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
 String argsString = reader.readLine();
 String[] args = argsString.split(" ");
 ​
 if (args.length < 1){
 System.out.printf("Please provide at least one argument or type 'help' for more detail.");
 return;
 }
 String firstArg = args[0];
 ​
 if ("add_container".equals(firstArg)) {
 // add_container "id" "title"
 if (args.length < 3){
 System.out.printf("Add a container requires an id and a title");
 continue;
 }
 int id = Integer.parseInt(args[1]);
 String title = args[2];
 tree.findAndAddTodo(new Container(title), id);
 System.out.println("Done adding a container : " + title + " to id: " + id);
 } else if ("add_item".equals(firstArg)) {
 // add_item "id" "title"
 if (args.length < 3){
 System.out.printf("Add an item requires a container id and a title");
 continue;
 }
 int id = Integer.parseInt(args[1]);
 String title = args[2];
 tree.findAndAddTodo(new Item(title), id);
 System.out.println("Done adding item: " + title + " to id: " + id);
 } else if ("delete".equals(firstArg)){
 // delete "id"
 if (args.length < 2){
 System.out.printf("Delete a container requires a container id");
 continue;
 }
 int id = Integer.parseInt(args[1]);
 tree.findAndDeleteTodo(id);
 System.out.println("Done deleting a todo id: " + id);
 } else if("list".equals(firstArg)){
 System.out.println(tree.printTree());
 } else if("save".equals(firstArg)){
 tree.save();
 } else if("load".equals(firstArg)){
 tree = tree.load();
 } else if("help".equals(firstArg)){
 help();
 } else if("exit".equals(firstArg)){
 System.out.println("Thank you for using the simple todo application. Exiting..");
 System.exit(1);
 } else {
 System.out.printf("Unknown argument: " + firstArg);
 System.out.println(". Please take a look at the supported arguments at\n");
 help();
 }
 }
 }
 ​
 public static void help(){
 System.out.println("- help                          : This help message");
 System.out.println("- list                          : List all the items/containers of your todo list");
 System.out.println("- save                          : Save the data to a local temporary file");
 System.out.println("- load                          : Load the data from a local temporary file");
 System.out.println("- add_item <ID> <NAME>          : Add an item/container to your todo list");
 System.out.println("  <ID> = 0 if you want to add to the root level, otherwise, use " +
 "the <ID> of a container you want to add to");
 System.out.println("  <NAME> (String) Is the name of the item you want to add. The " +
 "string cannot contain empty 'spaces'. For example, 'some item' " +
 "is not acceptable.");
 System.out.println("For example, to add an item named buy_milk to the root level, do: ");
 System.out.println(" add_item 0 buy_milk");
 System.out.println("");
 System.out.println("- add_container <ID> <NAME>     : Add an item/container to your todo list");
 System.out.println("  <ID> = 0 if you want to add to the root level, otherwise, use " +
 "the <ID> of a container you want to add to");
 System.out.println("  <NAME> (String) Is the name of the item you want to add. The " +
 "string cannot contain empty 'spaces'. For example, 'some item' " +
 "is not acceptable.");
 System.out.println("");
 System.out.println("- delete <ID>                   : Delete a container / item using an ID");
 System.out.printf("\n\n");
 }
 ​
 public static class Tree implements Serializable {
 ​
 /**
 * The data is serialized and stored in this file.
 */
private String saveLocation = "/tmp/todos.db";
        ​
/**
 * The tree always has a root
 */
private Container root = new Container("");
        ​
public Tree(String saveLocation){
        this.saveLocation = saveLocation;
        }
        ​
public Tree findAndAddTodo(TodoAbstract todo, int id){
        if (id == 0){
        root.add(todo);
        } else {
        root.findAndAddContainer(todo, id);
        }
        ​
        return this;
        }
        ​
public Tree findAndDeleteTodo(int id){
        if (id == 0){
        System.out.printf("Can't delete the root");
        return this;
        } else {
        root.findAndDeleteTodo(id);
        }
        ​
        return this;
        }
        ​
public String toString(){
        return root.toString();
        }
        ​
public String printTree(){
        return recursivePrint(root, 0);
        }
        ​
private String recursivePrint(TodoAbstract node, int level){
        String result = "";
        String indent = "";
        for(int i = 0; i < level; i++){
        indent += "   ";
        }
        ​
        result += indent + "-" + node.getClass().getSimpleName() + "[id]=" + node.id + "\n";
        result += indent + "(todo): " + node.title + "\n";
        ​
        if (node.getClass() == Container.class){
        for (Map.Entry<Integer, TodoAbstract> n : ((Container) node).todos.entrySet()){
        result += recursivePrint(n.getValue(), level + 1);
        }
        }
        ​
        return result;
        }
        ​
public Tree load() throws IOException, ClassNotFoundException {
        Path path = Paths.get(saveLocation);
        byte[] array = Files.readAllBytes(path);
        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        ObjectInput in = null;
        ​
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
        ​
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
        // ignore close exception
        }
        }
        file.close();
        }
        }
        ​
public static class Container extends TodoAbstract {
    public Map<Integer, TodoAbstract> todos = new HashMap<>();
​
    public Container(String title){
        super(title);
    }
​
    public Container add(TodoAbstract todo){
        todos.put(todo.id, todo);
        return this;
    }
​
    public Container delete(int id){
        todos.remove(id);
        return this;
    }
​
    public Container findAndAddContainer(TodoAbstract todo, int id){
        if (this.id != id){
            for(Map.Entry<Integer, TodoAbstract> n : todos.entrySet()){
                if (n.getValue().getClass() == Container.class){
                    ((Container)n.getValue()).findAndAddContainer(todo, id);
                }
            }
        } else {
            add(todo);
        }
​
        return this;
    }
​
    public Container findAndDeleteTodo(int id) {
        if (todos.containsKey(id)) {
            todos.remove(id);
        } else {
            for (Map.Entry<Integer, TodoAbstract> n : todos.entrySet()) {
                if (n.getValue().getClass() == Container.class) {
                    ((Container) n.getValue()).findAndDeleteTodo(id);
                }
            }
        }
​
        return this;
    }
​
    public String toString(){
        return todos.toString();
    }
}
​
public static class Item extends TodoAbstract {
    public Item(String title) {
        super(title);
    }
}
​
public static abstract class TodoAbstract implements Todo, Serializable {
    protected final int id;
    protected String title;
​
    public TodoAbstract(String title){
        this.id = UUID.randomUUID().hashCode();
        this.title = title;
    }
​
    public String toString(){
        return "{ type: " + getClass().getSimpleName() +
                ", id: " + id +
                ", title: " + title + "}";
    }
}
​
interface Todo {
​
}
}
        Collapse




        Jimmy Hinson  11:45 AM
 */