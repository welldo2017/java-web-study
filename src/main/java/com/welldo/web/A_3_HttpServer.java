package com.welldo.web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * socket编程.
 * （注：代码不用仔细研究！！！）
 *
 * 0. "HTTP编程"是以客户端的身份去请求服务器资源。
 * （详情见java-base-study工程17章，todo 需要重新学习,地址 https://github.com/welldo2017/java-base-study)
 * 现在，我们需要以服务器的身份响应客户端请求，编写服务器程序来处理客户端请求通常就称之为Web开发。
 *
 *
 * 1.编写HTTP Server(见代码)
 * 一个 HTTP Server本质上是一个TCP服务器，我们先用多线程去实现一个 "TCP编程的 服务器"：(见代码)
 *
 * 在开发网络应用程序的时候，我们又会遇到Socket这个概念。
 * Socket是一个抽象概念，一个应用程序通过一个Socket来建立一个远程连接，而Socket内部通过TCP/IP协议把数据传输到网络：
 * (也就是说,socket封装了 tcp)
 *
 * ┌───────────┐                                   ┌───────────┐
 * │Application│                                   │Application│
 * ├───────────┤                                   ├───────────┤
 * │  Socket   │                                   │  Socket   │
 * ├───────────┤                                   ├───────────┤
 * │    TCP    │                                   │    TCP    │
 * ├───────────┤      ┌──────┐       ┌──────┐      ├───────────┤
 * │    IP     │<────>│Router│<─────>│Router│<────>│    IP     │
 * └───────────┘      └──────┘       └──────┘      └───────────┘
 * !!! Socket、TCP和部分IP的功能都是由操作系统提供的，Java提供的几个Socket相关的类就封装了操作系统提供的接口。
 *
 *
 * 2.各个版本的区别（不用仔细研究）
 * HTTP目前有多个版本，
 * 1.0(已经淘汰) :
 * 版本浏览器每次建立TCP连接后，只发送一个HTTP请求并接收一个HTTP响应，然后就关闭TCP连接。
 * 由于创建TCP连接本身就需要消耗一定的时间，所以1.0 版本很慢,
 *
 * 1.1(主流版本):   大部分Web服务器都基于HTTP/1.1协议
 * 因为1.0的缺点,因此，HTTP 1.1允许浏览器和服务器在同一个TCP连接上反复发送、接收多个HTTP请求和响应，
 * 这样就大大提高了传输效率。
 *
 * 2.0():
 * 我们注意到HTTP协议是一个请求-响应协议，它总是发送一个请求，然后接收一个响应。
 * 能不能一次性发送多个请求，然后再接收多个响应呢？
 * HTTP 2.0可以支持浏览器同时发出多个请求，但每个请求需要唯一标识，服务器可以不按请求的顺序返回多个响应，由浏览器自己把收到的响应和请求对应起来。
 * （我的理解是 1.1 每次只能发一个请求，2.0每次可以发多个请求，和同步异步没有关系。todo 需要确认）
 * （可以参考 https://www.cnblogs.com/heluan/p/8620312.html ）
 *
 * 3.0(实验阶段):
 * HTTP 3.0为了进一步提高速度，将抛弃TCP协议，改为使用无需创建连接的UDP协议，目前HTTP 3.0仍然处于实验阶段。
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */
public class A_3_HttpServer {
    //启动后，使用浏览器发送一次请求, http://local.liaoxuefeng.com:8080/
    //local.liaoxuefeng.com 被廖雪峰老师买了，并且指向了 127.0.0.1
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080); // 监听我们编写的HTTP Server的8080端口.
        System.out.println("server is running...");

        /*
        这里的for循环,不会无休止地转下去
        因为accept()方法:   监听此socket的连接并接受它的返回值。该方法会一直阻塞，直到建立连接为止
        相当于这一步,把for循环给卡住了
         */
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
            handle(input, output);//核心代码
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
    }

    /**
     * 只需要在handle()方法中，用Reader读取HTTP请求，用Writer发送HTTP响应，即可实现一个最简单的HTTP服务器
     * 这里的核心代码是，先读取HTTP请求，这里我们只处理GET /的请求。
     * 当读取到空行时，表示已读到连续两个\r\n，说明请求结束，可以发送响应。
     * 发送响应的时候，首先发送响应代码HTTP/1.0 200 OK
     * 然后，依次发送Header，
     * 发送完Header后，再发送一个空行标识Header结束，
     * 紧接着发送HTTP Body，在浏览器输入 http://local.liaoxuefeng.com:8080/ 就可以看到响应页面：
     */
    private void handle(InputStream input, OutputStream output) throws IOException {

        System.out.println("Process new http request...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

        boolean requestOk = false;
        String first = reader.readLine();
        if (first.startsWith("GET / HTTP/1.")) {
            System.out.println(first);      //GET / HTTP/1.1
            requestOk = true;
        }
        for (; ; ) {
            String header = reader.readLine();
            if (header.isEmpty()) { // 读取到空行时, HTTP Header读取完毕
                System.out.println("空行-----------");
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

            //Windows系统里面，每行结尾是“<回车><换行>”，即“\r\n”；
            writer.write("HTTP/1.0 200 OK\r\n");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            //发送完Header后，再发送一个空行标识Header结束
            // 空行标识Header和Body的分隔
            writer.write("\r\n");

            writer.write(data);
            writer.flush();
        }
    }
}