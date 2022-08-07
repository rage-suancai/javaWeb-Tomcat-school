package com.example.JavaWeb.javaweb11;

import com.example.JavaWeb.javaweb8.entity.User;
import com.example.JavaWeb.javaweb8.mapper.UserMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

/**
 * Session
 * 由于HTTP是无连接的 那么如何能够辨别当前的请求是来自哪个用户发起的呢 Session就是用来处理这种问题的
 * 每个用户的会话都会有一个自己的Session对象 来自同一个浏览器的所有请求 就属于同一个会话
 *
 * 但是HTTP协议是无连接的呀 那Session是如何做到辨别是否来自同一个浏览器呢 Session实际上是基于Cookie实现的 前面我们了解了Cookie
 * 我们知道 服务端可以将Cookie保存到浏览器 当浏览器下次访问时 就会带这些Cookie信息
 *
 * Session也利用了这一点 它会给浏览器设定一个叫做JSESSIONID的Cookie 值是一个随机的排列组合 而此Cookie就对应了你属于哪一个会话
 * 只要我们的浏览器携带此Cookie访问服务器 服务器就会通过Cookie的值进行辨别 得到对应的Session对象 因此 这样就可以追踪到底是哪一个浏览器在访问服务器
 *
 * 那么现在 我们在用户登录成功之后 将用户对象添加到Session中 只要是此用发起的请求 我们就可以从HttpServletRequest中读取到存储在会话中的数据:
 *                     HttpSession httpSession = req.getSession();
 *                     httpSession.setAttribute("user", user);
 *
 * 同时如果用户没有登录就去访问首页 那么我们将发送一个重定向请求 告诉用户 需要先进行登录才可以访问:
 *                      HttpSession session = req.getSession();
 *                      User user = (User) session.getAttribute("user");
 *                      if (user == null){
 *                          resp.sendRedirect("login");
 *                          return;
 *                      }
 * 在访问过程中 注意观察Cookie变化
 *
 * Session并不是永远都存在的 它有着自己的过期时间 默认时间为30分钟 若超过此时间 Session将丢失 我们可以在配置文件中修改过期时间:
 *                      <session-config>
 *                          <session-timeout>1</session-timeout>
 *                      </session-config>
 *
 * 我们也可以在代码中使用invalidate方法来使用Session立即失效:
 *                      session.invalidate();
 *
 * 现在 通过Session 我们就可以更好地控制用户对于资源的访问 只有完成登录的用户才有资格访问首页
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            String username = null;
            String password = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) username = cookie.getValue();
                if (cookie.getName().equals("password")) password = cookie.getValue();
            }
            if (username != null && password != null) {
                // 登录校验
                try (SqlSession session = factory.openSession(true)){
                    UserMapper mapper = session.getMapper(UserMapper.class);
                    User user = mapper.getUser(username, password);
                    if (user != null) {
                        HttpSession httpSession = req.getSession();
                        httpSession.setAttribute("user", user);
                        resp.sendRedirect("time");
                        return; // 直接返回
                    } else {
                        Cookie cookie_username = new Cookie("username", username);
                        cookie_username.setMaxAge(0);
                        Cookie cookie_password = new Cookie("password", password);
                        cookie_password.setMaxAge(0);

                        resp.addCookie(cookie_username);
                        resp.addCookie(cookie_password);
                    }
                }
            }
        }
        req.getRequestDispatcher("/").forward(req, resp); // 正常情况还是转发给默认的Servlet帮我们返回静态页面
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
                    if (map.containsKey("remember-me")) { // 若勾选了勾选框 那么此表单信息会保存到本地
                        Cookie cookie_username = new Cookie("username", username);
                        cookie_username.setMaxAge(30);
                        Cookie cookie_password = new Cookie("password", password);
                        cookie_password.setMaxAge(30);

                        resp.addCookie(cookie_username);
                        resp.addCookie(cookie_password);
                    }

                    HttpSession httpSession = req.getSession();
                    httpSession.setAttribute("user", user);
                    resp.sendRedirect("time");
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
