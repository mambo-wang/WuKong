package com.wukong.service.algo.string;

/**
 * leetCode 208. 实现 Trie (前缀树)
 * https://leetcode-cn.com/problems/implement-trie-prefix-tree/
 */
class Trie {
    TrieNode root;
    /** Initialize your data structure here. */
    public Trie() {
        root = new TrieNode();
    }
    /** Inserts a word into the trie. */
    public void insert(String word) {
        TrieNode thisRoot = root;
        char[] chars = word.toCharArray();
        for (char c : chars){
            if(thisRoot.nodes[c - 'a'] == null){
                thisRoot.nodes[c - 'a'] = new TrieNode();
            }
            thisRoot = thisRoot.nodes[c - 'a'];
        }
        thisRoot.isEnd = true;
    }
    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        TrieNode thisRoot = root;
        char[] chars = word.toCharArray();
        for (char c : chars){
            if(thisRoot.nodes[c - 'a'] == null){
                return false;
            }
            thisRoot = thisRoot.nodes[c - 'a'];
        }
        return thisRoot.isEnd;
    }
    
    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        TrieNode thisRoot = root;
        char[] chars = prefix.toCharArray();
        for (char c : chars){
            if(thisRoot.nodes[c - 'a'] == null){
                return false;
            }
            thisRoot = thisRoot.nodes[c - 'a'];
        }
        return true;
    }
    class TrieNode{
        public boolean isEnd = false;
        public TrieNode[] nodes = new TrieNode[26];
        public TrieNode(){
        }
    }
}