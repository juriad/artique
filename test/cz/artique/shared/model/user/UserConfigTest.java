package cz.artique.shared.model.user;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;

import cz.artique.shared.model.config.ClientConfig;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserConfigTest extends AppEngineTestCase {

    private ClientConfig model = new ClientConfig();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
