package com.example.JavaWeb.javaweb6;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 上传和下载文件
 * 首先我们来看看比较简单的下载文件 首先将我们的图片文件放入到resource文件夹中 接着我们编写一个Servlet用于处理文件下载:
 *              @Override
 *              protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 *                  resp.setContentType("image/png");
 *                  OutputStream output = resp.getOutputStream();
 *                  InputStream input = Resources.getResourceAsStream("test.png");
 *              }
 *
 * 为了更加快速地编写IO代码 我们可以引入一个工具库:
 *              <dependency>
 *                  <groupId>commons-io</groupId>
 *                  <artifactId>commons-io</artifactId>
 *                  <version>2.6</version>
 *              </dependency>
 * 使用此类库可以快速完成IO操作:
 *              resp.setContentType("image/png");
 *              OutputStream output = resp.getOutputStream();
 *              InputStream input = Resources.getResourceAsStream("test.png");
 *              // 直接使用copy方法完成转换
 *              IOUtils.copy(input, output);
 *
 * 现在我们在前端页面添加一个链接 用于下载此文件:
 *              <hr>
 *              <a href="file" download="test.png">点我下载高清学习资源</a>
 * 下载文件搞定 那么如何上传一个文件呢
 *
 * 首先我们编写前端部分:
 *              <form method="post" action="file" enctype="multipart/form-data">
 *                  <div>
 *                      <input type="file" name="test-file">
 *                  </div>
 *                  <div>
 *                      <button>上传文件</button>
 *                  </div>
 *              </form>
 * 注意: 必须添加enctype="multipart/form-data" 来表示此表单用于文件传输
 *
 * 现在我们来修改一下Servlet代码:
 *              try (FileOutputStream stream = new FileOutputStream("绝对路径或者项目路径")){
 *                  Part part = req.getPart("test-file");
 *                  IOUtils.copy(part.getInputStream(), stream);
 *                  resp.setContentType("text/html;charset=UTF-8");
 *                  resp.getWriter().write("文件上传成功");
 *              }
 * 注意: 必须添加@MultipartConfig注解来表示此Servlet用于处理文件上传请求
 *
 * 现在我们再运行服务器 并将我们刚才下载的文件又上传给服务器
 */
/*@MultipartConfig
@WebServlet("/file")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");
        OutputStream output = resp.getOutputStream();
        InputStream input = Resources.getResourceAsStream("test.png");
        // 直接使用copy方法完成转换
        IOUtils.copy(input, output);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (FileOutputStream stream = new FileOutputStream("")){
            Part part = req.getPart("test-file");
            IOUtils.copy(part.getInputStream(), stream);
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("文件上传成功");
        }
    }

}*/
