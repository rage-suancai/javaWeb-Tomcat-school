package com.example.JavaWeb.javaweb7;

import com.example.JavaWeb.javaweb8.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 使用XHR请求数据
 * 现在我们希望 网页中的部分内容 可以动态显示 比如网页上有一个时间 旁边有一个按钮 点击按钮就可以刷新当前时间
 *
 * 这个时候就需要我们在网页展示时向后端发起请求了 并根据后端响应的结果 动态地更新页面中的内容 要实现此功能
 * 就需需要用到javaScript来帮助我们 首先在js中编写我们的XHR请求 并在请求中完成动态更新:
 *              function updateTime() {
 *                  let xhr = new XMLHttpRequest();
 *                  xhr.onreadystatechange = function() {
 *                      if (xhr.readyState === 4 && xhr.status === 200) {
 *                          document.getElementById("time").innerText = xhr.responseText
 *                      }
 *                  };
 *                  xhr.open('GET', 'time', true);
 *                  xhr.send();
 *              }
 *
 * 接着修改一下前端页面 添加一个时间显示区域:
 *              <div id="time"></div>
 *              <br>
 *              <button onclick="updateTime()">更新数据</button>
 *              <script>
 *                  updateTime()
 *              </script>
 *
 * 最后创建一个Servlet用于处理时间更新请求:
 *              @Override
 *              protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 *                  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
 *                  String date = dateFormat.format(new Date());
 *                  resp.setContentType("text/html;charset=UTF-8");
 *                  resp.getWriter().write(date);
 *              }
 * 现在点击按钮就可以更新了
 *
 * GET请求也能传递参数 这里做一下演示
 *              xhr.open('GET', 'time?username=yxs&password=123456', true);
 */
/*@WebServlet("/time")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null){
            resp.sendRedirect("login");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String date = dateFormat.format(new Date());
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(date + "欢迎您: " + user.getUsername());

        /*req.getParameterMap().forEach((k, v) -> {
            System.out.println(k + ": " + Arrays.toString(v));
        });*/

        //System.out.println(getServletContext().getAttribute("test"));

        /*if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //resp.sendRedirect("https://www.bilibili.com");
        // this.doGet(req, resp);

        User user = (User) req.getAttribute("user");
        resp.setContentType("text/html:charset=UTF-8");
        resp.getWriter().write(user.getUsername() + "登陆成功");
    }
}*/
