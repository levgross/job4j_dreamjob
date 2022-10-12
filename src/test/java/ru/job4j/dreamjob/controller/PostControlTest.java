package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostControlTest {
    @Test
    public void whenPost() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post", "description", LocalDateTime.now()),
                new Post(2, "New post", "description", LocalDateTime.now())
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostControl postControl = new PostControl(
                postService,
                cityService
        );
        String page = postControl.posts(model, new MockHttpSession());
        verify(model).addAttribute("posts", posts);
        assertThat(page).isEqualTo("posts");
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "New post", "description", LocalDateTime.now());
        input.setCity(new City(1, ""));
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostControl postControl = new PostControl(
                postService,
                cityService
        );
        String page = postControl.createPost(input);
        verify(postService).add(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenUpdatePost() {
        Post input = new Post(1, "New post", "description", LocalDateTime.now());
        input.setCity(new City(1, ""));
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostControl postControl = new PostControl(
                postService,
                cityService
        );
        String page = postControl.updatePost(input);
        verify(postService).update(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenFormAddPost() {
        List<City> cities = Arrays.asList(
                new City(1, "City"),
                new City(2, "Town")
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostControl postControl = new PostControl(
                postService,
                cityService
        );
        String page = postControl.formAddPost(model, new MockHttpSession());
        verify(model).addAttribute("cities", cities);
        assertThat(page).isEqualTo("addPost");
    }

    @Test
    public void whenFormUpdatePost() {
        List<City> cities = Arrays.asList(
                new City(1, "City"),
                new City(2, "Town")
        );
        Post input = new Post(1, "New post", "description", LocalDateTime.now());
        input.setCity(cities.get(1));
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        when(postService.findById(1)).thenReturn(input);
        PostControl postControl = new PostControl(
                postService,
                cityService
        );
        String page = postControl.formUpdatePost(model, 1, new MockHttpSession());
        verify(model).addAttribute("cities", cities);
        verify(model).addAttribute("post", input);
        assertThat(page).isEqualTo("updatePost");
    }
}
