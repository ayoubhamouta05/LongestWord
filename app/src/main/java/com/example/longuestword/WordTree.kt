package com.example.longuestword

data class WordTree (var word : String , val children : MutableList<WordTree> = mutableListOf())