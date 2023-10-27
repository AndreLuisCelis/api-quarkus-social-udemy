package io.github.andrecelis.quarkussocial.rest;


import io.github.andrecelis.quarkussocial.domain.model.Follower;
import io.github.andrecelis.quarkussocial.domain.model.User;
import io.github.andrecelis.quarkussocial.domain.repository.FollowerRepository;
import io.github.andrecelis.quarkussocial.domain.repository.UserRepository;
import io.github.andrecelis.quarkussocial.rest.dto.*;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Path("users/{userId}/follower")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private final UserRepository userRepository;
    private final Validator validator;
    private final FollowerRepository followerRepository;

    @Inject
    public FollowerResource(
            UserRepository userRepository,
            Validator validator,
            FollowerRepository followerRepository
            ){
        this.userRepository = userRepository;
        this.validator = validator;
        this.followerRepository = followerRepository;
    }

    @PUT
    @Transactional
    public Response followerUser(@PathParam("userId") Long userId, CreateFollowerRequest createFollowerRequest){
        Set<ConstraintViolation<CreateFollowerRequest>> violations = validator.validate(createFollowerRequest);
        if(!violations.isEmpty()){
            return ResponseError.creatFromValidation(violations)
                    .withStatusErro(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = userRepository.findById(userId);
        User userFollower = userRepository.findById(createFollowerRequest.getFollowerId());
        var jaSegue = followerRepository.follows(userFollower,user);
        if(!jaSegue){
            Follower follower = new Follower();
            follower.setUser(user);
            follower.setFollower(userFollower);
            followerRepository.persist(follower);
        }
        return Response.noContent().build();
    }
    @GET
    public Response listFollowers(@PathParam("userId") Long userId){
        List<Follower> followers=  followerRepository.findByUser(userId);
        FollowerPerUserResponse response = new FollowerPerUserResponse();

        List<FollowerResponse> followersResponse = (List<FollowerResponse>) followers.stream().map(
                foll -> {
                    FollowerResponse follResp = new FollowerResponse();
//                    follResp.setId(foll.getId());
                    follResp.setUserId(foll.getFollower().getId());
                    follResp.setFollowerId(foll.getId());
                    follResp.setName(foll.getFollower().getName());
                    return follResp;
                }
        ).collect(Collectors.toList());

        response.setFallowersCount((long) followers.size());
        response.setContent(followersResponse);
        return Response.ok(response).build();
    }

    @DELETE
    @Transactional
    public Response unFollower(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId){
        User user = userRepository.findById(userId);


        if(user != null){
            followerRepository.deleteByFollowerAndUser(followerId, userId);
            return Response.ok().entity(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id,CreateUserRequest userData){
        User user = userRepository.findById(id);
        if(user != null) {
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
