package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    private PostController controller;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String MATCH = "/api/posts/\\d+";
    private static final String API_POSTS = "/api/posts";
    private final AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext("ru.netology");

    @Override
    public void init() {
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            long id = 0;
            if (path.matches(MATCH)){
                id = Long.parseLong(path.substring(path.lastIndexOf("/")+1));
            }

            switch (method){
                case GET:
                    if (path.equals(API_POSTS)){
                        controller.all(resp);
                        return;
                    } else if (id != 0) {
                        controller.getById(id, resp);
                        return;
                    }
                    break;
                case POST:
                    if (path.equals(API_POSTS)){
                        controller.save(req.getReader(), resp);
                        return;
                    }
                    break;
                case DELETE:
                    if (id != 0){
                        controller.removeById(id, resp);
                        return;
                    }
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void destroy() {
        context.close();
    }
}

