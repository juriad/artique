package cz.artique.shared.model.label;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ListFilterTest extends AppEngineTestCase {

    private ListFilter model = new ListFilter();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
