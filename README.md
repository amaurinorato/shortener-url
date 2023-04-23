## URL Shortener Service

This service expose endpoints to shorten URL and find a shortener URL

The endpoints exposed are:
* POST: localhost:8080/shorten
* GET: localhost:8080/{id}

### POST localhost:8080/shorten
This endpoint receives a body like this:
```
{
    "url": "http://google.com"
}
```
And returns the following body with the status 201:
```
{
    "url": "http://localhost:8080/aa2239c17609b21e"
}
```

### GET localhost:8080/{id}
Given an id, this endpoint returns the long version of the saved URL, if it exists in the database
This endpoint also has the capability to redirect, if the user sends the query param like this:
`redirect=true`
The success response status of this endpoint is 200 with the following body:
```
{
    "url": "http://google.com"
}
```

In case of the id doesn't exist in the database, the response status will be a 404

### Build
We use Docker, so building is easy. Basically, access the project root directory and run: 
`docker build . -tshortener-url:latest`

When it's done, then starts the container by running:
`docker run -p8080:8080 shortener-url:latest`

If everything works fine, then you will be able to call the endpoints
