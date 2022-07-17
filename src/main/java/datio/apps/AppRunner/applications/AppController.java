package datio.apps.AppRunner.applications;

import datio.apps.AppRunner.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/app")
@AllArgsConstructor
@Slf4j
public class AppController {
    public final AppService appService;
    //rebuild endpoints to voids
    @GetMapping("/all")
    public ResponseEntity<Response> index(){
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .data(Map.of("apps",appService.getAllAps()))
                        .massage("Getting all apps.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @GetMapping("/{name}")
    public ResponseEntity<Response> runApp(@PathVariable String name) throws IOException {
        if(name == null) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timestamp(LocalDateTime.now())
                            .massage("Path is empty!")
                            .data(Map.of("error","No path error!"))
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
        String path = appService.getPath(name);
        if(path == null){
            return ResponseEntity.ok(
                    Response.builder()
                            .timestamp(LocalDateTime.now())
                            .massage("Path is empty!")
                            .data(Map.of("error","Could not find " + name + " entry!"))
                            .status(HttpStatus.FORBIDDEN)
                            .statusCode(HttpStatus.FORBIDDEN.value())
                            .build()
            );
        }
        Process pr = Runtime.getRuntime().exec(path);
        appService.addPid(pr.pid(),name);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .massage("Start application " + name + " pid " + pr.pid())
                        .data(Map.of("status","Running"))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PutMapping("/new-list")
    public ResponseEntity<Response> addNewEntry(@RequestBody Collection<BasicApp> apps){
        for (BasicApp app:
             apps) {
            if(appService.checkExists(app.getName())){
                log.warn("Removeing " + app.getName() + " from apps list!");
            }
        }
        apps.removeIf(e -> appService.checkExistsSilent(e.getName()));
        if (apps.isEmpty()) return ResponseEntity.ok(
                        Response.builder()
                        .timestamp(LocalDateTime.now())
                        .massage("Apps list is empty")
                        .data(Map.of("error","Apps list is Empty!"))
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
        appService.addNewApps(apps);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .massage("Add new Entry's")
                        .data(Map.of("status","Created"))
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @PutMapping("/new")
    public ResponseEntity<Response> addNewEntry(@RequestBody BasicApp app){
        if(appService.checkExists(app.getName())){
            return ResponseEntity.ok(
                    Response.builder()
                            .timestamp(LocalDateTime.now())
                            .massage("Entry " + app.getName() + " already exists in database!")
                            .status(HttpStatus.FAILED_DEPENDENCY)
                            .data(Map.of("error","Entry " + app.getName() + " already exists in database!"))
                            .statusCode(HttpStatus.FAILED_DEPENDENCY.value())
                            .build()
            );
        }
        appService.addNewApp(app);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .massage("Add new Entry " + app.getName())
                        .data(Map.of("status","Created"))
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @PutMapping("/new/{name}")
    public ResponseEntity<Response> addNewEntry(
            @PathVariable String name,
            @RequestBody String path){
        if(appService.checkExists(name)){
            return ResponseEntity.ok(
                    Response.builder()
                            .timestamp(LocalDateTime.now())
                            .massage("Entry " + name + " already exists in database!")
                            .status(HttpStatus.FAILED_DEPENDENCY)
                            .data(Map.of("error","Entry " + name + " already exists in database!"))
                            .statusCode(HttpStatus.FAILED_DEPENDENCY.value())
                            .build()
            );
        }
        appService.addNewApp(name, path);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .massage("Add new Entry " + name)
                        .data(Map.of("status","Created"))
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Response> deleteEntry(@PathVariable String name){
        if(!appService.checkExists(name)){
            return ResponseEntity.ok(
                    Response.builder()
                            .timestamp(LocalDateTime.now())
                            .massage("Cannot find " + name + " in database!")
                            .status(HttpStatus.FAILED_DEPENDENCY)
                            .data(Map.of("error","Cannot find " + name + " in database!"))
                            .statusCode(HttpStatus.FAILED_DEPENDENCY.value())
                            .build()
            );
        }
        appService.deleteAppEntry(name);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .massage("Entry " + name + " deleted")
                        .status(HttpStatus.OK)
                        .data(Map.of("status","Succeed"))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
