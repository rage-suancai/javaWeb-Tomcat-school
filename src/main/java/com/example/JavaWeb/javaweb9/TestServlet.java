package com.example.JavaWeb.javaweb9;

import com.example.JavaWeb.javaweb8.entity.User;
import com.example.JavaWeb.javaweb8.mapper.UserMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 了解ServletContext对象
 * ServletContext全局唯一 它是属于整个Web应用程序的 我们可以通过getServletContext()来获取到此对象
 *
 * 此对象也能设置附加值:
 *              ServletContext context = getServletContext();
 *              context.setAttribute("text", "我是重定向之前的数据")
 *
 *              System.out.println(getServletContext().getAttribute("test"));
 * 因为无论在哪里 无论什么时间 获取到ServletContext始终是同一个对象 因此我们可以随时随地获取我们添加的属性
 *
 * 它不仅仅可以用来进行数据传递 还可以做一些其他的事情 比如请求转发:
 *              context.getRequestDispatcher("/time"),forward(req, resp);
 *
 * 它还可以获取根目录下的资源文件(注意: 是webapp根目录下的 不是resource中的资源)
 *              System.out.println(IOUtils.toString(context.getResourceAsStream("index.html"), StandardCharsets.UTF_8));
 *
 *              System.out.println(IOUtils.resourceToString("/mybatis.config.xml", StandardCharsets.UTF_8));
 *
 * 初始化参数
 * 初始化参数类似于初始化配置需要的一些值 比如我们的数据库连接相关信息 就可以通过初始化参数来给予Servlet 或是一些其他的配置项 也可以使用初始化初始来实现
 *
 * 我们给一个Servlet添加一些初始化参数:
 *              @WebServlet(value = "/login", initParams = {
 *                      @WebInitParam(name = "test", value = "我是一个默认的初始化参数")
 *              })
 * 它也是以键值对形式保存的 我们可以直接通过Servlet的getInitParameter方法获取:
 *              System.out.println(getInitParameter("test"));
 *
 * 但是 这里的初始化初始仅仅是针对于此Servlet 我们也可以定义全局初始化参数 在需要在web.xml编写即可:
 *              <context-param>
 *                  <param-name>yxsnb</param-name>
 *                  <param-value>我是全局初始化参数</param-value>
 *              </context-param>
 * 我们需要使用ServletContext来读取全局初始化参数:
 *              ServletContext context = getServletContext();
 *              System.out.println(context.getInitParameter("yxsnb));
 *
 * 有关ServletContext其他的内容 我们需要完成后面的内容学习 才能理解
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
                    ServletContext context = getServletContext();
                    System.out.println(IOUtils.toString(context.getResourceAsStream("index.html"), StandardCharsets.UTF_8));
                    System.out.println(IOUtils.resourceToString("/mybatis.config.xml", StandardCharsets.UTF_8));
                    context.getRequestDispatcher("/time").forward(req, resp);
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
