package com.litovchenko.carsapp.controller;

import com.litovchenko.carsapp.model.User;
import com.litovchenko.carsapp.service.LoginService;
import com.litovchenko.carsapp.service.UsersServise;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch(req.getParameter("login")){
            case "login": {
                req.getRequestDispatcher("Login.jsp").forward(req, resp);
                break;
            }
            case "register": {
                req.getRequestDispatcher("Registration.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = null;
        switch (req.getParameter("action")){
            case "login": {
                user = UsersServise.logIn(req.getParameter("login"),
                        LoginService.hashPassword(req.getParameter("password")));
                if(user != null){
                    req.getSession().setAttribute("user", user);
                    resp.sendRedirect("/MainPageServlet");
                } else {
                    req.setAttribute("msg", true);
                    req.getRequestDispatcher("Login.jsp").forward(req, resp);
                }
                break;
            }
            case "register": {

                break;
            }
            case "logout": {
                req.getSession().setAttribute("user", null);
                resp.sendRedirect("/MainPageServlet");
            }
        }
    }
}