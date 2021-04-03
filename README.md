# DimensionFinder

Simple Tool that allows you to find possible Strings for a given Dimension ID

# What is this?

Designed for the Minecraft [20w14-Infinite Snapshot](https://minecraft.fandom.com/wiki/Java_Edition_20w14%E2%88%9E).
This snapshot introduced a new dimension system:<br>
<ul>
<li>Build a Nether Portal</li>
<li>Get a Book and Quill</li>
<li>Write text</li>
<li>Throw the book into the portal</li>
<li>Warp to a randomly generated dimension</li>
</ul>
The Book's content is hashed and the resulting `integer` is the dimension ID (I call it dimension ID)<br>
This tool brute-forces the book's text to find text, which leads to the dimension ID

# Usage

<ul>
<li>Clone this repository</li>
<li>Edit the Main Class (DimensionFinder)</li>
<li>Optional: Change the dimension IDs you are looking for</li>
<li>Optional: Change the characters the text can contains</li>
<li>Run the Main class (DimensionFinder)</li>
</ul>

## Dependencies

[Guava](https://mvnrepository.com/artifact/com.google.guava/guava) For the Hashing method<br>
[JetBrains Annotations](https://mvnrepository.com/artifact/org.jetbrains/annotations) For @Nullable, …

# Other-Information

## Dimension IDs you might want to know (for Speed Runs, etc)

<ul>
<li>2: The End</li>
<li>1: Overworld (Note: Warping from Overworld to Overworld does nothing)</li>
<li>0: The Nether</li>
</ul>

## List of Strings for the “normal” Dimensions

Copy and Paste the text (without “”) into a book, throw it in a nether portal and travel to the specific dimension!
You can find the results in [7-Character Search](result_seven_character_search.txt)
and [6-Character Search](result_six_character_search.txt). Seven Character search took way too long by the way…