package datio.apps.AppRunner.applications;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@Document("apps")
public class App {
    @Id
    @Field
    private String id;
    @Field
    public String name;
    @Field
    private String localPath;
    @Field
    public Long pid;

    public App(String name, String localPath) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.localPath = localPath;
    }


}
