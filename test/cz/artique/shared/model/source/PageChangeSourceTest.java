package cz.artique.shared.model.source;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PageChangeSourceTest extends AppEngineTestCase {

    private PageChangeSource model = new PageChangeSource();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
