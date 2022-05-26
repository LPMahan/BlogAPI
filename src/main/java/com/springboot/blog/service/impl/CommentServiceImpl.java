package com.springboot.blog.service.impl;
import java.util.*;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository, ModelMapper modelMapper) {

        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
    Post post = postRepository.findById(postId).orElseThrow(
            () -> new ResourceNotFoundException("Post", "id", postId));

    comment.setPost(post);

    Comment newComment= commentRepository.save(comment);



        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert entities to dtos
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));
        if(comment.getPost().getId()!= post.getId()){
        throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comment does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));
        if(comment.getPost().getId()!=(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
       Comment updatedComment =  commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));
        if(comment.getPost().getId()!=(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment)
    {
            CommentDto commentdto = modelMapper.map(comment, CommentDto.class);
        /*CommentDto commentdto = new CommentDto();
        commentdto.setId(comment.getId());
        commentdto.setName(comment.getName());
        commentdto.setBody(comment.getBody());
        commentdto.setEmail(comment.getEmail());*/
        return commentdto;
    }

    private Comment mapToEntity(CommentDto commentdto)
    {
        Comment comment = modelMapper.map(commentdto, Comment.class);
       /* Comment comment = new Comment();
        comment.setId(commentdto.getId());
        comment.setBody(commentdto.getBody());
        comment.setEmail(commentdto.getEmail());
        comment.setName(commentdto.getName());*/
        return comment;
    }
}
