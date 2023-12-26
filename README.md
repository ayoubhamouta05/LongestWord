# Longest Word Game

## MainActivity

The **MainActivity** allows the user to set the number of vowels using a spinner and start the game.

## TreeNode Class

### Functions

- **insertRec**: 
  - Inserts a word into the tree.
  - If the root is null or the word is empty, it returns a new node with the word.
  - Calculates the number of common letters between the root word and the word to determine the child index.
  - Recursively calls `insertRec` on the corresponding child.

- **newNode(word)**: 
  - Creates a new node with the specified word.
  - The number of children is equal to the size of the word.

- **searchRec**: 
  - Searches for a word in the tree.
  - Returns false if the root is null or the root word is empty.
  - Returns true if the root word matches the target word.
  - Otherwise, calculates the number of common letters and recursively calls `searchRec` on the corresponding child.

- **deleteCompletelyRec**: 
  - Deletes the entire tree recursively.
  - Returns null if the root is null or the root word is empty.
  - Recursively calls `deleteCompletelyRec` on all children of the root and then returns null.

## PlayActivity

In the **PlayActivity**, a list of **HistoryData** is created to store player tries, and another list (**wordsList**) is created to store all words available with the given letter combination.

### Game Flow

1. The `play` function is called, where the `wordsList` is populated with all combinations of the drawn letters.
2. The `getAllCombinations` function generates all possible combinations of the drawn letters.
3. Players enter words, and the game checks if the entered word is in `wordsList` and if it is the longest. If multiple longest words exist, the game checks their lengths and displays the result.
4. The `addTryToHistory` function adds the player's try to the history list, and `changePlayer` alternates between players.
5. If a player forms the longest word, a win dialog is displayed with the longest word and options to try again or exit the game.

### Loading Words from File

- The tree is initially populated with an array of letters randomly selected using the `drawnRandom` function.
- The `insertWordsFromFileIntoTree` function reads each line from a file and inserts the words into the tree.
