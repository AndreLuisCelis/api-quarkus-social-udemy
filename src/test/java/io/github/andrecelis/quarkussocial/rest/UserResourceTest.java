package io.github.andrecelis.quarkussocial.rest;


import io.github.andrecelis.quarkussocial.rest.dto.CreateUserRequest;
import io.github.andrecelis.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {


    @Test
    @DisplayName("should create user successfully")
    @Order(1)
    @Transactional
    public void  createUser(){
        var user = new CreateUserRequest();
        user.setName("Fulano Teste");
        user.setAge(32);
        String userJson = JsonbBuilder.create().toJson(user);
        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .extract().response();
        assertEquals(201,response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("should return error when json in invalid")
    @Order(2)
    public void  createUserValidationErrorTest(){
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);
        String userJson = JsonbBuilder.create().toJson(user);
        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .extract().response();
        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS,response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Object> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0));
        assertNotNull(errors.get(1));
    }

    @Test
    @DisplayName("should return list of users")
    @Order(3)
    public void  listUsersTest(){
         given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                 .statusCode(200)
                 .body("size()", Matchers.is(3));
    }


}