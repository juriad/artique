package cz.artique.shared.model.item;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserItemTest extends AppEngineTestCase {

    private UserItem model = new UserItem();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
