package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Stub
public class PostRepository {
  private final AtomicLong index = new AtomicLong(0);
  private static ConcurrentMap<Long, Post> postRecording = new ConcurrentHashMap<>();

  public List<Post> all() {
    return new ArrayList<>(postRecording.values());
  }

  public Optional<Post> getById(long id) {
    if (!postRecording.containsKey(id)) {
      return Optional.empty();
    }
    return postRecording.values().stream().filter(post -> post.getId() == id).findFirst();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(index.incrementAndGet());
      postRecording.put(index.get(), post);
      return postRecording.get(index.get());
    } else {
      if (postRecording.containsKey(post.getId())) {
        postRecording.put(post.getId(), post);
        return postRecording.get(post.getId());
      } else {
        throw new NotFoundException(post.getId() + " не найден. Чтобы добавить новое значение укажите id = 0");
      }
    }
  }

  public void removeById(long id) {
    if (!postRecording.containsKey(id)) {
      throw new NotFoundException(id + " id не найден");
    }
    postRecording.remove(id);
  }
}