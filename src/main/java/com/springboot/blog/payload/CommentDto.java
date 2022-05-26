package com.springboot.blog.payload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Api(value = "comment model information")
@Data
public class CommentDto {
    @ApiModelProperty(value= " comment id")
    private long id;
    @NotEmpty(message = "Name should not be empty")
    @ApiModelProperty(value= " comment name")
    private String name;
    @ApiModelProperty(value= " comment email")
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @ApiModelProperty(value= " comment body")
    @NotEmpty
    @Size(min = 10, message = "Message must be more than 10 characters")
    private String body;

}
