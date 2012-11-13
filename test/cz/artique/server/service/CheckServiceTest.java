package cz.artique.server.service;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CheckServiceTest extends AppEngineTestCase {

    private CheckService service = new CheckService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }
}
