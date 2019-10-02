package com.s4n.dronedelivery.controller;

import com.s4n.dronedelivery.model.Position;
import com.s4n.dronedelivery.model.enums.Sense;
import com.s4n.dronedelivery.service.DomicileService;
import java.io.File;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DomicileControllerTest {

    File entryFile;
    List<String> routes;
    Position position;
    List<Position> positionList;

    @MockBean
    DomicileController domicileController;

    @MockBean
    DomicileService domicileService;

    @Before
    public void setUp() {
        entryFile = new File("src/file/entry/in.txt");
        routes = Collections.singletonList("AADDA, AAAII, DDAA");
        position = new Position(0, 0, Sense.NORTH);
        positionList = Collections.singletonList(position);
    }

    @Test
    public void readFiles() {
        List<String> listResult = Collections.EMPTY_LIST;

        //when
        given(domicileService.pathsPerFile(entryFile.getPath())).willReturn(listResult);
        given(domicileService.moveDrone(routes)).willReturn(positionList);

        //then
        domicileService.generateReport(positionList);
        domicileController.readFiles();
    }
}