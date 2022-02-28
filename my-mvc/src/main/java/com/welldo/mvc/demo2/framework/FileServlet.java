package com.welldo.mvc.demo2.framework;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@WebServlet(urlPatterns = {"/favicon.ico","/static/*"})
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //读取当前请求路径
        ServletContext context = req.getServletContext();

        String urlPath = req.getRequestURI().substring(context.getContextPath().length());

        System.out.println(context.getContextPath());
        System.out.println(req.getContextPath());

        //获取真实文件路径
        String filePath = context.getRealPath(urlPath);
        if (filePath == null) {
            // 无法获取到路径:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path path = Paths.get(filePath);
        if (!path.toFile().isFile()) {
            // 文件不存在:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 根据文件名猜测Content-Type:
        String mime = Files.probeContentType(path);
        if (mime == null) {
            mime = "application/octet-stream";
        }

        resp.setContentType(mime);
        // 读取文件并写入Response:
        OutputStream output = resp.getOutputStream();

        //方法1
        // try (InputStream input = new BufferedInputStream(new FileInputStream(filePath))) {
        //     // input.transferTo(output);//java9 提供的api
        // }

        //方法2
        byte[] bytes = Files.readAllBytes(path);
        output.write(bytes);


        output.flush();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
