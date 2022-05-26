package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@Api(value = "CRUD REST APIs for Comment Response")
@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private CommentService commentservice;




    public CommentController(CommentService commentservice) {
        this.commentservice = commentservice;
    }
    @ApiOperation(value = "create comment rest api")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId,
                                                    @Valid @RequestBody CommentDto commentDto){
    return new ResponseEntity<>(commentservice.createComment(postId,commentDto), HttpStatus.CREATED);
    }
    @ApiOperation(value = "get comments by post id rest api")
    @GetMapping("/posts/{postId}/comments")
        public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") Long postId)
        {
            return commentservice.getCommentsByPostId(postId);
        }
    @ApiOperation(value = "get single comment by id  rest api")
        @GetMapping("/posts/{postId}/comments/{id}")
        public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
                                                         @PathVariable(value = "id") Long commentId){
        CommentDto commentdto = commentservice.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentdto, HttpStatus.OK);

        }
    @ApiOperation(value = "update comment by id rest api")
        @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value= "id") Long commentId,
                                                   @Valid @RequestBody CommentDto commentdto){
        CommentDto updatedComment = commentservice.updateComment(postId, commentId, commentdto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);

    }
    @ApiOperation(value = "delete comment by id rest api")
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "id") Long commentId){

        commentservice.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }


}
