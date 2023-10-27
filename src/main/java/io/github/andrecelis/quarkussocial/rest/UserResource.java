package io.github.andrecelis.quarkussocial.rest;


import io.github.andrecelis.quarkussocial.domain.model.User;
import io.github.andrecelis.quarkussocial.domain.repository.UserRepository;
import io.github.andrecelis.quarkussocial.rest.dto.CreateUserRequest;
import io.github.andrecelis.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


    private final UserRepository userRepository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator){
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest createUserRequest){
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
        if(!violations.isEmpty()){
            return ResponseError.creatFromValidation(violations)
                    .withStatusErro(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }
        User user = new User();
        user.setAge(createUserRequest.getAge());
        user.setName(createUserRequest.getName());
        userRepository.persist(user);

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
    }
    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = userRepository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id ){
        User user = userRepository.findById(id);
        if(user != null){
           this.userRepository.delete(user);
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
