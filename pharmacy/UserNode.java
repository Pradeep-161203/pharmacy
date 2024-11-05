public class UserNode {
    User user;        // The user object that this node holds
    UserNode left;    // Pointer to the left child
    UserNode right;   // Pointer to the right child

    // Constructor to create a new UserNode with a User object
    public UserNode(User user) {
        this.user = user; // Assign the user object to this node
        left = null;      // Initialize left child to null
        right = null;     // Initialize right child to null
    }
}
