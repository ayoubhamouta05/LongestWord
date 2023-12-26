package com.example.longuestword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.longuestword.databinding.ActivityPlayBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class PlayActivity : AppCompatActivity() {

    private val LETTERS = charArrayOf(
        'a', 'b', 'c', 'd', 'e', 'f', 'j', 'h', 'i', 'g', 'k', 'l', 'm', 'n', 'o', 'p',
        'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    )

    private var LONGEST_WORD = ""

    private lateinit var treeNode: TreeNode

    private lateinit var binding: ActivityPlayBinding

    private val historyAdapter by lazy {
        HistoryAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        treeNode = TreeNode()

        val maxVowels = intent.extras?.getString("numberOfVowels")
        setupRv()


        CoroutineScope(Dispatchers.IO).launch {
            val drawnLetter = drawnRandom(LETTERS, maxVowels!!.toInt())

            withContext(Dispatchers.Main) {
                binding.drawenLettersTv.text = drawnLetter.contentToString()
                showProgressBar()
            }

            insertWordsFromFileIntoTree(this@PlayActivity, "englishWords.txt")
            play(drawnLetter.joinToString(""), treeNode)
        }

        binding.lampBtn.setOnClickListener {
            Toast.makeText(this, "Longest word : $LONGEST_WORD", Toast.LENGTH_SHORT).show()
        }


    }

    private fun setupRv() {
        binding.historyRv.apply {
            adapter = historyAdapter
            val layoutmanager = LinearLayoutManager(this@PlayActivity)
            layoutmanager.reverseLayout = true
            layoutManager = layoutmanager
        }

    }

    private fun drawnRandom(letters: CharArray, numberVowels: Int): CharArray {
        val letterList = mutableListOf<Char>()

        while (letterList.size < if (numberVowels > 10) 10 else numberVowels) {
            val rand = (0..25).random()
            val letter = letters[rand]

            if (isVowel(letter)) {
                letterList.add(letter)
            }

        }

        while (letterList.size < 10) {
            val rand = (0..25).random()
            val letter = letters[rand]

            if (!isVowel(letter)) {
                letterList.add(letter)
            }
        }

        return letterList.shuffled().toCharArray()
    }

    private fun isVowel(char: Char): Boolean {
        return when (char) {
            'a' -> true
            'e' -> true
            'i' -> true
            'o' -> true
            'u' -> true
            else -> false
        }
    }

    private fun generatePermutations(s: String): List<String> {
        val permutations = mutableListOf<String>()
        val sortedLetters = s.toCharArray().sortedArray()

        while (true) {
            // check if the word is existed and added it to the list
            val word = String(sortedLetters)
            if (wordIsCorrect(treeNode, word)) {
                if (word.length > LONGEST_WORD.length) {
                    LONGEST_WORD = word
                }
                permutations.add(word)
            }


            var i = sortedLetters.size - 2
            while (i >= 0 && sortedLetters[i] >= sortedLetters[i + 1]) {
                i--
            }

            if (i == -1) {
                break
            }

            var j = sortedLetters.size - 1
            while (sortedLetters[j] <= sortedLetters[i]) {
                j--
            }

            val temp = sortedLetters[i]
            sortedLetters[i] = sortedLetters[j]
            sortedLetters[j] = temp

            sortedLetters.sort(i + 1, sortedLetters.size)
        }
        return permutations
    }

    private fun getAllCombinations(s: String): List<String> {
        val combinations = mutableListOf<String>()
        for (i in 0 until (1 shl s.length)) {
            val subset = StringBuilder()
            for (j in s.indices) {
                if ((i and (1 shl j)) > 0) {
                    subset.append(s[j])
                }
            }
            if (subset.isNotEmpty()) {
                combinations.addAll(generatePermutations(subset.toString()))
            }
        }

        return combinations
    }


    private fun wordIsCorrect(tree: TreeNode, word: String): Boolean {
        return tree.search(word)
    }

    private fun play(drawnLetter: String, root: TreeNode) {

        val resultList = mutableListOf<HistoryData>()
        val wordsList: List<String> = getAllCombinations(drawnLetter)



        CoroutineScope(Dispatchers.Main).launch {
            hideProgressBar()
        }

        binding.submitBtn.setOnClickListener {
            val input = binding.inputEd.text.toString().lowercase()
            hideKeyboard()
            binding.inputEd.setText("")
            binding.inputEd.clearFocus()
            val message: String
            if (wordsList.contains(input)) {
                if (input.length == LONGEST_WORD.length) {
                    message = "Congratulations"
                    root.deleteCompletely()
                    showWinDialog(input)
                } else {
                    message = "word is correct but not the biggest : try again"
                }
            } else {
                message = "word is incorrect : try again"
            }

            addTryToHistory(binding.inputLbl.hint.toString(), input, message, resultList)

            changePlayer(binding.inputLbl.hint.toString())

            if (historyAdapter.itemCount >= 2) {
                binding.lampBtn.visibility = View.VISIBLE
            }

        }

    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.inputEd.windowToken, 0)
    }

    private fun showProgressBar() {

        binding.inputLbl.isEnabled = false
        binding.submitBtn.isClickable = false
        binding.submitBtn.isEnabled = false
        binding.inputEd.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.inputLbl.isEnabled = true
        binding.submitBtn.isClickable = true
        binding.submitBtn.isEnabled = true
        binding.inputEd.isEnabled = true
        binding.progressBar.visibility = View.GONE
    }

    private fun addTryToHistory(
        player: String,
        input: String,
        message: String,
        resultList: MutableList<HistoryData>
    ) {
        resultList.add(
            HistoryData(
                player,
                input,
                message
            )
        )
        historyAdapter.differ.submitList(resultList)
        historyAdapter.notifyItemChanged(historyAdapter.differ.currentList.lastIndex)
        binding.historyRv.scrollToPosition(historyAdapter.itemCount - 1)
    }

    private fun changePlayer(player: String) {
        binding.inputLbl.hint = if (player == "First Player") "Second Player" else "First Player"
    }

    private fun showWinDialog(word: String) {
        val alertDialog = AlertDialog.Builder(this@PlayActivity)
        alertDialog.setTitle("${binding.inputLbl.hint.toString()} Win")
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Longest word : $word")
        alertDialog.setPositiveButton("Try Again") { dialog, _ ->
            val intent = Intent(this@PlayActivity, MainActivity::class.java)
            dialog.dismiss()
            startActivity(intent)
        }
        alertDialog.setNegativeButton("Exit game") { dialog, _ ->
            finishAffinity()
        }
        alertDialog.show()

    }

    private fun insertWordsFromFileIntoTree(context: Context, fileName: String) {


        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?

            while (reader.readLine().also { line = it } != null) {
                treeNode.insert(line!!)
            }

            reader.close()
            inputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

}