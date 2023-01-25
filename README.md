# Take Home Exercise 

## Design and Decisions 

### Cache 

Github has a limit of 5,000 requests per hour. 

To get around this I used Guava's cache. Instructions for configuring the cache can be found in the configuration section.

I thought this was a fair design decision as Github profiles and repos are likely not updated frequently, especially the requested info. 

    There are only two hard things in Computer Science: cache invalidation and naming things.

    -- Phil Karlton

**Future Improvement:** The cache can be put behind an interface. This would allow for different cache implementations.
An example would be a distributed cache, such as Redis, for a high-availability, multi-instance deployment.

### Layout

I used a pretty standard Spring Layout: 

 * Controller
 * Service
 * Entity (DTO, for Github and http response)
 * Config (for configurations: retry, cache, http)

Uncached: 

A call is made to the controller, which calls the business logic in the service (via the cache). 
The service uses 2 configs' beans for retry + http (I wanted to introduce some configurability).

The service then calls the Github API, and converts the response to the DTO entities. 

I used Jackson for JSON serialization/deserialization, this allowed me to only define the fields I wanted to use.

Github returns ISO 8601. The service converts this time stamp to the desired format.

Finally, the service returns the DTO to the controller, which converts it to JSON and returns it to the user.

Important to note: The service passes through exceptions through the cache (and controller). This way certain errors will be exposed to the user: 

    ay@AYs-MacBook-Pro test % curl -I localhost:8080/user/username/octocaasfwqweqefwq
    HTTP/1.1 404
    Content-Length: 0
    Date: Wed, 25 Jan 2023 08:13:30 GMT

Cached: 

The cache flow is almost the same, except the cache checks to see if the key is in the cache (and not expired). The username is a fair key here, because Github usernames are unique. 


**Future Improvements:** I didn't use as much abstraction/inheritance as I hoped.
My justification for this is that this is a relatively small project, and I do not need to introduce coupling. 

I noticed that branch uses multiple languages: an improvement would be to use protobuf for the response (there are drawbacks to this in a REST system)

There could be better error handling. I could have used a custom exception, and then used a controller advice to handle it. However, this is a smaller project. 

### HTTP and Retry 

I implemented a retry mechanism for the Github API. This is to handle the case where there may be connection issues.
It's important to note, I referenced the internet for the retry logic (it's been a while since I've written one of these).
I understand how it works, as I made mine for this assignment. 

**Future Improvements:**

I could have used the Github token as a header. This would have been simple to implement, however, I did not have enough time, and wanted to focus on the actual API.

I could have more robust retry logic, however, I had limited time, and it's been a while here. 


### Testing

I thought the service layer was the most important to test. I used Mockito for this. 

I definitely regret not testing more. However, in this case the service layer is the most important, and I wanted to focus on that.

The other layers are close enough to the production releases, that I didn't test them in this assignment. If this was a production app, I would write more robust test cases. 


**Future Improvements:** 

More test cases.

More paths (failure cases, edge cases)


## Run Instructions


### Gradle 

First build with `./gradlew build`

Then run with `./gradlew bootRun`


### Docker

After building in Gradle: 
    
    docker build -t github-api .
    docker run -p 8080:8080 github-api

### Running Commands

    curl -i -X GET localhost:8080/user/username/octocat

    {
    "user_name": "octocat",
    "display_name": "The Octocat",
    "avatar": "https://avatars.githubusercontent.com/u/583231?v=4",
    "geo_location": "San Francisco",
    "email": null,
    "url": "https://api.github.com/users/octocat",
    "created_at": "2011-01-25 18:44:36",
    "repos": [
    {
    "name": "boysenberry-repo-1",
    "url": "https://api.github.com/repos/octocat/boysenberry-repo-1"
    },
    {
    "name": "git-consortium",
    "url": "https://api.github.com/repos/octocat/git-consortium"
    },
    {
    "name": "hello-worId",
    "url": "https://api.github.com/repos/octocat/hello-worId"
    },
    {
    "name": "Hello-World",
    "url": "https://api.github.com/repos/octocat/Hello-World"
    },
    {
    "name": "linguist",
    "url": "https://api.github.com/repos/octocat/linguist"
    },
    {
    "name": "octocat.github.io",
    "url": "https://api.github.com/repos/octocat/octocat.github.io"
    },
    {
    "name": "Spoon-Knife",
    "url": "https://api.github.com/repos/octocat/Spoon-Knife"
    },
    {
    "name": "test-repo1",
    "url": "https://api.github.com/repos/octocat/test-repo1"
    }
    ]
    }

### Testing

    ./gradlew test

## Configuration

Configuration is done in `application.properties`.

logging.level.root for logging level

retry.maxAttempts for max attempts

cache.expire-after for cache expiration (ms) 

cache.max-size for cache size (entires)