package com.s4n.dronedelivery.controller;

import com.s4n.dronedelivery.exception.AppException;
import com.s4n.dronedelivery.model.Position;
import com.s4n.dronedelivery.service.DomicileService;
import com.s4n.dronedelivery.util.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DomicileController {


    @Autowired
    private DomicileService domicileService;

    @Value("${route.entry.folder}")
    private String routeEntryFolder;


    public void readFiles() {

        String pathFolder = String.format("%s%s" + routeEntryFolder, System.getProperty("user.dir"), File.separator);
        try (Stream<Path> walk = Files.walk(Paths.get(pathFolder))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(path -> path.toString()).collect(Collectors.toList());

            if(result.size() > 1){
                throw new AppException("Only one drone is allowed for delivery");
            }

            result.forEach(files -> {

                List<String> routes = domicileService.pathsPerFile(files);

                if (Util.validateNameFile(files) && (routes.size() > 0 && routes.size() <= 3)) {
                    List<Position> positionList = domicileService.moveDrone(routes);
                    domicileService.generateReport(positionList);
                } else {
                    throw new AppException("Invalid file or exceeds the number of lunches");
                }
            });
        } catch (IOException e) {
            throw new AppException("Exception file does not meet the minimum parameters", e);
        }
    }
}