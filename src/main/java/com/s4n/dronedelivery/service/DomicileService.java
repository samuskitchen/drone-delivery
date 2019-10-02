package com.s4n.dronedelivery.service;

import com.s4n.dronedelivery.exception.AppException;
import com.s4n.dronedelivery.model.Position;
import com.s4n.dronedelivery.model.enums.Movement;
import com.s4n.dronedelivery.model.enums.Sense;
import com.s4n.dronedelivery.util.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DomicileService {

    @Value("${route.output.folder}")
    private String routeOutputFolder;

    /**
     * Metodo que lee las lineas del archivo que son las rutas del drone
     *
     * @param pathFile
     * @return
     */
    public List<String> pathsPerFile(String pathFile) {
        List<String> listRoutes;

        try (Stream<String> stream = Files.lines(Paths.get(pathFile))) {
            listRoutes = stream.map(String::toUpperCase)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new AppException("Exception file not found or read", e);
        }

        return listRoutes;
    }

    /**
     * Translate the movement code by Cartesian vector
     *
     * @param routes
     * @return
     */
    public List<Position> moveDrone(List<String> routes){

        return routes.stream().map(route -> {
            Position initialPosition = new Position(0,0, Sense.NORTH);

            for (char ch : route.toCharArray()) {

                Movement movement = Util.validateMovement(ch);

                switch (movement) {
                    case A:
                        moveForward(initialPosition);
                        break;
                    case I:
                        turnLeft(initialPosition);
                        break;
                    case D:
                        turnRight(initialPosition);
                        break;
                    default:
                        break;
                }
            }

            if (!Util.validateBlocks(initialPosition)) {
                throw new AppException("The number of blocks established for delivery has been exceeded: " + 10);
            }

            return initialPosition;
        }).collect(Collectors.toList());
    }


    public void generateReport(List<Position> positions) {

        StringBuilder report = new StringBuilder();

        report.append("== Reporte de entregas ==");
        report.append(StringUtils.LF);

        positions.forEach(position -> {
            report.append("(");
            report.append(position.getCoordinateX());
            report.append(",");
            report.append(StringUtils.SPACE);
            report.append(position.getCoordinateY());
            report.append(")");
            report.append(StringUtils.SPACE);
            report.append("dirección");
            report.append(StringUtils.SPACE);
            report.append(Util.senseFormatted(position.getSense().getSense()));
            report.append(StringUtils.LF);

        });

        String fileName = "out.txt";
        String pathFolder;
        try {
            pathFolder = String.format("%s%s" + routeOutputFolder, System.getProperty("user.dir"), File.separator);
            Files.write(Paths.get(pathFolder + fileName), Collections.singleton(report.toString()));
        } catch (IOException e) {
            throw new  AppException("File not Found", e);
        }
    }


    /**
     * Method that translates the code for forward movement
     *
     * @param position
     */
    private void moveForward(Position position) {
        switch (position.getSense()) {
            case NORTH:
                position.setCoordinateY(position.getCoordinateY() + 1);
                break;
            case SOUTH:
                position.setCoordinateY(position.getCoordinateY() - 1);
                break;
            case EAST:
                position.setCoordinateX(position.getCoordinateX() + 1);
                break;
            case WEST:
                position.setCoordinateX(position.getCoordinateX() - 1);
                break;
        }
    }

    /**
     * Method that translates the code to turn left
     *
     * @param position
     */
    private void turnLeft(Position position) {
        switch (position.getSense()) {
            case NORTH:
                position.setSense(Sense.WEST);
                break;
            case EAST:
                position.setSense(Sense.NORTH);
                break;
            case WEST:
                position.setSense(Sense.SOUTH);
                break;
            case SOUTH:
                position.setSense(Sense.EAST);
                break;
        }
    }

    /**
     * Method that translates the code to turn right
     *
     * @param position
     */
    private void turnRight(Position position) {
        switch (position.getSense()) {
            case NORTH:
                position.setSense(Sense.EAST);
                break;
            case EAST:
                position.setSense(Sense.SOUTH);
                break;
            case WEST:
                position.setSense(Sense.NORTH);
                break;
            case SOUTH:
                position.setSense(Sense.WEST);
                break;
        }
    }
}