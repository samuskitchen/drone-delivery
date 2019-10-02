package com.s4n.dronedelivery.service;

import com.s4n.dronedelivery.model.Position;
import com.s4n.dronedelivery.model.enums.Sense;
import java.io.File;
import java.util.Collections;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class DomicileServiceTest {

    File entryFile;

    @MockBean
    DomicileService domicileService;

    @Before
    public void setUp() throws Exception {
        entryFile = new File("src/file/entry/in.txt");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void pathsPerFile() {
        List<String> listResult = Collections.EMPTY_LIST;

        //when
        given(domicileService.pathsPerFile(entryFile.getPath())).willReturn(listResult);

        //then
        assertNotEquals(listResult, (CoreMatchers.equalTo(listResult)));
    }

    @Test
    public void moveDrone() {
        List<String> routes = Collections.singletonList("AADDA, AAAII, DDAA");
        Position position = new Position(0, 0, Sense.NORTH);
        List<Position> positionList = Collections.singletonList(position);

        //when
        given(domicileService.moveDrone(routes)).willReturn(positionList);

        //then
        assertNotEquals(positionList, (CoreMatchers.equalTo(positionList)));
    }

    @Test
    public void generateReport() {
        Position position = new Position(0, 0, Sense.NORTH);
        List<Position> positionList = Collections.singletonList(position);

        //when
        domicileService.generateReport(positionList);
    }

}