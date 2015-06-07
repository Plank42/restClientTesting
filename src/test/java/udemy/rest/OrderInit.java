package udemy.rest;
//
//import com.jayway.restassured.RestAssured;
//import com.jayway.restassured.response.Response;
//import org.testng.annotations.Test;
//
//import java.security.KeyStoreException;
//
//import static com.jayway.restassured.RestAssured.when;
//import static com.jayway.restassured.config.SSLConfig.sslConfig;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by anewton on 05/06/2015.
 */
public class OrderInit {
/*
    @Test
    public void getOrderInit() throws KeyStoreException {

       RestAssured.baseURI = "https://staging.vpn.hidemyass.com";
        RestAssured.config = RestAssured.config().sslConfig(sslConfig()
                .with().keystoreType("pem").and().strictHostnames());
        //String trustStorePath =
        //KeyStore keyStore = KeyStore.getInstance("privax.ca");

        Response init = when().get("/checkout-api/order/init");
        *//*when()
                .get("/checkout-api/order/init")
                .then()
                .statusCode(200)
                .body("", is(notNullValue()));*//*

        init.then()
                .statusCode(200);
        init.then()
                .body(is(notNullValue()));
    }

*/
}
