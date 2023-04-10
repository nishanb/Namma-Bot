## Redis as cache setup 
### Summary: 

Redis can be set up on Docker and used as a caching layer for database calls. This can improve the performance of the application by reducing the load on the database server. Redis can be configured in the application.yaml file.



### Steps:

- Install Docker on your system.
- Run the following command to start a Redis container: `docker run -p 6379:6379 redis`
- Test that Redis is running by running the command docker ps. You should see the Redis container listed in the output.
- Configure Redis in the application.yaml file by adding the following line: `spring.redis.url: {REDIS_URL}`
- Use Redis as a caching layer for your database calls in your application code.

### Accessing redis 
- Login : `docker exec -it $(docker ps | grep redis | awk -F " " '{print $1}')  redis-cli `
- View Keys : `redis-cli > keys *`