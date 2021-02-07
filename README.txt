Final Project for CS 3200 at Northeastern University
Kevin Cruse & Jacob Shell
4/14/20


INSTALLATION INSTRUCTIONS

1. This project uses JavaFX and lambda expressions, so you first need to install Java 8. Any earlier or later versions will not work. If you have multiple versions of Java installed, make sure to add the bin directory of the Java 8 install to your system path before any other versions of Java.

2. After extracting the zip file we submitted, run the create_database.sql script on your local MySQL server. This should create the simple_spotify database.

3. Add a new user to your MySQL server called ‘simplespotify’ (no quotes) with no password and give this user SELECT and EXECUTEpermissions for the newly created simple_spotify database. The program tries to connect to localhost on port 3306 with these credentials, so if there is a connection error, make sure the server is accessible with these parameters. If you need to change the connection parameters, you will have to edit the constants in Database.java and build the project yourself from our submitted source code, instructions for which are below.

4. There are two ways to run the program. The first way is to simply double click the SimpleSpotify.jar file, but this way does not show any potential error output and may or may not work depending on how Java was installed. The other way is to open a terminal, navigate to the directory where the jar file resides, and run ‘java -jar SimpleSpotify.jar’ (no quotes). This way, any error output will be printed to the terminal.


USAGE INSTRUCTIONS

1. We provided sample albums for testing the upload functionality in our program. If you want to try and upload your own albums, the files need to be in a very specific format, as we haven’t implemented error checking for file uploads yet:
    
    a. The audio files must be in MP3 format and have ID3v2 metadata tags for title, artist, genre, track, release year, and length
    
    b. The cover art file must be in PNG format and have dimensions of 300 by 300 pixels.

2. There is a bug with JavaFX where, after selecting the files you want when uploading a new album, the upload album window may disappear. To fix this, simply minimize and reopen the program, and the window should come back.

3. If you get an error regarding maximum packet size, uncomment and run the SET command in our create_database.sql script. This will temporarily increase the maximum packet size for the session.

4. To run a search, type your search query in the search field and hit the return key.

5. After creating a playlist or uploading an album, you will need to run a new search for it to be displayed.

6. The way the song queue works is that the last item you left-clicked in the search panel (the boxes on the left side of the interface) is what the queue is set to. For instance, when you add songs to a playlist, you will have to click on the playlist item in the search panel again so that the queue is set to the playlist, instead of the song you just clicked. Clicking any item on the right panel has no effect.


BUILD INSTRUCTIONS
We packaged an executable jar file with our code, so you will most likely not need to build from scratch. If you do need to rebuild the project for any reason, though, follow these instructions:

1. Install JDK 8. Any earlier or later version will not work. Make sure the bin directory for JDK 8 is in your system path before any other Java version.

2. Open a terminal and navigate to the root directory of the project (where src, resource, lib folders are).

3. Run this command: ‘javac -cp "lib/*" -d bin src/application/*.java src/data/*.java src/ui/*.java’ (no quotes)

4. To run the application: ‘java -cp "bin;lib/*" application.Main’ (no quotes)
