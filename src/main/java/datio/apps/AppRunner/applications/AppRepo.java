package datio.apps.AppRunner.applications;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppRepo extends MongoRepository<App,String> {
    <S extends App> S findByName(String name);
}
