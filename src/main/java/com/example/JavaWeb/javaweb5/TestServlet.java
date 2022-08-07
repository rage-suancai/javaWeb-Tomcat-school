package com.example.JavaWeb.javaweb5;

import com.example.JavaWeb.javaweb5.entity.User;
import com.example.JavaWeb.javaweb5.mapper.UserMapper;
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
 * 使用POST请求完成登录
 * 我们前面已经了解了如何使用Servlet来处理HTTP请求 那么现在 我们就结合前端 来实现一下登录操作
 *
 * 我们需要改一下我们的Servlet 现在我们要让其能够接收一个POST请求:
 *               @Override
 *               protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 *                      req.getParameterMap().forEach((k, v) -> {
 *                      System.out.println(k + ": " + Arrays.toString(v));
 *                      });
 *               }
 * parameterMap超出了我们发送的POST请求所携带的表单数据 我们可以直接将其遍历查看 浏览器发送了什么数据
 *
 * 现在我们再来改一下前端:
 *              <h1>登录到系统</h1>
 *              <form method="post" action="login">
 *                  <hr>
 *                  <div>
 *                      <label>
 *                          <input name="username" type="text" placeholder="用户名">
 *                      </label>
 *                  </div>
 *                  <div>
 *                      <label>
 *                          <input name="password" type="password" placeholder="密码" >
 *                      </label>
 *                  </div>
 *                  <div>
 *                      <input type="submit">
 *                  </div>
 *              </form>
 * 通过修改form标签的属性 现在我们点击登录按钮 会自动向后台发送一个POST请求 请求地址为当前地址加login(注意不同路径的写法) 也就是我们上面编写的Servlet路径
 *
 * 运行服务器 测试后发现 在点击按钮后 确实向服务器发起了一个POST请求 并且携带了表单中文本框的数据
 * 现在我们根据已有的基础 将其与数据库打通 我们进行一个真正的用户登录操作 首先修改一下Servlet的逻辑:
 *              // 首先设置一下响应类型
 *              resp.setContentType("text/html;charset=UTF-8");
 *              // 获取POST请求携带的表单数据
 *              Map<String, String[]> map = req.getParameterMap();
 *              // 判断表单是否完整
 *              if (map.containsKey("username") && map.containsKey("password")) {
 *                  String username = req.getParameter("username");
 *                  String password = req.getParameter("password");
 *
 *                  try (SqlSession session = factory.openSession(true)) {
 *                      UserMapper mapper = session.getMapper(UserMapper.class);
 *                      User user = mapper.getUser(username, password);
 *                      if (user != null) {
 *                          resp.getWriter().write("用户: " + username + ", 登录成功");
 *                      } else {
 *                          resp.getWriter().write("用户名或密码不正确 请重试");
 *                      }
 *                  }
 *               //权限校验(待完善)
 *              } else {
 *                  resp.getWriter().write("错误 您的表单数据不完整");
 *              }
 *
 * 接下来我们再去编写Mybatis和数据库的依赖和配置文件 创建一个表 用于存放我们用于测试的用户的帐号和密码 这里相信你们已经很熟悉了 就不做详细介绍了
 *
 * 配置完成后 在我们的servlet的init方法中编写Mybatis初始化代码 因为它只需要初始化一次
 *              SqlSessionFactory factory;
 *              @SneakyThrows
 *              @Override
 *              public void init() throws ServletException {
 *                  factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
 *              }
 * 现在再去浏览器上进行测试吧
 * 注册界面其实是同理的 这里就不多做讲解了
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
        /*req.getParameterMap().forEach((k, v) -> {
            System.out.println(k + ": " + Arrays.toString(v));
        });

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
                    resp.getWriter().write("用户: " + username + ", 登录成功");
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
