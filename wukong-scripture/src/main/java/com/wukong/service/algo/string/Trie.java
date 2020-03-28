package com.wukong.service.algo.string;

public class Trie {
  private TrieNode root = new TrieNode('/'); // 存储无意义字符

  // 往Trie树中插入一个字符串
  public void insert(char[] text) {
    TrieNode thisRoot = root;
    for (char c : text){
      int index = c - 'a';
      if(thisRoot.nodes[index] == null){
        thisRoot.nodes[index] = new TrieNode(c);
      }
      thisRoot = thisRoot.nodes[index];
    }
    thisRoot.isEnd = true;
  }

  // 在Trie树中查找一个字符串
  public boolean find(char[] pattern) {
    TrieNode thisRoot = root;
    for (char c : pattern){
      int index = c - 'a';
      if(thisRoot.nodes[index] == null){
        return false;
      }
      thisRoot = thisRoot.nodes[index];
    }
    return thisRoot.isEnd;
  }

  public class TrieNode {
    public char data;
    public boolean isEnd = false;
    public TrieNode[] nodes = new TrieNode[26];

    public TrieNode(char c){
      this.data = c;
    }
  }
}