package cz.artique.shared.model.item;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class LinkItemTest extends AppEngineTestCase {

    private LinkItem model = new LinkItem();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
