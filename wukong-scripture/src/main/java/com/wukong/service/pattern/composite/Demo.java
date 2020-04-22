package com.wukong.service.pattern.composite;

/**
 * 组合模式
 * 将一组对象（文件和目录）组织成树形结构，以表示一种‘部分 - 整体’的层次结构（目录与子目录的嵌套结构）。
 * 组合模式让客户端可以统一单个对象（文件）和组合对象（目录）的处理逻辑（递归遍历）。”
 */
public class Demo {
  public static void main(String[] args) {
    /**
     * /
     * /wz/
     * /wz/a.txt
     * /wz/b.txt
     * /wz/movies/
     * /wz/movies/c.avi
     * /xzg/
     * /xzg/docs/
     * /xzg/docs/d.txt
     */
    Directory fileSystemTree = new Directory("/");
    Directory node_wz = new Directory("/wz/");
    Directory node_xzg = new Directory("/xzg/");
    fileSystemTree.addSubNode(node_wz);
    fileSystemTree.addSubNode(node_xzg);

    File node_wz_a = new File("/wz/a.txt");
    File node_wz_b = new File("/wz/b.txt");
    Directory node_wz_movies = new Directory("/wz/movies/");
    node_wz.addSubNode(node_wz_a);
    node_wz.addSubNode(node_wz_b);
    node_wz.addSubNode(node_wz_movies);

    File node_wz_movies_c = new File("/wz/movies/c.avi");
    node_wz_movies.addSubNode(node_wz_movies_c);

    Directory node_xzg_docs = new Directory("/xzg/docs/");
    node_xzg.addSubNode(node_xzg_docs);

    File node_xzg_docs_d = new File("/xzg/docs/d.txt");
    node_xzg_docs.addSubNode(node_xzg_docs_d);

    System.out.println("/ files num:" + fileSystemTree.countNumOfFiles());
    System.out.println("/wz/ files num:" + node_wz.countNumOfFiles());
  }
}