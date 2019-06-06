docker build -t pantryspringapp .
docker run --network pantry --network pantry -e SPRING_PROFILES_ACTIVE=local pantryspringapp