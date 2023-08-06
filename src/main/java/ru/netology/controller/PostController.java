package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private static List<Post> postRecording = new CopyOnWriteArrayList<>();
  public long index = 0;
  private final PostService service;
  private Gson gson;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.all();
    gson = new Gson();
    response.getWriter().print(gson.toJson(postRecording));
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    gson = new Gson();
    if (postRecording.size() >= id) {
      final var post = postRecording.get((int) id - 1);
      response.getWriter().print(gson.toJson(post));
    } else {
      response.getWriter().print(new NotFoundException(id + " id не найден"));
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    gson = new Gson();
    final var post = gson.fromJson(body, Post.class);
    if (post.getId() == 0) {
      index = index + 1;
      post.setId(index);
      postRecording.add(post);
      response.getWriter().print(gson.toJson(postRecording.get((int) index - 1)));
    } else {
      if (postRecording.size() > post.getId() - 1) {
        postRecording.remove((int) post.getId() - 1);
        postRecording.add((int) post.getId() - 1, post);
        response.getWriter().print(gson.toJson(postRecording.get((int) post.getId() - 1)));
      } else {
        response.getWriter().print(new NotFoundException(post.getId() + " не найден. Чтобы добавить новое значение укажите id = 0"));
      }
    }
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    if (postRecording.size() > id) {
      postRecording.remove((int) id - 1);
      response.getWriter().print(new NotFoundException("Запись с id - " + id + " удалена"));
    } else {
      response.getWriter().print(new NotFoundException(id + " id не найден"));
    }
  }
}