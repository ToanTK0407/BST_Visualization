/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bstvisualization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author CE191239 Nguyen Kim Bao Nguyen, CE190173 Truong Khai Toan
 */
public class BSTTree implements Serializable {
    BSTNode root;
    ArrayList<BSTNode> path;

    public BSTTree() {
        path = new ArrayList<>();
    }

    public static BSTNode insert(BSTNode node, int data) {
        if (node == null) {
            return new BSTNode(data);
        } else {
            if (data < node.data) {
                node.left = insert(node.left, data);
            } else if (data > node.data) {
                node.right = insert(node.right, data);
            }
        }
        return node;
    }

    public BSTNode createNode(int data) {
        if (root == null) {
            root = new BSTNode();
            root.level = 0;
            root.data = data;
            root.parent = null;
            root.index = 0;
            root.x = 473;
            root.y = 25;
            return root;
        }
        return insert(root, data, 0);
    }

    // Dung de quy them vao mot nut
    private BSTNode insert(BSTNode node, int data, int level) {
        if (node == null) {
            BSTNode newNode = new BSTNode();
            newNode.data = data;
            newNode.level = level;
            return newNode;
        }

        if (data < node.data) {
            BSTNode leftChild = insert(node.left, data, level + 1);
            node.left = leftChild;
            leftChild.parent = node;
            leftChild.index = node.index * 2 + 1;
        } else if (data > node.data) {
            BSTNode rightChild = insert(node.right, data, level + 1);
            node.right = rightChild;
            rightChild.parent = node;
            rightChild.index = node.index * 2 + 2;
        } else {
            node.count++;
        }
        return node;
    }

    // Dung de quy tim kiem muc tieu
    public BSTNode search(BSTNode currentNode, int target) {
        if (currentNode == null) {
            return null;
        }
        path.add(currentNode);
        if (currentNode.data == target) {
            return currentNode;
        } else if (currentNode.data < target) {
            return search(currentNode.right, target);
        } else {
            return search(currentNode.left, target);
        }
    }

    public BSTNode delete(BSTNode currentNode, int target) {
        if (currentNode == null) {
            return null;
        }
        path.add(currentNode);
        if (target > currentNode.data) {
            currentNode.right = delete(currentNode.right, target);
        } else if (target < currentNode.data) {
            currentNode.left = delete(currentNode.left, target);
        } else {
            if (currentNode.left == null) {
                return currentNode.right;
            } else if (currentNode.right == null) {
                return currentNode.left;
            }
            // Tìm node nhỏ nhất ở cây con phải và xóa nó
            currentNode.data = minValueNode(currentNode.right).data;
            currentNode.right = delete(currentNode.right, currentNode.data);
        }
        return currentNode;
    }

    public BSTNode minValueNode(BSTNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public BSTNode maxValueNode(BSTNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public void clearData() {
        root = null;
    }

    public int randomNode() {
        return (int) (Math.random() * 101);
    }

    void inOrder(BSTNode node) {
        if (node == null)
            return;

        inOrder(node.left);
        path.add(node);
        System.out.println(node.data);
        inOrder(node.right);
    }

    void preOrder(BSTNode node) {
        if (node == null)
            return;
        path.add(node);
        System.out.println(node.data);
        preOrder(node.left);
        preOrder(node.right);
    }

    void posOrder(BSTNode node) {
        if (node == null)
            return;
        posOrder(node.left);
        posOrder(node.right);
        path.add(node);
        System.out.println(node.data);
    }

    public ArrayList<BSTNode> getNodesInBFSOrder() {
        ArrayList<BSTNode> nodes = new ArrayList<>();
        Queue<BSTNode> queue = new LinkedList<>();
        if (root != null)
            queue.add(root);

        while (!queue.isEmpty()) {
            BSTNode current = queue.poll();
            nodes.add(current);
            if (current.left != null)
                queue.add(current.left);
            if (current.right != null)
                queue.add(current.right);
        }
        return nodes;
    }

    int height(BSTNode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int getBalance(BSTNode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    public BSTNode balanceTree(BSTNode node) {
        if (node == null)
            return null;

        // Cân bằng cây con trái và phải
        node.left = balanceTree(node.left);
        if (node.left != null) {
            node.left.parent = node; // Cập nhật cha của cây con trái
        }

        node.right = balanceTree(node.right);
        if (node.right != null) {
            node.right.parent = node; // Cập nhật cha của cây con phải
        }

        // Cập nhật chiều cao
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Kiểm tra balance factor
        int balance = getBalance(node);

        // Xử lý 4 trường hợp mất cân bằng
        // Left Left
        if (balance > 1 && getBalance(node.left) >= 0) {
            BSTNode newRoot = rightRotate(node);
            newRoot.parent = node.parent; // Cập nhật cha của newRoot
            return newRoot;
        }
        // Left Right
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            node.left.parent = node; // Cập nhật cha sau phép quay trái
            BSTNode newRoot = rightRotate(node);
            newRoot.parent = node.parent;
            return newRoot;
        }
        // Right Right
        if (balance < -1 && getBalance(node.right) <= 0) {
            BSTNode newRoot = leftRotate(node);
            newRoot.parent = node.parent;
            return newRoot;
        }
        // Right Left
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            node.right.parent = node; // Cập nhật cha sau phép quay phải
            BSTNode newRoot = leftRotate(node);
            newRoot.parent = node.parent;
            return newRoot;
        }

        return node;
    }

    // Hàm quay phải (cập nhật parent)
    private BSTNode rightRotate(BSTNode y) {
        BSTNode x = y.left;
        BSTNode T2 = x.right;

        // Thực hiện quay
        x.right = y;
        y.left = T2;

        // Cập nhật parent
        x.parent = y.parent; // x thay thế vị trí của y
        y.parent = x; // y trở thành con của x
        if (T2 != null) {
            T2.parent = y; // Cập nhật parent cho T2
        }

        // Cập nhật chiều cao
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Hàm quay trái (cập nhật parent)
    private BSTNode leftRotate(BSTNode x) {
        BSTNode y = x.right;
        BSTNode T2 = y.left;

        // Thực hiện quay
        y.left = x;
        x.right = T2;

        // Cập nhật parent
        y.parent = x.parent; // y thay thế vị trí của x
        x.parent = y; // x trở thành con của y
        if (T2 != null) {
            T2.parent = x; // Cập nhật parent cho T2
        }

        // Cập nhật chiều cao
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public void balanceEntireTree() {
        root = balanceTree(root);
    }

    // Tính chiều cao cây
    public int getHeight(BSTNode node) {
        if (node == null)
            return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString().trim();
    }

    private void serializeHelper(BSTNode node, StringBuilder sb) {
        if (node == null)
            return;
        sb.append(node.data).append(" ");
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    public void deserialize(String data) {
        clearData();
        String[] values = data.split(" ");
        for (String value : values) {
            try {
                int num = Integer.parseInt(value);
                this.createNode(num);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ");
            }
        }
    }
}
