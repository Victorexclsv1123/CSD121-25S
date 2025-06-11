package lab2; // package that created by the user

/*type: class
purpose: commonly use to load image file from the stream and it will easily read, write image, manipulate, and data video.
Kinds of Classes:
ImageIO – provides static methods like read() and write() for handling image files.
ImageReader, ImageWriter – for low-level image format operations.
ImageTypeSpecifier, ImageInputStream, ImageOutputStream
*/

import javax.imageio.ImageIO;

/*package: javax.swing
purpose: this will help us to build/develop a GUI.
classes:
top-level windows - JFrame, JDialog
user interface elements - JLabel, JButton, JTextField, JPanel
containers and layout managers
        */
import javax.swing.*;

/*package: java.awt
purpose: used to create GUI and graphics, mostly common in older GUI.
kinds of Classes:
component classes - Frame, Button, Label
graphics classes - Graphics, Color, Font, Image
layout managers - FlowLayout, BorderLayout, GridLayout
event handling - ActionEvent, MouseEvent
 */
import java.awt.*;

/*package: java.io.IOException
part of the Java Image I/O API, used to read, write, and manipulate image files.
kinds of Classes:
imageIO – provides static methods like read() and write() for handling image files.
imageReader, ImageWriter – for low-level image format operations.
imageTypeSpecifier, ImageInputStream, ImageOutputStream
*/
import java.io.IOException;

/*package: java.io.InputStream
purpose: provides classes for system input and output through data streams, serialization, and file system access.
 Useful for reading/writing bytes, characters, and objects.
classes: Windows (JFrame), buttons (JButton), labels (JLabel), panels (JPanel), and many others.
*/
import java.io.InputStream;

/*package: java.net
purpose: used for networking, such as working with URLs, sockets, and internet addresses.
classes:
URI, URL – represent web addresses
Socket and ServerSocket – for network communication
InetAddress and URLConnection
 */
import java.net.URI;

/*
kinds of Classes:
HttpClient – sends HTTP requests
HttpRequest – builds HTTP requests
HttpResponse – handles responses
HttpRequest.BodyPublisher, HttpResponse.BodyHandler - control body types

 */
import java.net.http.HttpClient;

/*
purpose: represents the HTTP client. It is used to send HTTP requests and receive responses.
constructor: HttpClient.newHttpClient() to create an instance (factory method)
classes:
.send(request, handler) -	Sends a synchronous request
.sendAsync(request, handler)- Sends an asynchronous request
 */

import java.net.http.HttpRequest;
/*
purpose: represents an HTTP response from a server. It contains status code, headers, and body.
classes:

HttpClient - Main class used to send requests and receive responses. Can be configured with options like redirect policy, proxy, and SSL.
HttpRequest	- Represents an HTTP request. Built using a builder pattern (HttpRequest.newBuilder()).
HttpResponse<T>	- Represents an HTTP response, with generic type T for the response body (e.g., String, InputStream).
HttpRequest.BodyPublishers - Contains static methods to create request bodies (e.g., ofString(), ofFile()).
HttpResponse.BodyHandlers -	Contains static methods to process response bodies (e.g., ofString(), ofInputStream()).
WebSocket - Provides support for WebSocket communication (real-time, full-duplex).
HttpHeaders - Represents HTTP headers (used in requests and responses).
HttpTimeoutException - Exception thrown when a request times out.
 */
import java.net.http.HttpResponse;

//method: AvatarGenerator
//type: user-defined class
//purpose: generate and display random avatars from the internet using the DiceBear API.
public class AvatarGenerator {

    //method: main
    //this is the entry code/core for the program
    public static void main(String[] args) {


        //method: AvatarGenerator.getRandomAvatarStream()
        //type: static method call(since the methods are called on the class (AvatarGenerator) directly, not on an object instance.)
        //purpose: to generate a random avatar to display
        try {
            var avatarStream = AvatarGenerator.getRandomAvatarStream();

            //method:  AvatarGenerator.showAvatar(avatarStream);
            //type: class method
            //purpose: display the image in the gui
            AvatarGenerator.showAvatar(avatarStream);

            // Method call: e.printStackTrace()
            // Type: instance method of Exception class
            // Purpose: Prints exception stack trace to standard error
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    //method: getRandomAvatarStream
    //type: public static
    //throws a IOException and InterruptedException when encountered certain runtime problems.
    public static InputStream getRandomAvatarStream() throws IOException, InterruptedException {


        // Pick a random style
       //Variable type: String[]
        //list of styles
        String[] styles = { "adventurer", "adventurer-neutral", "avataaars", "big-ears", "big-ears-neutral", "big-smile", "bottts", "croodles", "croodles-neutral", "fun-emoji", "icons", "identicon", "initials", "lorelei", "micah", "miniavs", "open-peeps", "personas", "pixel-art", "pixel-art-neutral" };

        //method: Math.random()
        //type:  class method
        //purpose: it will choose a random style from the  String[] styles
        var style = styles[(int)(Math.random() * styles.length)];

        
        // Generate a random seed
        // it generate random integer between 0 to 99999 to randomize the avatar
        //method: Math.random()
        var seed = (int)(Math.random() * 10000);

        // Create an HTTP request for a random avatar
        //method: URI.create
        //the purpose of the url is to generate an avatar image by sending a request to the URL
        //type: class method
        var uri = URI.create("https://api.dicebear.com/9.x/%s/png?seed=%d".formatted(style, seed));

        //method: HttpRequest.newBuilder(uri).build();
        //type: instance method
        //purpose: to build an http request
        var request = HttpRequest.newBuilder(uri).build();

        // Send the request
        //method: HttpClient.newHttpClient()
        //type:static method
        //purpose: to create a new http client
        try (var client = HttpClient.newHttpClient()) {

            //method: client.send
            //type: instance method
            //purpose: sends the HTTP request and gets the response as InputStream
            var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            //method: response.body
            //type: instance method
            //purpose: return the response body, which is an input stream
            return response.body();
        }
    }


    //method:showAvatar
    //purpose: responsible for displaying the image
    //type: static method
    //argument: inputStream ImageStream
    public static void showAvatar(InputStream imageStream) {

            //constructor: new JFrame("PNG Viewer")
            //purpose: creates a new window with a title
            JFrame frame = new JFrame("PNG Viewer");

            //method:  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //type: instance method
            //purpose: specifies the close operation for the frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            //method: frame.setResizable(false)
            //type: instance method
            //purpose: makes the window not resizable
            frame.setResizable(false);

            //method: frame.setSize(200, 200)
            //type: instance method
            //purpose: sets the window size to 200x200 pixels
            frame.setSize(200, 200);

            //method: frame.getContentPane()
            //type: instance method
        //purpose: gets the content pane of the frame
        //sets the background color to black
            frame.getContentPane().setBackground(Color.BLACK);


            try {
                // Load the PNG image
                //method:ImageIO.read(imageStream)
                //type: class method
                //purpose: wraps the image into an icon for display
                Image image = ImageIO.read(imageStream);

                // Create a JLabel to display the image
                //constructor: new Jlabel and the new ImageIcon
                JLabel imageLabel = new JLabel(new ImageIcon(image));

                // Method: frame.add(component, position)
                // Type: instance method
                // Purpose: Adds component to the frame layout
                frame.add(imageLabel, BorderLayout.CENTER);

                //catches any IOException that may occur while reading the image
                //type: instance method
                //prints the stack trace to the console for debugging
            } catch (IOException e) {
                e.printStackTrace();
            }

            //makes the Jframe visible on the screen
            frame.setVisible(true);
    }
}
