package io.github.andrecelis.quarkussocial.rest;


import io.github.andrecelis.quarkussocial.domain.model.Post;
import io.github.andrecelis.quarkussocial.domain.model.User;
import io.github.andrecelis.quarkussocial.domain.repository.PostRepository;
import io.github.andrecelis.quarkussocial.domain.repository.UserRepository;
import io.github.andrecelis.quarkussocial.rest.dto.CreatePostRequest;
import io.github.andrecelis.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.grammars.hql.HqlParser;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId")Long id, CreatePostRequest createPostRequest){
        User user = this.userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
    }
        Post post = new Post();
        post.setText(createPostRequest.getText());
        post.setUser(user);
        post.setDateTime(LocalDateTime.now());
        postRepository.persist(post);
        return Response
                .status(Response.Status.CREATED)
                .entity(post)
                .build();
    }
    @GET
    public Response listPosts(@PathParam("userId")Long id, @PathParam("postId")Long postId){
        User user = this.userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        PanacheQuery<Post> query = postRepository
                .find("user", Sort.by("dateTime",Sort.Direction.Descending), user);
        List<Post> listPost = query.list();
        var postResponse = listPost.stream().map( post -> PostResponse.fromEntity(post)).collect(Collectors.toList());
        return Response.ok(postResponse).build();
    }
    @DELETE
    @Transactional
    public Response deletePost(@PathParam("userId")Long userId,
                               @QueryParam("postId") Long postId) {
        User user = this.userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = postRepository.findById(postId);
        if( post == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.postRepository.delete(post);
        return Response.ok().entity(post).build();
    }
}
