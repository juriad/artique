package cz.artique.shared.model.user;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserConfigTest extends AppEngineTestCase {

    private UserConfig model = new UserConfig();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
