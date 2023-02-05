package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            long id = 0;
            if (path.matches("/api/posts/\\d+")){
                id = Long.parseLong(path.substring(path.lastIndexOf("/")+1));
            }

            switch (method){
                case "GET":
                    if (path.equals("/api/posts")){
                        controller.all(resp);
                        return;
                    } else if (id != 0) {
                        controller.getById(id, resp);
                        return;
                    }
                    break;
                case "POST":
                    if (path.equals("/api/posts")){
                        controller.save(req.getReader(), resp);
                        return;
                    }
                    break;
                case "DELETE":
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
}

