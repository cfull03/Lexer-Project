# Lexer-Project

This repository contains a basic lexer and parser for a simple programming language. The project includes the necessary classes to tokenize and parse the input code, demonstrating fundamental concepts in lexical analysis and parsing.

## Project Structure

- **src/lexer/**: Contains the source code for the lexer and parser.
- **bin/lexer/**: Directory for compiled classes.
- **lexer.jar**: A JAR file that can be used to run the lexer and parser.

## How to Use

### Running the Parser Using lexer.jar

1. **Ensure you have Java installed**: Make sure you have Java Development Kit (JDK) installed on your machine. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-downloads.html).

2. **Open a terminal or command prompt**: Navigate to the directory where `lexer.jar` is located.

3. **Run the JAR file**: Use the following command to run the lexer and parser:

    ```sh
    java -jar lexer.jar
    ```

4. **Using the GUI**: The command above will launch a graphical user interface (GUI) where you can input your code and see the parsing results.

### Sample Code for the RDP

Below is an example of code that can be parsed by the RDP parser:

PROGRAM
x = 8;

LOOP(y = 0 : y < 100)
z = y + x;
END_LOOP

IF(x > y)
c = 10;
END_IF

END_PROGRAM


### Example Code to Use the RDP Programmatically

Here's how you can use the `RDP` parser programmatically by compiling and running the source code:

1. **Navigate to the project directory**: Open a terminal or command prompt and navigate to the project directory.

2. **Compile the Java files**: Use the following command to compile the Java files:

    ```sh
    javac -d bin src/lexer/*.java
    ```

3. **Run the parser programmatically**: Create a new Java file `Example.java` with the following content:

    ```java
    package lexer;

    import java.util.LinkedList;
    import java.util.Queue;

    public class Example {
        public static void main(String[] args) {
            // Sample code to be parsed
            String code = "PROGRAM\n" +
                          "x = 8;\n" +
                          "LOOP(y = 0 : y < 100)\n" +
                          "z = y + x;\n" +
                          "END_LOOP\n" +
                          "IF(x > y)\n" +
                          "c = 10;\n" +
                          "END_IF\n" +
                          "END_PROGRAM";

            // Tokenize the code
            Lexer lexer = new Lexer(code);
            Queue<Token> tokens = lexer.tokenize();

            // Parse the tokens
            try {
                RDP parser = new RDP(tokens);
                parser.program();
                System.out.println("Parsing successful!");
            } catch (IllegalArgumentException e) {
                System.err.println("Parsing failed: " + e.getMessage());
            }
        }
    }
    ```

4. **Compile and run `Example.java`**: Use the following commands to compile and run the example:

    ```sh
    javac -cp bin -d bin Example.java
    java -cp bin lexer.Example
    ```

## Example GUI

The graphical user interface allows users to input code and see parsing results:

![GUI Screenshot](path_to_screenshot)

## License

This project is licensed under the MIT License.


