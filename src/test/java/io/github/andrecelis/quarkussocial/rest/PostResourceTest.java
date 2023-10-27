package io.github.andrecelis.quarkussocial.rest;

import io.github.andrecelis.quarkussocial.domain.model.User;
import io.github.andrecelis.quarkussocial.domain.repository.UserRepository;
import io.github.andrecelis.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    Long userId;

    @BeforeEach
    @Transactional
    public void setUp(){
        var userTest = new User();
        userTest.setName("Usuario de Teste");
        userTest.setAge(34);
        userRepository.persist(userTest);
        userId = userTest.getId();

    }
    @Test
    @DisplayName("Should create post")
    public void createPostTest(){
        var post = new CreatePostRequest();
        post.setText("Texto de teste");
        given()
                .contentType(ContentType.JSON)
                .pathParams("userId",userId)
                .body(JsonbBuilder.create().toJson(post))
                .when()
                .post()
                .then()
                .statusCode(201);

    }

    @Test
    @DisplayName("Should error 404 when create post with user invalid")
    public void createPostTestuSERiNVLID(){
        var post = new CreatePostRequest();
        post.setText("Texto de teste");
        int userInvalid = 999;
        given()
                .contentType(ContentType.JSON)
                .pathParams("userId",userInvalid)
                .body(JsonbBuilder.create().toJson(post))
                .when()
                .post()
                .then()
                .statusCode(404);

    }
}