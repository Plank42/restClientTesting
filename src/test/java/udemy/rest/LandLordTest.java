package udemy.rest;


import bsh.Console;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import udemy.model.LandLord;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class LandLordTest {


    @BeforeClass
    public static void SetUp() {

        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void getLandLord() {

        Response r = when().get("/landlords");

        r.then()
                .statusCode(200)
                .body("", not(empty()));
       System.out.print(r.body().prettyPrint());
    }

    @Test
    public void postLandLordOne(){

        LandLord landLord = new LandLord("Andrew", "Newton");

        RequestSpecification start = given().contentType(ContentType.JSON).body(landLord);
        start
            .when()
                .post("/landlords")
            .then()
                .statusCode(201)
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(false))
                .body("apartments", is(empty()));

    }

    @Test
    public void postLandLordOnePlus(){

        LandLord landLord = new LandLord("Andrew", "Newton");

        RequestSpecification start = given().contentType(ContentType.JSON).body(landLord);
        String id = start
                .when()
                .post("/landlords")
                .then()
                .statusCode(201)
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(false))
                .body("apartments", is(empty()))
                .extract() // extracts info from response
                .path("id");

        given()
                .pathParam("id",id)
                .when()
                .get("/landlords/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(false))
                .body("apartments", is(empty()));
    }

    @Test
    public void postLandLordTrusted(){

        LandLord landLord = new LandLord("Drew", "Newton", true);

        RequestSpecification start = given().contentType(ContentType.JSON).body(landLord);
        String id = start
                .when()
                .post("/landlords")
                .then()
                .statusCode(201)
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(true))
                .body("apartments", is(empty()))
                .extract() // extracts info from response
                .path("id");

        given()
                .pathParam("id",id)
        .when()
                .get("/landlords/{id}")
        .then()
                .statusCode(200)
                .body("id", is(id))
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(true))
                .body("apartments", is(empty()));
    }

    @Test
    public void negativeLandLord(){

        LandLord empty = new LandLord();

        RequestSpecification start = given().contentType(ContentType.JSON).body(empty);

        start.when()
                .post("/landlords")
        .then()
                .statusCode(400)
                .body("message", is("Fields are with validation errors"))
                .body("fieldErrorDTOs[0].fieldName", is("firstName"))
                .body("fieldErrorDTOs[0].fieldError", is("First name can not be empty"))
                .body("fieldErrorDTOs[1].fieldName", is("lastName"))
                .body("fieldErrorDTOs[1].fieldError", is("Last name can not be empty"));


    }

    @Test
    public void putLandLordUpdate(){

        LandLord update = new LandLord("Dave", "Newton", true);

        RequestSpecification start = given().contentType(ContentType.JSON).body(update);
        String id = start.when()
                .post("/landlords")
                .then()
        .statusCode(201)
                .body("firstName", is(update.getFirstName()))
                .body("lastName", is(update.getLastName()))
                .body("trusted", is(true))
                .body("apartments", is(empty()))
        .extract() // extracts info from response
                .path("id");


        update.setTrusted(false);

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .pathParam("id", id)
        .when()
                .put("/landlords/{id}")
        .then()
                .statusCode(200)
                .body("message", is ("LandLord with id: "+id+" successfully updated"));

        given()
                .pathParam("id",id)
        .when()
                .get("/landlords/{id}")
        .then()
                .statusCode(200)
                .body("id", is(id))
                .body("firstName", is(update.getFirstName()))
                .body("lastName", is(update.getLastName()))
                .body("trusted", is(false));
                //.body("apartments", is(empty()));

    }


    @Test(enabled = false)
    public void postLandLordTwo(){

        LandLord landLord1 = new LandLord("Andrew", "Newton");
        LandLord landLord2 = new LandLord("Peter", "Newton");

        RequestSpecification start = given().contentType(ContentType.JSON).body(landLord1).body(landLord2);

        start.when()
                .post("/landlords")
        .then()
                .statusCode(201)
                .body("firstName", is(landLord2.getFirstName()))
                .body("lastName", is(landLord2.getLastName()))
                .body("trusted", is(false))
                .body("apartments", is(empty()))
                .body("firstName", is(landLord1.getFirstName()))
                .body("lastName", is(landLord1.getLastName()))
                .body("trusted", is(false))
                .body("apartments", is(empty()));
    }



    @Test
    public void deleteLandLord(){
        LandLord update = new LandLord("Fred", "Newton", true);

        RequestSpecification start = given().contentType(ContentType.JSON).body(update);
        String id = start.when()
                .post("/landlords")
        .then()
                .statusCode(201)
                .body("firstName", is(update.getFirstName()))
                .body("lastName", is(update.getLastName()))
                .body("trusted", is(true))
                .body("apartments", is(empty()))
        .extract() // extracts info from response
                .path("id");

        given()
                .pathParam("id", id)
        .when()
                .delete("/landlords/{id}")
        .then()
                .statusCode(200)
                .body("message", is("LandLord with id: " + id + " successfully deleted"));


        given()
                .pathParam("id", id)
        .when()
                .get("/landlords/{id}")
        .then()
                .statusCode(404)
                .body("message", is("There is no LandLord with id: "+ id +""));

    }

    @Test
    public void negativeDeleteLandLord(){
        LandLord update = new LandLord("Dave", "Newton", true);

        RequestSpecification start = given().contentType(ContentType.JSON).body(update);
        String id = start.when()
                .post("/landlords")
                .then()
                .statusCode(201)
                .body("firstName", is(update.getFirstName()))
                .body("lastName", is(update.getLastName()))
                .body("trusted", is(true))
                .body("apartments", is(empty()))
        .extract() // extracts info from response
                .path("id");


    }

}
