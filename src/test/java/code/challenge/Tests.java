package code.challenge;

import code.challenge.helper.HttpRequestHelper;
import code.challenge.models.Activities;
import code.challenge.models.Posts;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class Tests {
    HttpRequestHelper httpRequestHelper;

    @BeforeSuite
    public void BeforeSuite() {
        httpRequestHelper = new HttpRequestHelper();
    }

    @Test
    public void Test1() throws URISyntaxException {
        Posts posts = new Posts();
        Response response = httpRequestHelper.post("https://jsonplaceholder.typicode.com/posts", posts);
        response.then().statusCode(201).body("title", is(posts.getTitle()), "body", is(posts.getBody()), "userId", is(posts.getUserId())).body(JsonSchemaValidator.matchesJsonSchema(new File(getClass().getClassLoader().getResource("schema.json").toURI())));
    }

    @Test
    public void Test2() {
        Posts posts = new Posts();
        posts.setTitle("titlemodified");
        posts.setBody("bodymodified");
        Response response = httpRequestHelper.post("https://jsonplaceholder.typicode.com/posts/1", posts);
        response.then().statusCode(201).body("title", is(posts.getTitle()), "body", is(posts.getBody()), "userId", is(posts.getUserId()), "id", is(posts.getId()));
    }

    @Test
    public void Test3() {
        Response response = httpRequestHelper.get("https://jsonplaceholder.typicode.com/posts");
        List<Posts> postsList = response.then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Posts.class);

        //Second part
        response = httpRequestHelper.get("https://jsonplaceholder.typicode.com/posts/" + postsList.get(0).getId());
        response.then().statusCode(200).body("title", is(postsList.get(0).getTitle()), "body", is(postsList.get(0).getBody()), "userId", is(Integer.parseInt(postsList.get(0).getUserId())), "id", is(Integer.parseInt(postsList.get(0).getId())));
    }

    @Test
    public void Test4() {
        Response response = httpRequestHelper.delete("https://jsonplaceholder.typicode.com/posts/15");
        response.then().statusCode(200);
    }

    @Test
    public void Test5() {
        Activities activities = new Activities();
        Response response = httpRequestHelper.get("https://fakerestapi.azurewebsites.net/api/v1/Activities/1");
        response.then().statusCode(200).body("id", is(Integer.parseInt(activities.getId())), "title", is(activities.getTitle()), "dueDate", not(empty()), "completed", is(activities.isCompleted()));
    }
}
