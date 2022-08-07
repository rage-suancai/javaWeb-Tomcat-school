package com.example.JavaWeb.javaweb12;

import com.example.JavaWeb.javaweb8.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class MainFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = request.getRequestURL().toString();
        if (!url.endsWith(".js") && !url.endsWith(".css") && !url.endsWith(".png")) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null && !url.endsWith("login")) {
                response.sendRedirect("login");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
