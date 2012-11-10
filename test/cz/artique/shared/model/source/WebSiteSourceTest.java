package cz.artique.shared.model.source;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class WebSiteSourceTest extends AppEngineTestCase {

    private WebSiteSource model = new WebSiteSource();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
