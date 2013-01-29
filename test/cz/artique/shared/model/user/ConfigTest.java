package cz.artique.shared.model.user;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;

import cz.artique.shared.model.config.Config;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ConfigTest extends AppEngineTestCase {

    private Config model = new Config();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
