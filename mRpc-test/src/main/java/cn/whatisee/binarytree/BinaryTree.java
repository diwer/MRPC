package cn.whatisee.binarytree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class BinaryTree {
    private AtomicInteger nodeCount;
    private Node root;

    public void Add(int data) {
        Node node = new Node();
        node.setLevel(-1);
        searchAdd(root, node);
    }

    public void remove(int data) {
        searchDel(root, data);
    }

    public void printTree() {
        Queue<Node> nodeQueue = new LinkedList<Node>();
        nodeQueue.add(root);
        int level = 0;
        while (nodeQueue.size() != 0) {
            Node node = nodeQueue.poll();
            if (node.getLeft() != null)
                nodeQueue.add(node.getLeft());
            if (node.getRight() != null) {
                nodeQueue.add(node.getRight());
            }
            if (level != node.getLevel()) {
                System.out.print("\n");
                level = node.getLevel();
            }
            System.out.print("{" + node.data + " " + node.level + "}");
        }
    }

    private void searchAdd(Node root, Node node) {
        node.setLevel(node.getLevel() + 1);
        if (root == null) {
            root = node;
            return;
        }
        if (node.getData() > root.getData()) {
            searchAdd(root.getRight(), node);
        } else {

            searchAdd(root.getLeft(), node);
        }

    }

    private void searchDel(Node root, int data) {
        if (root.getRight().data == data) {
            Node node = searchLeft(root.getRight());
            root.setRight(node);
            deleteCirle(root.getRight());
        }

        if (data > root.getData()) {
            searchDel(root.getRight(), data);
        } else {
            searchDel(root.getLeft(), data);
        }


    }

    private void deleteCirle(Node right) {

    }

    private Node searchLeft(Node root) {
        if (root.getLeft() == null && root.getRight() == null)
            return root;
        return searchLeft(root.left);
    }

    class Node {
        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        private int level;
        private int data;
        private Node left;
        private Node right;

    }
}

    
    
    
    
    
    
    
    
    
    