package com.AutomationUniversity;

 import java.io.BufferedReader;
 import java.text.ParseException;
 import java.io.IOException;
 import java.io.InputStreamReader;

/**
 * Hi, team,
 *
 * In preparation for our class next Tuesday, could you please review the CodeCademy class (https://www.codecademy.com/learn/learn-java?coursePageWithSignup=true) ? It would be great if you could post any questions here about the main concepts in the course:
 * Inheritance and Polymorphism
 * Class Instances
 * If-Then-Else and Loops
 * Strings
 *
 * This will allow us to jump into more fun concepts like:
 * 1. What's the parent of
 *
 * Class Outline:
 * 1. Review:
 *   a.
 */
public class TodoApp {
    public static ToDoAbstract Container = new Container("");

    public static void main(String[] ignored) throws ParseException, IOException, ClassNotFoundException, AutomationUniversityException {
        System.out.println("-------------------");
        System.out.println("This is a simple and easy to use todo app.");
        System.out.println("Type 'help' to know how to use this app.");
        System.out.println("-------------------");

        Tree tree = new Tree("/tmp/todos.tree.db");

        System.out.println("What do you want to do? See 'help' for a list of available options.\n");
        while (true) {
            System.out.printf("> ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String argsString = reader.readLine();
            String[] args = argsString.split(" ");

            if (args.length < 1) {
                System.out.printf("Please provide at least one argument or type 'help' for more detail.");
                return;
            }
            String firstArg = args[0];

            if ("add_container".equals(firstArg)) {
                if (args.length < 3) {
                    System.out.printf("Add a container requires an id and a title");
                    continue;
                }
                int id = Integer.parseInt(args[1]);
                String title = args[2];
                tree.findAndAddTodo(new Container(title), id);
                System.out.println("Done adding a container : " + title + " to id: " + id);
            } else if ("add_item".equals(firstArg)) {
                // add_item "id" "title"
                if (args.length < 3) {
                    System.out.printf("Add an item requires a container id and a title");
                    continue;
                }
                int id = Integer.parseInt(args[1]);
                String title = args[2];
                tree.findAndAddTodo(new Item(title), id);
                AutomationUniversityException aue = (AutomationUniversityException)new Exception("Why doesn't this work");
                throw aue;
//                System.out.println("Done adding item: " + title + " to id: " + id);
            } else if ("delete".equals(firstArg)) {
                // delete "id"
                if (args.length < 2) {
                    System.out.printf("Delete a container requires a container id");
                    continue;
                }
                int id = Integer.parseInt(args[1]);
                tree.findAndDeleteTodo(id);
                System.out.println("Done deleting a todo id: " + id);
            } else if ("list".equals(firstArg)) {
                System.out.println(tree.printTree());
            } else if ("save".equals(firstArg)) {
                tree.save();
            } else if ("load".equals(firstArg)) {
                tree = tree.load();
            } else if ("help".equals(firstArg)) {
                help();
            } else if ("exit".equals(firstArg)) {
                System.out.println("Thank you for using the simple todo application. Exiting..");
                System.exit(1);
            } else {
                System.out.printf("Unknown argument: " + firstArg);
                System.out.println(". Please take a look at the supported arguments at\n");
                help();
            }
        }
    }

    public static void help() {
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
}
