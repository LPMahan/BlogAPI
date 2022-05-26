package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceIMPL implements PostService {

    private PostRepository postrepository;

    private ModelMapper mapper;

    public PostServiceIMPL(PostRepository postrepository, ModelMapper mapper) {
        this.postrepository = postrepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postdto) {

            Post post = mapToEntity(postdto);


            Post newpost = postrepository.save(post);


            //convert entity to dto
        PostDto postResponse = mapToDto(newpost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageable =  PageRequest.of(pageNo, pageSize, sort);


        Page<Post> posts = postrepository.findAll( pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content =listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postrepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postdto, long id) {
        //get post by id from db
        Post post = postrepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id",id));

        post.setTitle(postdto.getTitle());
        post.setDescription(postdto.getDescription());
        post.setContent(postdto.getContent());
        Post updatedPost = postrepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postrepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id",id));
        postrepository.delete(post);
    }

    private PostDto mapToDto(Post post)
    {
        PostDto postdto = mapper.map(post,PostDto.class);

      /* // PostDto postdto= new PostDto();
        postdto.setId(post.getId());
        postdto.setTitle(post.getTitle());
        postdto.setContent(post.getContent());
        postdto.setDescription(post.getDescription()); */
        return postdto;
    }
        private Post mapToEntity(PostDto postdto)
        {
                Post post = mapper.map(postdto, Post.class);
           /* Post post = new Post();
            post.setTitle(postdto.getTitle());
            post.setDescription(postdto.getDescription());
            post.setContent(postdto.getContent());*/
            return post;
        }

}
