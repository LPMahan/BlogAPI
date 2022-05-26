package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postdto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);


    PostDto getPostById(long id);

    PostDto updatePost(PostDto postdto, long id);
    void deletePostById(long id);
}
