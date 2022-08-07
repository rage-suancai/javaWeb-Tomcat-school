package com.example.JavaWeb.javaweb4;

import lombok.extern.java.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WebServlet注解详解
 * 我们接着来看WebServlet注解 我们前面已经得知 可以注解使用此注解来快速注册一个Servlet 那么我们来详细看看此注解还有什么其他的玩法
 *
 * 首先name属性就是Servlet名称 而urlPatterns和value实际上是同样功能 就是代表当前Servlet的访问路径 它不仅仅可以是一个固定值 还可以进行通配符匹配:
 *              @WebServlet("/test/*")
 * 上面的路径表示 所有匹配/test/随便上面的路径名称 都可以访问此Servlet 我们可以在浏览器中尝试一下
 *
 * 也可以进行某个扩展名的匹配:
 *              @WebServlet("*.js")
 * 这样的话 获取如何以js结尾的文件 都会由我们自己定义的Servlet处理
 *
 * 那么如果我们的路径为/呢
 *              @WebServlet("/")
 * 此路径和Tomcat默认为我们提供的Servlet冲突 会直接替换掉默认的 而使用我们的 此路径的意思为 如果没有找到匹配当前访问路径的Servlet 那么就会使用此Servlet进行处理
 *
 * 我们还可以为一个Servlet配置多个访问路径:
 *              @WebServlet({"/test1", "/test2"})
 *
 * 我们接着来看loadOnStartup属性 此属性决定了是否在Tomcat启动时就加载此Servlet 默认情况下 Servlet只有在被访问时才会加载 它的默认值为-1 表示不在启动时加载
 * 我们可以将其改为大于等于0的数 来开启启动时加载 并且数字的大小决定了此Servlet的启动优先级(值小的优先加载)
 *              @WebServlet(value = "/test", loadOnStartup = 1)
 *              public class TestServlet extends HttpServlet{
 *
 *                  @Override
 *                  public void init() throws ServletException {
 *                      System.out.println("我被加载了");
 *                  }
 *              }
 *
 * 其他内容都是Servlet的一些基本配置 这里就不详细讲解了
 */
/*@Log
//@WebServlet("/test/*")
@WebServlet(value = "/test", loadOnStartup = 1)
public class TestServlet extends HttpServlet{

    @Override
    public void init() throws ServletException {
        System.out.println("我被加载了");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("<h1>恭喜你解锁了全新玩法</h1>");
    }
}*/
