package com.example.longuestword

class TreeNode(var root : WordTree? = null) {

        fun insert(word: String) {
            root = insertRec(root, word)
        }

        private fun insertRec(root: WordTree?, word: String): WordTree {
            if (root == null || root.word.isEmpty()) {
                return newNode(word)
            }
            // go to the number of common letters indice
            // coerce mean that if (number<min) return min else if (number>max) return max , number.coerceIn(min,max)
            val index = findNumberCommon(root.word, word).coerceIn(0, root.children.size - 1)


            root.children[index] = insertRec(root.children[index], word)

            return root

        }


        private fun newNode(word: String): WordTree {
            val mutableList = mutableListOf<WordTree>()
            for (i in word.indices) {
                mutableList.add(WordTree(""))
            }
            return WordTree(word, mutableList)
        }

        private fun findNumberCommon(rootWord: String, word: String): Int {
            var count = 0
            for (i in rootWord.indices) {
                if (word.contains(rootWord[i])) {
                    count += 1
                }
            }
            return count
        }

        fun printTree(root: WordTree?) {
            if (root != null && root.word.isNotEmpty()) {
                println(root.word)
                for (i in root.children) {
                    printTree(i)
                }
            }
        }


        fun search(word: String): Boolean {
            return searchRec(root, word)
        }

        private fun searchRec(root: WordTree?, word: String): Boolean {
            if (root == null || root.word.isEmpty()) {
                return false
            }
            if (root.word == word) {
                return true
            }
            // coerce mean that if (number<min) return min else if (number>max) return max , number.coerceIn(min,max)
            val index = findNumberCommon(root.word, word).coerceIn(0, root.children.size - 1)

            return searchRec(root.children[index], word)
        }


        fun deleteCompletely() {
            root = deleteCompletelyRec(root)
        }

        private fun deleteCompletelyRec(root: WordTree?): WordTree? {
            if (root == null || root.word.isEmpty()) {
                return null
            } else {
                for (i in root.children) {
                    deleteCompletelyRec(i)
                }
                return null
            }
        }


}