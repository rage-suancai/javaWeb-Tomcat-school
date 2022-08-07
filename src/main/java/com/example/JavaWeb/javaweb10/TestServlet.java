package com.example.JavaWeb.javaweb10;

import com.example.JavaWeb.javaweb8.entity.User;
import com.example.JavaWeb.javaweb8.mapper.UserMapper;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Cookie
 * 什么是Cookie 不是曲奇 它可以在浏览器中保存一些信息 并且在下次请求时 请求头中会携带这些信息
 *
 * 我们可以编写一个测试用例来看看:
 *              Cookie cookie = new Cookie("yxs", "yyds");
 *              resp.addCookie(cookie);
 *              resp.sendRedirect("time");
 *
 *              if (req.getCookies() != null) {
 *                  for (Cookie cookie : req.getCookies()) {
 *                      System.out.println(cookie.getName() + ": " + cookie.getValue());
 *                  }
 *              }
 * 我们一块观察一下 在HttpServletResponse中添加Cookie之后 浏览器的响应头中会包含一个Set-Cookie属性 同时 在重定向之后
 * 我们的请求头中 会携带此Cookie作为一个属性 同时 我们可以直接通过HttpServletRequest来快速获取有哪些Cookie信息
 *
 * 那么我们来看看 一个Cookie包含哪些信息:
 *      > name Cookie的名称 Cookie一旦创建 名称便不可改变
 *      > value Cookie的值 如果值为Unicode字符 需要为字符编码 如果为二进制数据 则需要使用BASE64编码
 *      > maxAge Cookie失效的时间 单位秒 如果为正数 则该Cookie在maxAge秒后失效 如果为负数 该Cookie为临时Cookie
 *               关闭浏览器即失效 浏览器也不会以任何形式保存该Cookie 如果为0 表示删除该Cookie 默认为-1
 *      > secure 该Cookie是否仅被使用安全协议传输 安全协议 安全协议有HTTPS SSL等 在网络上传输数据之前先将数据加密 默认为false
 *      > path Cookie的使用路径 如果设置为"/sessionWeb/" 则只有contextPath为"/sessionWeb"的程序可以访问该Cookie 如果设置为"/"
 *             则本域名下contextPath都可以访问该Cookie 注意: 最后一个字符必须为"/"
 *      > domain 可以访问该Cookie的域名 如果设置为".google.com" 则所有以"google.com"结尾的域名都可以访问该Cookie 注意: 第一个字符必须为"."
 *      > comment 该Cookie的用处说明 浏览器显示Cookie信息的时候显示该说明
 *      > version Cookie使用的版本号 0表示遵循Netscape的Cookie规范 1表示遵循W3C的RFC 2109规范
 * 我们发现 最关键其实是name value maxAge domain属性
 *
 * 那么我们来尝试修改一下maxAge来看看失效时间:
 *              cookie.setMaxAge(20);
 * 设定为60秒 我们可以直接看到 响应头为我们设定了20秒的过期时间 20秒内访问都会携带此Cookie 而超过20秒 Cookie消失
 *
 * 既然了解了Cookie的作用 我们就可以通过使用Cookie来实现记住我功能 我们可以将用户名和密码全部保存在Cookie中 如果访问我们的首页时携带了这些Cookie
 * 那么我们就可以中直接为用户进行登录 如果登录成功则直接跳转到首页 如果登录失败 则清理浏览器中的Cookie
 * 那么首先 我们先在前端页面的表单中添加一个勾选框:
 *              <div>
 *                  <lobel>
 *                      <input type="checkbox" placeholder="记住我" name="remember-me">
 *                      记住我
 *                  </lobel>
 *              </div>
 * 接着 我们在登陆成功时进行判断 如果用户勾选了记住我 那么就将Cookie存储到本地:
 *              if (map.containsKey("remember-me")) { // 若勾选了勾选框 那么此表单信息会保存到本地
 *                  Cookie cookie_username = new Cookie("user", username);
 *                  cookie_username.setMaxAge(30);
 *                  Cookie cookie_password = new Cookie("password", password);
 *                  cookie_password.setMaxAge(30);
 *
 *                  resp.addCookie(cookie_username);
 *                  resp.addCookie(cookie_password);
 *              }
 * 然后 我们修改一下默认的请求地址 现在一律通过"http://localhost:8080/yxs/login"进行登录 那么我们需要添加GET请求的相关处理:
 *              @Override
 *              protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 *                  Cookie[] cookies = req.getCookies();
 *                  if (cookies != null) {
 *                      String username = null;
 *                      String password = null;
 *                      for (Cookie cookie : cookies) {
 *                          if (cookie.getName().equals("username")) username = cookie.getValue();
 *                          if (cookie.getName().equals("password")) password = cookie.getValue();
 *                      }
 *                      if (username != null && password != null) {
 *                      // 登录校验
 *                          try (SqlSession session = factory.openSession(true)){
 *                              UserMapper mapper = session.getMapper(UserMapper.class);
 *                              User user = mapper.getUser(username, password);
 *                              if (user != null) {
 *                                  resp.sendRedirect("time");
 *                                  return; // 直接返回
 *                              }
 *                          }
 *                      }
 *                   }
 *                  req.getRequestDispatcher("/").forward(req, resp); // 正常情况还是转发给默认的Servlet帮我们返回静态页面
 *              }
 * 现在 30秒内都不需要登录 访问登录页面后 会直接跳转到time页面
 *
 * 现在已经离我们理想的页面越来越接近了 但是仍然有一个问题 就是我们的首页 无论是否登录 所有人都可以访问 那么 如何才可以实现只有登录之后才能访问呢 这就需要用到Session了
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
