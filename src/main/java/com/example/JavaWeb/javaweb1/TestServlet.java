package com.example.JavaWeb.javaweb1;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * Servlet
 * 前面我们已经完成了基本的环境搭建 那么现在我们就可以开始来了解我们的第一个重要类 Servlet
 *
 * 它是javaEE的一个标准 大部分的Web服务器都支持此标准 包括Tomcat 就像之前的JDBC一样 由官方定义了一系列接口 而具体实现由我们来编写 最后交给Web服务器(如Tomcat)来运行我们编写的Servlet
 *
 * 那么 它能做什么呢 我们可以通过实现Servlet来进行动态网页响应 使用Servlet 不再是直接由Tomcat服务器发送我们编写好的静态网页内容(HTML文件) 而是由我们通过java代码进行动态拼接的结果 它能够很好地实现动态网页的返回
 * 当然 Servlet并不是专用与HTTP协议通信 也可以用于其他的通信 但是一般都是用于HTTP
 *
 * 创建Servlet
 * 那么如何创建一个Servlet呢 非常简单 我们只需要实现Servlet类即可 并添加注解@WebServlet来进行注册
 * 我们现在可以去访问一下我们的页面: http://localhost:8080/yxs/test
 * 我们发现 直接访问此页面是没有任何内容的 这是因为我们还没有为该请求方法编写实现 这里先不做讲解 后面我们会对浏览器的请求处理做详细的介绍
 *
 * 除了直接编写一个类 我们也可以在web.xml中进行注册 现将类上@WebServlet的注解去掉
 *              <servlet>
 *                  <servlet-name>test</servlet-name>
 *                  <servlet-Class>com.example.JavaWeb.javaweb1.TestServlet</servlet-Class>
 *              </servlet>
 *              <servlet-mapping>
 *                  <servlet-name>test</servlet-name>
 *                  <url-pattern>/test</url-pattern>
 *              </servlet-mapping>
 * 这样的方式也能注册Servlet 但是显然直接使用注解更加方便 因此之后我们一律使用注解进行开发 只有比较新的版本才支持此注解 老的版本是不支持的
 *
 * 实际上 Tomcat服务器会为我们提供一些默认的Servlet 也就是说在服务器启动后 即使我们什么都不编写 Tomcat也自带了几个默认的Servlet 他们编写在conf目录下的web.xml中
 * 我们发现 默认的Servlet实际上可以帮助我们去访问一些静态资源 这也是为什么我们启动Tomcat服务器之后 能够直接访问webapp目录下的静态页面
 *
 * 我们可以将之前编写的页面放入到webapp目录下 来测试一下是否能够直接访问
 */
/*@WebServlet("/test")
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

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}*/
