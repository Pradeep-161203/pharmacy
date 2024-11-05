import java.io.*;

public class UserBST {
    private UserNode root;

    public UserBST() {
        root = null;
    }

    public boolean insert(User user) {
        if (root == null) {
            root = new UserNode(user);
            return true;
        } else {
            return insertRec(root, user);
        }
    }

    private boolean insertRec(UserNode node, User user) {
        if (user.getEmail().equals(node.user.getEmail())) {
            return false; // Email already exists
        }
        if (user.getEmail().compareTo(node.user.getEmail()) < 0) {
            if (node.left == null) {
                node.left = new UserNode(user);
                return true;
            } else {
                return insertRec(node.left, user);
            }
        } else {
            if (node.right == null) {
                node.right = new UserNode(user);
                return true;
            } else {
                return insertRec(node.right, user);
            }
        }
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            saveRec(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRec(UserNode node, BufferedWriter writer) throws IOException {
        if (node != null) {
            saveRec(node.left, writer);
            writer.write(node.user.getUsername() + "," + node.user.getPassword() + "," + node.user.getEmail());
            writer.newLine();
            saveRec(node.right, writer);
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    User user = new User(parts[0], parts[1], parts[2]);
                    insert(user); // Insert the user into the BST
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printUsers() {
        printUsersRec(root);
    }

    private void printUsersRec(UserNode node) {
        if (node != null) {
            printUsersRec(node.left);
            System.out.println("Username: " + node.user.getUsername() + ", Email: " + node.user.getEmail());
            printUsersRec(node.right);
        }
    }
}
