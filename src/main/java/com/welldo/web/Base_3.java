package com.welldo.web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * web开发
 * （注：代码不用仔细研究）
 *
 * 0. HTTP编程是以客户端的身份去请求服务器资源。
 * （详情见java-base-study todo 需要重新学习）
 * 现在，我们需要以服务器的身份响应客户端请求，编写服务器程序来处理客户端请求通常就称之为Web开发。
 *
 *
 * 1.如何编写HTTP Server。
 * 一个 HTTP Server本质上是一个TCP服务器，我们先用TCP编程的多线程实现的服务器端框架：
 *
 *
 * 2.各个版本的区别（不用仔细研究）
 * HTTP目前有多个版本，
 * 1.0 版本浏览器每次建立TCP连接后，只发送一个HTTP请求并接收一个HTTP响应，然后就关闭TCP连接。
 * 由于创建TCP连接本身就需要消耗一定的时间，
 * 因此，HTTP 1.1允许浏览器和服务器在同一个TCP连接上反复发送、接收多个HTTP请求和响应，这样就大大提高了传输效率。
 *
 * 我们注意到HTTP协议是一个请求-响应协议，它总是发送一个请求，然后接收一个响应。
 * 能不能一次性发送多个请求，然后再接收多个响应呢？
 * HTTP 2.0可以支持浏览器同时发出多个请求，但每个请求需要唯一标识，服务器可以不按请求的顺序返回多个响应，由浏览器自己把收到的响应和请求对应起来。
 * （我的理解是 1.1 每次只能发一个请求，2.0每次可以发多个请求，和同步异步没有关系。todo 需要确认）
 * （可以参考 https://www.cnblogs.com/heluan/p/8620312.html ）
 *
 *
 *
 *
 * 4.HTTP协议运行在TCP之上，所有传输的内容都是明文，
 * HTTPS运行在SSL/TLS之上，SSL/TLS运行在TCP之上，所有传输的内容都经过加密的。
 * 前者是80，后者是443。
 * http ===> tcp
 * https ===> SSL/TLS(加密解密) ===> tcp
 *
 *
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */
public class Base_3 {
    //启动后，请求 http://local.liaoxuefeng.com:8080/ 即可
    //local.liaoxuefeng.com 被廖雪峰老师买了，并且指向了 127.0.0.1
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080); // 监听指定端口
        System.out.println("server is running...");
        for (; ; ) {
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        try (InputStream input = this.sock.getInputStream();
             OutputStream output = this.sock.getOutputStream()) {
            handle(input, output);
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {

        System.out.println("Process new http request...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

        boolean requestOk = false;
        String first = reader.readLine();
        if (first.startsWith("GET / HTTP/1.")) {
            requestOk = true;
        }
        for (; ; ) {
            String header = reader.readLine();
            if (header.isEmpty()) { // 读取到空行时, HTTP Header读取完毕
                break;
            }
            System.out.println(header);
        }
        System.out.println(requestOk ? "Response OK" : "Response Error");

        if (!requestOk) {
            // 发送错误响应:
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        } else {
            // 发送成功响应:
            String data = "<html><body><h1>Hello, world!</h1></body></html>";
            int length = data.getBytes(StandardCharsets.UTF_8).length;
            writer.write("HTTP/1.0 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            writer.write("\r\n"); // 空行标识Header和Body的分隔
            //Windows系统里面，每行结尾是“<回车><换行>”，即“\r\n”；
            writer.write(data);
            writer.flush();
        }
    }
}