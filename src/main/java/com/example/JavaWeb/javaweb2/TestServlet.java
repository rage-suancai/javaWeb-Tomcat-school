package com.example.JavaWeb.javaweb2;

import lombok.extern.java.Log;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 探究Servlet的生命周期
 * 我们已经了解了如何注册一个Servlet 那么我们接着来看看 一个Servlet是如何运行的
 *
 * 首先我们需要了解 Servlet中的方法各自是在什么时候被调用的 我们先来编写一个打印语句来看看:
 *              System.out.println("我是构造方法");
 * 我们首先启动一次服务器 然后访问我们定义的页面 然后再关闭服务器 得到如下的顺序:
 *              我是init -> 我是service -> 我是service(出现两次是因为浏览器请求了2次 是因为有一次是请求favicon.ico 浏览器通病) -> 我是destroy
 *
 * 我们可以多次尝试访问此页面 但是init和构造方法只会执行一次 而每次访问都会执行的是service方法 因此 一个service的生命周期为:
 *      > 首先执行构造方法完成Servlet初始化
 *      > Servlet初始化后调用init()方法
 *      > Servlet调用service()方法来处理客户端的请求
 *      > Servlet销毁前调用destroy()方法
 *      > 最后 Servlet是由JVM的垃圾回收器进行垃圾回收的
 * 现在我们发现 实际上在Web应用程序运行时 每当浏览器向服务器发起一个请求时 都会创建一个线程执行一次service方法 来让我们处理用户的请求 并将结果响应给用户
 *
 * 我们发现service方法中 还有两个参数 ServletRequest和ServletResponse 实际上 用户发起的HTTP请求 就被Tomcat服务器封装为了一个ServletRequest对象
 * 我们得到的其实是Tomcat服务器帮助我们创建的一个实现类 HTTP请求报文中的所有内容 都可以从ServletRequest对象中获取
 * 同理 ServletResponse就是我们需要返回给浏览器的HTTP响应报文实体类封装
 *
 * 那么我们来看看ServletRequest中有哪些内容 我们可以获取请求的一些信息:
 *              HttpServletRequest request = (HttpServletRequest) servletRequest;
 *
 *              System.out.println(request.getProtocol()); // 获取协议版本
 *              System.out.println(request.getRemoteAddr()); // 获取访问者的IP地址
 *              System.out.println(request.getMethod()); // 获取请求方法
 *              // 获取头部信息
 *              Enumeration<String> enumeration = request.getHeaderNames();
 *              while (enumeration.hasMoreElements()) {
 *                  String name = enumeration.nextElement();
 *                  System.out.println(name + ": " + request.getHeader(name));
 *              }
 * 我们发现 整个HTTP请求报文中的所有内容 都可以通过HttpServletRequest对象来获取 当然 它的作用肯定不仅仅是获取头部信息 我们还可以使用它来完成更多操作 后面会一一讲解
 *
 * 那么我们再来看看ServletResponse 这个是服务端的响应内容 我们可以在这里填写我们想发送给浏览器显示的内容:
 *              // 转换为HttpServletResponse(同上)
 *              HttpServletResponse response = (HttpServletResponse) servletResponse;
 *              // 设定内容类型以及编码格式(普通HTML文件使用text/html 之后会讲解文件传输)
 *              response.setHeader("Content-type", "text/html;charset=UTF-8");
 *              // 获取Writer直接写入内容
 *              response.getWriter().write("我是响应内容");
 *              // 所有内容写入完成之后 在发送给浏览器
 * 现在我们在浏览器中打开此页面 就能够收到服务器发来的响应内容了 其中 响应头部分 是由Tomcat帮助我们生成的一个默认响应头
 */
/*@Log
@WebServlet("/test")
public class TestServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        /*log.info("浏览器发起了一次请求");
        log.info(servletRequest.getClass().getName());
        log.info(servletResponse.getClass().getName());*/

        // 首先将其转换为HttpServletRequest(继承自ServletRequest 一般是此接口实现)
        /*HttpServletRequest request = (HttpServletRequest) servletRequest;

        System.out.println(request.getProtocol()); // 获取协议版本
        System.out.println(request.getRemoteAddr()); // 获取访问者的IP地址
        System.out.println(request.getMethod()); // 获取请求方法
        // 获取头部信息
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            System.out.println(name + ": " + request.getHeader(name));
        }

        // 转换为HttpServletResponse(同上)
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 设定内容类型以及编码格式(普通HTML文件使用text/html 之后会讲解文件传输)
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        // 获取Writer直接写入内容
        response.getWriter().write("<h1>我是响应内容</h1>");
        // 所有内容写入完成之后 在发送给浏览器
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
    }
}*/
