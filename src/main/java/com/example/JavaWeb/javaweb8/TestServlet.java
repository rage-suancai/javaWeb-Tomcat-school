package com.example.JavaWeb.javaweb8;

import com.example.JavaWeb.javaweb8.entity.User;
import com.example.JavaWeb.javaweb8.mapper.UserMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 重定向与请求转发
 * 当我们希望用户登录完成之后 直接跳转到网站的首页 那么这个时候 我们就可以使用重定向来完成 当浏览器收到一个重定向的响应时 会按照重定向给出的地址 再次向此地址发出请求
 *
 * 实现重定向很简单 只需要调用一个方法即可 我们修改一下登录成功后执行的代码:
 *              resp.sendRedirect("https://www.bilibili.com");
 * 调用后 响应的状态会被设置为302 并且响应头中添加了一个Location属性 此属性表示 需要重定向到哪一个网址
 *
 * 现在 如果我们成功登录那么服务器会发送给我们一个重定向响应 这时 我们的浏览器会去重新请求另一个网址 这样 我们在登录成功之后 就可以直接帮助用户跳转到用户首页了
 *
 * 那么我们接着来看请求转发 请求转发其实是一种服务器内部的跳转机制 我们知道 重定向会使得浏览器去重新请求一个页面 而请求转发则是服务器内部进行跳转
 * 它的目的是 直接将本次请求转发给其他Servlet进行处理 并由其他Servlet来返回结果 因此它是在进行内部的转发
 *              req.getRequestDispatcher("https://www.bilibili.com").forward(req, resp);
 * 现在 在登录成功的时候 我们将请求转发给处理时间的Servlet 注意: 这里的路径规则和之前的不同 我们需要填写Servlet上指明的路径
 * 并且请求转发只能转发到此应用程序内部的Servlet 不能转发给其他站点或是其他Web应用程序
 *
 * 现在再次进行登录操作 我们发现 返回结果为一个405页面 证明了 我们的请求现在是被另一个Servlet进行处理 并且请求的信息全部被转交给另一个Servlet 由于此Servlet不支持POST请求 因此返回405状态码
 *
 * 那么也就是说 该请求包括请求参数也一起被传递了 那么我们可以尝试获取以下POST请求的参数
 * 现在我们给此Servlet添加请求处理 直接转交给Get请求处理:
 *              @Override
 *              protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 *                  resp.sendRedirect("https://www.bilibili.com");
 *              }
 * 再次访问 成功得到结果 但是我们发现 浏览器只发起了一次请求 并没有再次请求新的URL 也就是说 这一次请求直接返回了请求转发的处理结果
 *
 * 那么 请求转发有什么好处呢 它可以携带数据!
 *              req.setAttribute("user: ", "我是请求转发前的数据");
 *              req.getRequestDispatcher("//time").forward(req, resp);
 *
 *              System.out.println(req.getAttribute("user"))
 * 通过setAttribute方法来给当前请求添加一个附加数据 在请求转发后 我们可以直接获取到该数据
 *
 * 重定向属于二次请求 因此无法使用这种方法来传递数据 那么 如果在重定向之间传递数据呢 我们可以使用即将要介绍的ServletContext对象
 *
 * 最后总结 两者的区别为:
 *      > 请求转发是一次请求 重定向是两次请求
 *      > 请求转发地址栏不会发生变化 重定向地址栏会发生改变
 *      > 请求转发可以共享请求参数 重定向之后 就获取不了共享参数了
 *      > 请求转发只能转发给内部的Servlet
 */
/*@WebServlet(value = "/login", loadOnStartup = 1)
public class TestServlet extends HttpServlet {

    SqlSessionFactory factory;
    @SneakyThrows
    @Override
    public void init() throws ServletException {
        factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 首先设置一下响应类型
        resp.setContentType("text/html;charset=UTF-8");
        // 获取POST请求携带的表单数据
        Map<String, String[]> map = req.getParameterMap();
        // 判断表单是否完整
        if (map.containsKey("username") && map.containsKey("password")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            try (SqlSession session = factory.openSession(true)) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                User user = mapper.getUser(username, password);
                if (user != null) {
                    //resp.sendRedirect("https://www.bilibili.com");
                    req.setAttribute("user", user);
                    req.getRequestDispatcher("//time").forward(req, resp);
                } else {
                    resp.getWriter().write("用户名或密码不正确 请重试");
                }
            }
            //权限校验(待完善)
        } else {
            resp.getWriter().write("错误 您的表单数据不完整");
        }
    }

}*/
