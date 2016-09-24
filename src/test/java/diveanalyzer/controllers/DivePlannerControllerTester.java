package diveanalyzer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import diveanalyzer.models.DivePoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DivePlannerControllerTester {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void getPlan() throws Exception {
        LinkedList<DivePoint> dive = new LinkedList<>();
        dive.add(new DivePoint(0,0));
        dive.add(new DivePoint(20,10));
        dive.add(new DivePoint(0,1));
        String body = jacksonObjectMapper.writeValueAsString(dive);
        mvc.perform(MockMvcRequestBuilders.put("/dive-plan").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON_UTF8).content(body))
                .andExpect(status().isOk());
    }
}
