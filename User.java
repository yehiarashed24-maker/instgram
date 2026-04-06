public class User{
        private final  String getUsername;
    private final  String getpassword;

    public User(String username, String password) {
        this.getUsername = username;
        this.getpassword = password;
    }

    public boolean checkLogin(String u, String p) {
        return getUsername.equals(u) && getpassword.equals(p);
    }
}