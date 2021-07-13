# DimensionFinder

Simple Tool that allows you to find Strings for a given Dimension ID

# Introduction / What does this do?

Designed for the Minecraft [20w14-Infinite Snapshot](https://minecraft.fandom.com/wiki/Java_Edition_20w14%E2%88%9E).
This snapshot introduced a new dimension system:  

* Build a Nether Portal
    * 10 or more Obsidian + something to light the portal
* Get a [Book and Quill (Writeable Book)](https://minecraft.fandom.com/wiki/Book_and_Quill)
    * Book + Feather + Ink Sack
* Write text
* Throw the signed book into the portal
* Enter the portal to warp to a randomly generated dimension

## Explanation

The Book's content is being hashed and the resulting hash is the dimension ID (I call it dimension ID)  
This tool brute-forces possible text in order to find dimension IDs for you.

# Usage

## Downloaded Jar / Command Line
The latest release can be found [here](https://github.com/spasskopf/dimensionfinder/releases/latest).  
Run the Jar-File via the command-line (`cmd` or `terminal`).  
The programm will ask for the necessary arguments.  
Detailed Information can be found [here](https://github.com/spasskopf/dimensionfinder/wiki/How-do-I-use-this%3F).

## Editing the Code
* Clone this repository `git clone https://github.com/spasskopf/dimensionfinder`
* Edit the Main Class (`DimensionFinder.java`)
* Optional: Change the dimension IDs you want to find text for
* Optional: Change the characters the text consists of
* Run the program.
  * Main class: `DimensionFinder.java`

# Other-Information

## Interesting Dimension IDs (for Speedruns, etc)

|ID|Dimension|One example|
|:----|----|----|
|0|The Nether|cvtna7t|
|1|Overworld|34blv8|
|2|The End|qb7kw9|

## Results for the most common Dimension IDs (0, 1 and 2)

You can find the results here:
* [7-Character Search Output](result_seven_character_search.txt)
* [6-Character Search Output](result_six_character_search.txt)

## Dependencies

* [Guava](https://mvnrepository.com/artifact/com.google.guava/guava) to calculate the hash
* [JetBrains Annotations](https://mvnrepository.com/artifact/org.jetbrains/annotations) for the annotations

## Fabric-Mod that lets you view the hashes in-game

[Link to the mod](https://github.com/spasskopf/dimensionfinder-fabric-mod)
