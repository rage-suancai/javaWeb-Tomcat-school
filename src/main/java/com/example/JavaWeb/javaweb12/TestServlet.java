package com.example.JavaWeb.javaweb12;

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
 * Filter
 * 有了Session之后 我们就可以很好地控制用户的登录验证了 只有授权的用户 才可以访问一些页面 但是我们需要一个一个去进行配置 还是太过麻烦 能否一次性地过滤掉没有登录验证的用户呢
 *
 * 过滤器相当于在所有访问前加了一堵墙 来自浏览器的所有访问请求都会首先经过过滤器 只有过滤器允许通过的请求 才可以顺利地到达对应的Servlet 而过滤器不允许通过的请求
 * 我们可以自由地进行控制是否进行重定向或是请求转发 并且过滤器可以添加很多个 就相当于添加了很多堵墙 我们的请求只有穿过层层阻碍 才能与Servlet相拥 像极了爱情
 *
 * 添加一个过滤器非常简单 只需要实现Filter接口 并添加@WebFilter注解即可:
 *                  @WebFilter("/*")
 *                  public class TestFilter implements Filter {
 *                      @Override
 *                      public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
 *
 *                      }
 *                  }
 * 这样我们就成功地添加了一个过滤器 那么添加一句打印语句看看 是否所有的请求都会经过此过滤器:
 *                  HttpServletRequest request = (HttpServletRequest) servletRequest;
 *                  System.out.println(request.getRequestURL());
 * 我们发现 现在我们发起的所有请求 一律需要经过此过滤器 并且所有的请求都没有任何的响应内容
 *
 * 那么如何让请求可以顺利地到达对应的Servlet 也就是说怎么让这个请求顺利通过呢 我们只需要在最后添加一句:
 *                  filterChain.doFilter(servletRequest, servletResponse)
 * 那么这行代码是什意思呢
 *
 * 由于我们整个应用程序可能存在多个过滤器 那么这行代码意思实际上是将此请求继续传递给下一个过滤器 当没有下一个过滤器时
 * 才会到达对应的Servlet进行处理 我们可以再来创建一个过滤器看看效果:
 *                  @WebFilter("/*")
 *                  public class TestFilter2 implements Filter {
 *                      @Override
 *                      public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
 *                          System.out.println("我是过滤器2");
 *                          filterChain.doFilter(servletRequest, servletResponse);
 *                      }
 *                  }
 * 由于过滤器的过滤顺序是按照类名的自然排序进行的 因此我们将第一个过滤器命名进行调整
 *
 * 我们发现 在经过一个过滤器之后 会继续前往第二个过滤器 只有两个过滤器全部经过之后 才会到达我们的Servlet中
 *
 * 实际上 当doFilter方法调用时 就好一直向下直到Servlet 在Servlet处理完成之后 又依次返回到最前面的Filter 类似于递归的结构 我们添加几个输出语句来判断一下:
 *                  System.out.println("我是过滤器2");
 *                  filterChain.doFilter(servletRequest, servletResponse);
 *                  System.out.println("我是2号过滤器 处理后");
 *
 *                  System.out.println("我是过滤器1");
 *                  filterChain.doFilter(servletRequest, servletResponse);
 *                  System.out.println("我是1号过滤器 处理后");
 * 最后验证我们的结论
 *
 * 同Servlet一样 Filter也有对应的HttpFilter专用类 它针对于HTTP请求进行了专门处理 因此我们可以直接使用HttpFilter来编写:
 *                  @WebFilter("/*")
 *                  public class MainFilter extends HttpFilter {
 *                      @Override
 *                      protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
 *                          String url = request.getRequestURL().toString();
 *                          if (!url.endsWith(".js") && !url.endsWith(".css") && !url.endsWith(".png")) {
 *                              HttpSession session = request.getSession();
 *                              User user = (User) session.getAttribute("user");
 *                              if (user == null && !url.endsWith("login")) {
 *                                  response.sendRedirect("login");
 *                                  return;
 *                              }
 *                          }
 *                          chain.doFilter(request, response);
 *                      }
 *                  }
 * 现在 我们的页面已经基本完善为我们想要的样子了
 *
 * 当然 可能跟着教程编写项目比较乱 大家可以自己花费一点时间来重新编写一个Web应用程序 加深之前讲解的知识理解 我们也会在之后安排一个编程实战进行深化练习
 */
@WebServlet(value = "/login", loadOnStartup = 1)
public class TestServlet extends HttpServlet {

    SqlSessionFactory factory;
    @SneakyThrows
    @Override
    public void init() throws ServletException {
        factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println("我是Servlet");
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

}
