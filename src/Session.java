// Session.java
public class Session {
    private static String currentUsername;
    private static String currentRole;

    public static void setCurrentUser(String username, String role) {
        currentUsername = username;
        currentRole = role;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    public static void clear() {
        currentUsername = null;
        currentRole=null;
    }
}