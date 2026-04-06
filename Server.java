import java.io.*;
import java.net.*;

public class Server {

    static User user = new User("yehia", "1234"); // بيانات ثابتة

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(3000);
            System.out.println("Server running...");

            while (true) {
                try {
                    Socket socket = server.accept();

                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                    );

                    BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
                    );

                    // =========================
                    // قراءة الهيدر
                    // =========================
                    String line;
                    int contentLength = 0;

                    while ((line = in.readLine()) != null && !line.isEmpty()) {
                        if (line.startsWith("Content-Length:")) {
                            String[] parts = line.split(":");
                            if (parts.length > 1) {
                                contentLength = Integer.parseInt(parts[1].trim());
                            }
                        }
                    }

                    // =========================
                    // قراءة البيانات (body)
                    // =========================
                    char[] body = new char[contentLength];
                    in.read(body);
                    String data = new String(body);

                    System.out.println("Data: " + data);

                    // =========================
                    // تحليل البيانات
                    // =========================
                    String username = "";
                    String password = "";

                    String[] params = data.split("&");

                    for (String param : params) {
                        String[] pair = param.split("=");

                        if (pair.length > 1) {
                            if (pair[0].equals("username")) {
                                username = pair[1];
                            } else if (pair[0].equals("password")) {
                                password = pair[1];
                            }
                        }
                    }

                    // =========================
                    // التحقق من البيانات
                    // =========================
                    String message;

                    if (user.checkLogin(username, password)) {
                        message = "<h1>Login Success</h1>";
                    } else {
                        message = "<h1>Login Failed</h1>";
                    }

                    // =========================
                    // الرد على المتصفح
                    // =========================
                    String response =
                        "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n\r\n" +
                        message;

                    out.write(response);
                    out.flush();

                    socket.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}